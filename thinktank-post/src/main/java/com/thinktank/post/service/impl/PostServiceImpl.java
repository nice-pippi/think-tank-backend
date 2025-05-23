package com.thinktank.post.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.thinktank.api.clients.BlockClient;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.common.utils.ObjectMapperUtil;
import com.thinktank.common.utils.R;
import com.thinktank.common.utils.RabbitMQUtil;
import com.thinktank.common.utils.RedisCacheUtil;
import com.thinktank.generator.dto.PostInfoDto;
import com.thinktank.generator.entity.*;
import com.thinktank.generator.mapper.*;
import com.thinktank.generator.vo.BlockMasterListVo;
import com.thinktank.generator.vo.PostCommentsVo;
import com.thinktank.generator.vo.PostHotVo;
import com.thinktank.generator.vo.PostInfoVo;
import com.thinktank.post.config.AddPostClickRecordsFanoutConfig;
import com.thinktank.post.config.AddPostDocFanoutConfig;
import com.thinktank.post.config.DeletePostFanoutConfig;
import com.thinktank.post.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉20⽇ 19:34
 * @Description: 帖子业务接口实现类
 * @Version: 1.0
 */
@Slf4j
@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private BlockInfoMapper blockInfoMapper;

    @Autowired
    private PostInfoMapper postInfoMapper;

    @Autowired
    private PostCommentsMapper postCommentsMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private MessagePrivateMapper messagePrivateMapper;

    @Autowired
    private MessageChatRoomMapper messageChatRoomMapper;

    @Autowired
    private PostLikesMapper postLikesMapper;

    @Autowired
    private PostScoreMapper postScoreMapper;

    @Autowired
    private PostClickRecordsMapper postClickRecordsMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private BlockClient blockClient;

    /**
     * 验证板块是否存在，若存在则返回板块信息
     *
     * @param id
     * @return
     */
    private BlockInfo getBlockExists(Long id) {
        BlockInfo blockInfo = blockInfoMapper.selectById(id);
        if (blockInfo == null) {
            log.warn("板块'{}'不存在", id);
            throw new ThinkTankException("当前板块不存在！");
        }
        return blockInfo;
    }

    @Transactional
    @Override
    public void publish(PostInfoDto postInfoDto) {
        // 验证当前板块是否存在
        getBlockExists(postInfoDto.getBlockId());

        // 获取登录用户id
        long loginId = StpUtil.getLoginIdAsLong();

        // 验证当前用户是否被所在板块禁言
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, loginId);
        queryWrapper.eq(SysUserRole::getBlockId, postInfoDto.getBlockId());
        queryWrapper.eq(SysUserRole::getRoleId, 104L);
        if (sysUserRoleMapper.selectCount(queryWrapper) > 0) {
            log.warn("用户'{}'在'{}'被禁言，无法发布帖子", loginId, postInfoDto.getBlockId());
            throw new ThinkTankException("您在当前板块已被禁言，无法发布帖子！");
        }

        // 写入记录到帖子信息表
        PostInfo postInfo = new PostInfo();
        BeanUtils.copyProperties(postInfoDto, postInfo);
        postInfo.setUserId(loginId);
        postInfoMapper.insert(postInfo);

        // 写入记录到帖子评论表
        PostComments postComments = new PostComments();
        BeanUtils.copyProperties(postInfoDto, postComments);
        postComments.setPostId(postInfo.getId());
        postComments.setUserId(loginId);
        postComments.setTopicFlag(1);
        postCommentsMapper.insert(postComments);

        // 验证是否要提问
        Integer count = postInfoDto.getCount();
        if (count > 0) {
            // 随机抽取用户
            List<Long> randomList = sysUserMapper.selectRandomList(count);
            if (randomList.size() < count) {
                throw new ThinkTankException("发布失败，当前系统注册用户人数未超过" + count + "人。");
            }

            // 遍历随机用户，将提问信息记录到用户私信表中
            String url = String.format("http://localhost:8585/postIndex/%s/%s", postComments.getBlockId(), postComments.getPostId());
            String context = String.format("我向您提问了一个问题:%s，快来为我解答吧~(%s)", postInfoDto.getTitle(), url); // 消息内容
            for (Long acceptUserId : randomList) {
                MessageChatRoom messageChatRoom = new MessageChatRoom();
                messageChatRoom.setUserIdA(loginId);
                messageChatRoom.setUserIdB(acceptUserId);
                messageChatRoom.setLatestContent(context);
                messageChatRoomMapper.insert(messageChatRoom);

                MessagePrivate messagePrivate = new MessagePrivate();
                messagePrivate.setSendUserId(loginId);
                messagePrivate.setAcceptUserId(acceptUserId);
                messagePrivate.setContent(context);
                messagePrivate.setChatRoomId(messageChatRoom.getId());
                messagePrivateMapper.insert(messagePrivate);
            }
        }

        // 提交待处理帖子信息到mq队列，由队列异步处理写入es文档操作
        CorrelationData correlationData = RabbitMQUtil.getCorrelationData();
        Message message = RabbitMQUtil.transformMessage(postInfo);
        rabbitTemplate.convertAndSend(AddPostDocFanoutConfig.FANOUT_EXCHANGE, "", message, correlationData);
    }

    /**
     * 细粒度验证用户身份
     *
     * @param postId
     * @return
     */
    private Boolean validateRole(Long postId) {

        // 验证是否超级管理员身份
        if (StpUtil.hasRole("super-admin")) {
            return true;
        }

        // 获取当前登录用户id
        long loginId = StpUtil.getLoginIdAsLong();

        // 验证该帖子是否由用户自己发布的
        PostInfo postInfo = postInfoMapper.selectById(postId);
        if (postInfo == null) {
            log.warn("帖子id:'{}'不存在", postId);
            throw new ThinkTankException("该帖子id不存在！");
        }

        if (postInfo.getUserId().equals(loginId)) {
            return true;
        }

        // 验证是否该板块板主
        if (StpUtil.hasRoleOr("big-master", "small-master")) {
            LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysUserRole::getUserId, loginId);
            queryWrapper.eq(SysUserRole::getBlockId, postInfo.getBlockId());
            SysUserRole sysUserRole = sysUserRoleMapper.selectOne(queryWrapper);
            if (sysUserRole == null) {
                return false;
            }

            // 验证是否被禁言用户，若不是则代表该用户有当前板块的板主或小版主角色
            return !sysUserRole.getRoleId().equals(104L);
        }
        return false;
    }

    @Transactional
    @Override
    public void delete(Long postId) {
        if (!validateRole(postId)) {
            throw new ThinkTankException("您无权对他人的帖子进行操作！");
        }

        // 删除该帖子下所有评论
        LambdaQueryWrapper<PostComments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostComments::getPostId, postId);
        postCommentsMapper.delete(queryWrapper);

        // 删除该帖子
        postInfoMapper.deleteById(postId);

        // 提交待处理帖子id到mq队列，由队列异步处理删除es文档操作
        CorrelationData correlationData = RabbitMQUtil.getCorrelationData();
        rabbitTemplate.convertAndSend(DeletePostFanoutConfig.FANOUT_EXCHANGE, "", postId, correlationData);
    }

    @Override
    public List<PostInfoVo> getRecommendedPostsByCollaborativeFiltering() {
        // 获取所有用户帖子评分矩阵
        Map<Long, Map<Long, Integer>> matrix = getPostScoreMatrix();

        // 计算用户相似度，取出与当前登录用户相似度高的其他用户id
        List<Long> similarUsers = getSimilarUsers(matrix);

        // 从相似度高的其他用户中取出评分较高并且当前用户未看过的帖子
        return getRecommendedPosts(similarUsers);
    }

    /**
     * 获取用户帖子评分矩阵
     *
     * @return 用户帖子评分矩阵
     */
    private Map<Long, Map<Long, Integer>> getPostScoreMatrix() {
        // 取出所有用户评分数据
        List<PostScore> postScores = postScoreMapper.selectList(null);

        // 构造用户帖子评分矩阵
        Map<Long, Map<Long, Integer>> matrix = new HashMap<>();
        for (PostScore postScore : postScores) {
            matrix.computeIfAbsent(postScore.getUserId(), k -> new HashMap<>())
                    .putIfAbsent(postScore.getPostId(), postScore.getScore());
        }
        return matrix;
    }

    /**
     * 获取相似度高的用户id列表
     *
     * @param matrix 用户帖子评分矩阵
     * @return 相似度高的用户id列表
     */
    private List<Long> getSimilarUsers(Map<Long, Map<Long, Integer>> matrix) {
        // 当前登录用户id
        long loginId = StpUtil.getLoginIdAsLong();

        // 取出当前登录用户的帖子评分记录
        Map<Long, Integer> currentUserScores = matrix.get(loginId);

        // 判断当前登录用户是否有帖子评分记录，若无则返回空列表
        if (currentUserScores == null || currentUserScores.isEmpty()) {
            return Collections.emptyList();
        }

        // 相似度高的用户id列表
        List<Long> userList = new LinkedList<>();

        // 相似度基准线
        float threshold = 0.8f;

        // 遍历其他用户，计算相似度
        for (Map.Entry<Long, Map<Long, Integer>> entry : matrix.entrySet()) {
            // 获取其他用户id
            Long otherUserId = entry.getKey();
            if (otherUserId.equals(loginId)) {
                continue;
            }

            // 获取其他用户帖子评分
            Map<Long, Integer> otherUserScores = entry.getValue();
            List<Integer> x = new ArrayList<>();
            List<Integer> y = new ArrayList<>();
            for (Map.Entry<Long, Integer> scoreEntry : currentUserScores.entrySet()) {
                Long postId = scoreEntry.getKey();
                if (otherUserScores.containsKey(postId)) {
                    x.add(scoreEntry.getValue());
                    y.add(otherUserScores.get(postId));
                }
            }
            // 使用余弦相似度计算
            Double similarity = cosineSimilarityList(x, y);

            // 如果相似度大于阈值，添加到相似用户列表中
            if (similarity >= threshold) {
                userList.add(otherUserId);
            }
        }
        return userList;
    }

    /**
     * 计算余弦相似度
     */
    private Double cosineSimilarityList(List<Integer> x, List<Integer> y) {
        // 转换为double数组
        double[] xArrays = x.stream().mapToDouble(Integer::doubleValue).toArray();
        double[] yArrays = y.stream().mapToDouble(Integer::doubleValue).toArray();

        //  计算余弦相似度
        RealVector a = new ArrayRealVector(xArrays);
        RealVector b = new ArrayRealVector(yArrays);
        if (xArrays.length != yArrays.length) {
            log.warn("两个向量长度不一致！");
            throw new ThinkTankException("两个向量长度不一致！");
        }
        return (a.dotProduct(b)) / (a.getNorm() * b.getNorm());
    }

    /**
     * 从相似度高的其他用户中取出评分较高并且当前用户未看过的帖子id
     *
     * @param similarUsers 相似用户id列表
     * @return 推荐帖子列表
     */
    private List<PostInfoVo> getRecommendedPosts(List<Long> similarUsers) {
        // 判断当前相似用户集合是否为空
        if (similarUsers == null || similarUsers.isEmpty()) {
            return getLatestPosts();
        }

        // 当前登录用户id
        long loginId = StpUtil.getLoginIdAsLong();

        // 评分基准线
        int threshold = 3;

        // 取出所有相似用户评分高于基准线的帖子id集合
        LambdaQueryWrapper<PostScore> postScoreLambdaQueryWrapper = new LambdaQueryWrapper<>();
        postScoreLambdaQueryWrapper.in(PostScore::getUserId, similarUsers);
        postScoreLambdaQueryWrapper.ge(PostScore::getScore, threshold);
        postScoreLambdaQueryWrapper.select(PostScore::getPostId);
        List<Long> postIdList = postScoreMapper.selectList(postScoreLambdaQueryWrapper)
                .stream().map(PostScore::getPostId).collect(Collectors.toList());

        // 获取当前登录用户已经点击过的帖子id列表
        LambdaQueryWrapper<PostClickRecords> postClickRecordsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        postClickRecordsLambdaQueryWrapper.eq(PostClickRecords::getUserId, loginId);
        postClickRecordsLambdaQueryWrapper.select(PostClickRecords::getPostId);
        List<Long> clickPostIdList = postClickRecordsMapper.selectList(postClickRecordsLambdaQueryWrapper)
                .stream().map(PostClickRecords::getPostId).collect(Collectors.toList());

        // 过滤出当前登录用户未点击过的帖子id
        List<Long> filteredPostIdList = postIdList.stream()
                .filter(postId -> !clickPostIdList.contains(postId))
                .collect(Collectors.toList());

        // 根据过滤后的帖子id集合，查询并获取对应的帖子信息
        if (!filteredPostIdList.isEmpty()) {
            LambdaQueryWrapper<PostInfo> postInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            postInfoLambdaQueryWrapper.in(PostInfo::getId, filteredPostIdList);
            List<PostInfo> postInfoList = postInfoMapper.selectList(postInfoLambdaQueryWrapper);
            return postInfoList.stream().map(this::getPostInfo).collect(Collectors.toList());
        }
        return getLatestPosts();
    }

    @Override
    public List<PostInfoVo> getLatestPosts() {
        // 查数据库
        LambdaQueryWrapper<PostInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(PostInfo::getCreateTime);
        queryWrapper.last("limit 30");
        List<PostInfo> postInfoList = postInfoMapper.selectList(queryWrapper);

        // 使用stream流将postInfoList转换为PostInfoVo
        return postInfoList.stream().map(this::getPostInfo).collect(Collectors.toList());
    }

    /**
     * 根据帖子id封装vo所需信息
     *
     * @param postInfo
     * @return
     */
    private PostInfoVo getPostInfo(PostInfo postInfo) {
        // 根据帖子id获取该帖子前五条评论
        List<PostCommentsVo> postComments = postCommentsMapper.getPostCommentsVoByFive(postInfo.getId());

        // 获取主题帖
        PostCommentsVo postCommentsVo = postComments.stream().filter(item -> item.getTopicFlag() == 1).findFirst().orElse(null);

        if (postCommentsVo == null) {
            throw new ThinkTankException("该帖子不存在主题帖！");
        }

        // 去掉帖子内容中的HTML标签以及制表符
        String content = postCommentsVo.getContent().replaceAll("<.*?>", "").replaceAll("\\t", "");

        // 收集所有帖子评论中的图片URL
        Pattern pattern = Pattern.compile("<img\\s+src=\"([^\"]+)\"");
        List<String> imageUrlList = new ArrayList<>();
        for (PostComments comment : postComments) {
            Matcher matcher = pattern.matcher(comment.getContent());
            while (matcher.find()) {
                imageUrlList.add(matcher.group(1));
            }
        }

        PostInfoVo postInfoVo = new PostInfoVo();
        BeanUtils.copyProperties(postInfo, postInfoVo);
        postInfoVo.setUsername(postCommentsVo.getUsername());
        postInfoVo.setBlockName(postCommentsVo.getBlockName());
        postInfoVo.setImages(imageUrlList);
        postInfoVo.setContent(content);
        return postInfoVo;
    }

    @Override
    public R<List<PostInfoVo>> page(PostInfoDto postInfoDto) {
        Page<PostInfo> page = new Page<>(postInfoDto.getCurrentPage(), postInfoDto.getSize());
        LambdaQueryWrapper<PostInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostInfo::getBlockId, postInfoDto.getBlockId());
        queryWrapper.like(StringUtils.isNotEmpty(postInfoDto.getTitle()), PostInfo::getTitle, postInfoDto.getTitle());
        queryWrapper.orderByDesc(PostInfo::getCreateTime);
        Page<PostInfo> postInfoPage = postInfoMapper.selectPage(page, queryWrapper);

        // 转换成PostInfoVo
        List<PostInfoVo> list = postInfoPage.getRecords().stream().map(this::getPostInfo).collect(Collectors.toList());

        return R.success(list).add("total", postInfoPage.getTotal());
    }

    @Override
    public PostInfo getTitleAndTag(Long postId) {
        LambdaQueryWrapper<PostInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(PostInfo::getTitle, PostInfo::getTag);
        queryWrapper.eq(PostInfo::getId, postId);
        PostInfo postInfo = postInfoMapper.selectOne(queryWrapper);

        if (postInfo == null) {
            log.warn("帖子'{}'不存在", postId);
            throw new ThinkTankException("当前帖子不存在！");
        }
        return postInfo;
    }

    @Override
    public R<List<PostInfoVo>> getPageByPublishedPosts(Long id, Integer currentPage) {
        Page<PostInfo> page = new Page<>(currentPage, 15);
        LambdaQueryWrapper<PostInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostInfo::getUserId, id);
        queryWrapper.orderByDesc(PostInfo::getCreateTime);
        Page<PostInfo> postInfoPage = postInfoMapper.selectPage(page, queryWrapper);

        List<PostInfoVo> list = postInfoPage.getRecords().stream().map(this::getPostInfo).collect(Collectors.toList());

        return R.success(list).add("total", postInfoPage.getTotal());
    }

    /**
     * 验证帖子是否存在，若存在则返回帖子信息
     *
     * @param postId 帖子ID
     * @return 存在的帖子信息
     */
    private PostInfo getPostExists(Long postId) {
        PostInfo postInfo = postInfoMapper.selectById(postId);

        if (postInfo == null) {
            log.warn("帖子'{}'不存在", postId);
            throw new ThinkTankException("帖子不存在！");
        }
        return postInfo;
    }

    /**
     * 根据帖子ID和登录用户ID获取收藏记录
     *
     * @param postId  帖子ID
     * @param loginId 登录用户ID
     * @return 收藏记录
     */
    private PostLikes getPostLikes(Long postId, long loginId) {
        // 验证该帖子是否已在收藏列表
        LambdaQueryWrapper<PostLikes> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostLikes::getUserId, loginId);
        queryWrapper.eq(PostLikes::getPostId, postId);
        return postLikesMapper.selectOne(queryWrapper);
    }

    @Transactional
    @Override
    public void addFavoritePost(Long postId) {
        // 验证帖子是否存在
        getPostExists(postId);

        // 获取当前登录用户id
        long loginId = StpUtil.getLoginIdAsLong();

        PostLikes postLikes = getPostLikes(postId, loginId);

        if (postLikes != null) {
            log.warn("用户'{}'重复收藏帖子'{}'", loginId, postId);
            throw new ThinkTankException("该帖子已在收藏列表中，无需重复收藏！");
        }

        postLikes = new PostLikes();
        postLikes.setPostId(postId);
        postLikes.setUserId(loginId);

        postLikesMapper.insert(postLikes);
    }


    @Override
    public Boolean isFavorite(Long postId) {
        // 验证帖子是否存在
        getPostExists(postId);

        // 获取当前登录用户id
        long loginId = StpUtil.getLoginIdAsLong();

        return getPostLikes(postId, loginId) != null;
    }

    @Override
    public void removeFavoritePost(Long postId) {
        // 验证帖子是否存在
        getPostExists(postId);

        // 获取当前登录用户id
        long loginId = StpUtil.getLoginIdAsLong();

        // 判断用户是否已收藏该帖子
        PostLikes postLikes = getPostLikes(postId, loginId);
        if (postLikes == null) {
            log.warn("用户'{}'取消收藏帖子'{}'失败，该帖子不存在或未收藏！", loginId, postId);
            throw new ThinkTankException("取消收藏失败，该帖子不存在或未收藏！");
        }

        // 删除收藏记录
        postLikesMapper.deleteById(postLikes.getId());
    }


    @Override
    public IPage<PostInfo> getFavoritePage(Long userId, Integer currentPage) {
        // 验证用户是否存在
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser == null) {
            log.warn("用户'{}'不存在", userId);
            throw new ThinkTankException("用户不存在！");
        }

        // 返回分页查询用户收藏的帖子
        int postsPerPage = 10; // 每页帖子数量
        Page<PostInfo> page = new Page<>(currentPage, postsPerPage);
        return postLikesMapper.getFavoritePage(page, userId);
    }

    @Override
    public List<PostHotVo> getHotPostByTop5() {
        // 先查缓存
        String namespace = "post:hot:top5";

        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        // 查询redis中是否存在数据，若存在直接返回
        String result = ops.get(namespace);
        if (result != null) {
            return RedisCacheUtil.getObjectByTypeReference(result, new TypeReference<List<PostHotVo>>() {
            });
        }

        // 为最新帖子分配锁
        RLock lock = redissonClient.getLock(namespace + ":lock");
        lock.lock();
        try {
            // 查询redis中是否存在数据，若存在直接返回
            result = ops.get(namespace);
            if (result != null) {
                return RedisCacheUtil.getObjectByTypeReference(result, new TypeReference<List<PostHotVo>>() {
                });
            }

            // 查询数据库
            List<PostHotVo> list = postScoreMapper.getHotPostByTop5();

            // 取top5写入缓存，用于首页显示
            redisTemplate.opsForValue().set(namespace, ObjectMapperUtil.toJSON(list));
            return list;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<PostHotVo> getHotPostByTop30() {
        // 先查缓存
        String namespace = "post:hot:top30";

        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        // 查询redis中是否存在数据，若存在直接返回
        String result = ops.get(namespace);
        if (result != null) {
            return RedisCacheUtil.getObjectByTypeReference(result, new TypeReference<List<PostHotVo>>() {
            });
        }

        // 为最新帖子分配锁
        RLock lock = redissonClient.getLock(namespace + ":lock");
        lock.lock();
        try {
            // 查询redis中是否存在数据，若存在直接返回
            result = ops.get(namespace);
            if (result != null) {
                return RedisCacheUtil.getObjectByTypeReference(result, new TypeReference<List<PostHotVo>>() {
                });
            }

            // 查询数据库
            List<PostHotVo> list = postScoreMapper.getHotPostByTop30();

            // 取top5写入缓存，用于首页显示
            redisTemplate.opsForValue().set(namespace, ObjectMapperUtil.toJSON(list));
            return list;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void addPostClickRecord(PostClickRecords postClickRecords) {
        // 获取登录用户ID
        long loginId = StpUtil.getLoginIdAsLong();
        postClickRecords.setUserId(loginId);

        // 获取CorrelationData对象
        CorrelationData correlationData = RabbitMQUtil.getCorrelationData();

        // 将PostClickRecord对象转换为Message对象
        Message message = RabbitMQUtil.transformMessage(postClickRecords);

        // 发送消息
        rabbitTemplate.convertAndSend(AddPostClickRecordsFanoutConfig.FANOUT_EXCHANGE, "", message, correlationData);
    }

    @Override
    public Boolean hasDeletePermission(Long id) {
        // 验证当帖子是否是当前登录用户发布的
        long loginId = StpUtil.getLoginIdAsLong();
        PostInfo postInfo = postInfoMapper.selectById(id);

        if (postInfo == null) {
            log.warn("帖子id'{}'不存在,操作用户id'{}'", id, loginId);
            throw new ThinkTankException("帖子id不存在");
        }

        if (postInfo.getUserId().equals(loginId)) {
            return true;
        }

        // 远程调用获取当前板块所有板主以及小板主信息
        R<BlockMasterListVo> result = blockClient.getAllBlockMasterById(postInfo.getBlockId());
        if (!result.getStatus().equals(200)) {
            throw new ThinkTankException(result.getMsg());
        }

        // 判断是否是板主
        if (result.getData().getMasterList().stream().anyMatch(item -> item.getId().equals(loginId))) {
            return true;
        }

        // 判断是否是小板主
        if (result.getData().getSmallMasterList().stream().anyMatch(item -> item.getId().equals(loginId))) {
            return true;
        }
        return false;
    }
}
