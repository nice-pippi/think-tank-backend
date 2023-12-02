package com.thinktank.post.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.common.utils.ObjectMapperUtil;
import com.thinktank.common.utils.R;
import com.thinktank.common.utils.RedisCacheUtil;
import com.thinktank.generator.dto.PostInfoDto;
import com.thinktank.generator.entity.*;
import com.thinktank.generator.mapper.*;
import com.thinktank.generator.vo.PostCommentsVo;
import com.thinktank.generator.vo.PostInfoVo;
import com.thinktank.post.config.AddPostDocFanoutConfig;
import com.thinktank.post.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
    private PostLikesMapper postLikesMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 验证板块是否存在，若存在则返回板块信息
     *
     * @param id
     * @return
     */
    private BlockInfo getBlockExists(Long id) {
        BlockInfo blockInfo = blockInfoMapper.selectById(id);
        if (blockInfo == null) {
            log.error("板块'{}'不存在", id);
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
            log.error("用户'{}'在'{}'被禁言，无法发布帖子", loginId, postInfoDto.getBlockId());
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
            String content = postInfoDto.getContent();
            for (Long acceptUserId : randomList) {
                MessagePrivate messagePrivate = new MessagePrivate();
                messagePrivate.setSendUserId(loginId);
                messagePrivate.setAcceptUserId(acceptUserId);
                messagePrivate.setContent(content);
                messagePrivateMapper.insert(messagePrivate);
            }
        }

        // 提交待处理帖子id到mq队列，由队列异步处理写入es文档操作
        // 1.全局唯一的消息ID，需要封装到CorrelationData中
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        // 2.添加callback
        correlationData.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error("消息发送异常, ID:{}, 原因{}", correlationData.getId(), throwable.getMessage());
            }

            @Override
            public void onSuccess(CorrelationData.Confirm confirm) {
                if (confirm.isAck()) {
                    // 3.1.ack，消息成功
                    log.debug("消息发送成功, ID:{}", correlationData.getId());
                } else {
                    // 3.2.nack，消息失败
                    log.error("消息发送失败, ID:{}, 原因{}", correlationData.getId(), confirm.getReason());
                }
            }
        });
        // 3.发送消息
        String json = ObjectMapperUtil.toJSON(postInfo);
        Message message = MessageBuilder.withBody(json.getBytes(StandardCharsets.UTF_8)).build(); // 消息内容
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
            log.error("帖子id:'{}'不存在", postId);
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
    }

    /**
     * 根据帖子id封装vo所需信息
     *
     * @param postInfo
     * @return
     */
    private PostInfoVo getPostInfo(PostInfo postInfo) {
        // 根据帖子id获取该帖子前五条评论
        List<PostCommentsVo> postComments = postCommentsMapper.getPostCommentsVo(postInfo.getId());

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
    public List<PostInfoVo> getLatestPosts() {
        // 先查缓存
        String namespace = "post:latest";

        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        // 查询redis中是否存在数据，若存在直接返回
        String result = ops.get(namespace);
        if (result != null) {
            return RedisCacheUtil.getObjectByTypeReference(result, new TypeReference<List<PostInfoVo>>() {
            });
        }

        // 为最新帖子分配锁
        RLock lock = redissonClient.getLock(namespace + ":lock");
        List<PostInfoVo> list;

        // 开启锁
        lock.lock();
        try {
            // 查询redis中是否存在数据，若存在直接返回
            result = ops.get(namespace);
            if (result != null) {
                return RedisCacheUtil.getObjectByTypeReference(result, new TypeReference<List<PostInfoVo>>() {
                });
            }

            // 查数据库
            LambdaQueryWrapper<PostInfo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.orderByDesc(PostInfo::getCreateTime);
            queryWrapper.last("limit 20");
            List<PostInfo> postInfoList = postInfoMapper.selectList(queryWrapper);
            list = postInfoList.stream().map(this::getPostInfo).collect(Collectors.toList());

            // 写入缓存
            ops.set(namespace, ObjectMapperUtil.toJSON(list));
        } finally {
            lock.unlock();
        }
        return list;
    }

    @Override
    public R<List<PostInfoVo>> page(PostInfoDto postInfoDto) {
        Page<PostInfo> page = new Page<>(postInfoDto.getCurrentPage(), postInfoDto.getSize());
        LambdaQueryWrapper<PostInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostInfo::getBlockId, postInfoDto.getBlockId());
        queryWrapper.orderByDesc(PostInfo::getCreateTime);
        Page<PostInfo> postInfoPage = postInfoMapper.selectPage(page, queryWrapper);

        // 转换成PostInfoVo
        List<PostInfoVo> list = postInfoPage.getRecords().stream().map(this::getPostInfo).collect(Collectors.toList());

        return R.success(list).add("total", postInfoPage.getTotal());
    }

    @Override
    public String getTitle(Long postId) {
        LambdaQueryWrapper<PostInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(PostInfo::getTitle);
        queryWrapper.eq(PostInfo::getId, postId);
        PostInfo postInfo = postInfoMapper.selectOne(queryWrapper);

        if (postInfo == null) {
            log.error("帖子'{}'不存在", postId);
            throw new ThinkTankException("当前帖子不存在！");
        }
        return postInfo.getTitle();
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
     * @param postId
     * @return
     */
    private PostInfo getPostExists(Long postId) {
        PostInfo postInfo = postInfoMapper.selectById(postId);

        if (postInfo == null) {
            log.error("帖子'{}'不存在", postId);
            throw new ThinkTankException("帖子不存在！");
        }
        return postInfo;
    }

    @Transactional
    @Override
    public void addLikePost(Long postId) {
        getPostExists(postId);

        // 获取当前登录用户id
        long loginId = StpUtil.getLoginIdAsLong();

        // 验证该帖子是否已在收藏列表
        LambdaQueryWrapper<PostLikes> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostLikes::getPostId, postId);
        queryWrapper.eq(PostLikes::getUserId, loginId);
        PostLikes postLikes = postLikesMapper.selectOne(queryWrapper);

        if (postLikes != null) {
            log.warn("用户'{}'重复收藏帖子'{}'", loginId, postId);
            throw new ThinkTankException("该帖子已在收藏列表中，无需重复收藏！");
        }

        postLikes = new PostLikes();
        postLikes.setPostId(postId);
        postLikes.setUserId(loginId);

        postLikesMapper.insert(postLikes);
    }

}
