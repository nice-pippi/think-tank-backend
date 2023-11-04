package com.thinktank.post.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.common.utils.ObjectMapperUtil;
import com.thinktank.generator.entity.PostCommentLikes;
import com.thinktank.generator.entity.PostComments;
import com.thinktank.generator.entity.PostInfo;
import com.thinktank.generator.entity.SysUserRole;
import com.thinktank.generator.mapper.PostCommentLikesMapper;
import com.thinktank.generator.mapper.PostCommentsMapper;
import com.thinktank.generator.mapper.PostInfoMapper;
import com.thinktank.generator.mapper.SysUserRoleMapper;
import com.thinktank.generator.vo.PostCommentsVo;
import com.thinktank.post.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉23⽇ 18:32
 * @Description: 评论业务接口
 * @Version: 1.0
 */
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private PostCommentsMapper postCommentsMapper;

    @Autowired
    private PostInfoMapper postInfoMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private PostCommentLikesMapper postCommentLikesMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 统计当前评论点赞用户id集合
    private List<Long> getLikes(Long commentId) {
        String namespace = "post:comment:like";
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
        Object o = ops.get(namespace, commentId.toString());

        List<Long> userList;
        if (o == null) {
            LambdaQueryWrapper<PostCommentLikes> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PostCommentLikes::getCommentId, commentId);
            userList = postCommentLikesMapper.selectList(queryWrapper)
                    .stream().map(PostCommentLikes::getUserId).collect(Collectors.toList());
            ops.put(namespace, commentId.toString(), ObjectMapperUtil.toJSON(userList));
        } else {
            userList = ObjectMapperUtil.toObjectByTypeReference(o.toString(), new TypeReference<List<Long>>() {
            });
        }
        return userList;
    }

    // 递归获取当前评论下所有子评论
    private List<PostCommentsVo> getAllChildrenComment(Long id, boolean isLogin) {
        List<PostCommentsVo> childrenComment = postCommentsMapper.getAllChildrenComment(id);
        if (childrenComment.size() > 0) {
            for (PostCommentsVo postCommentsVo : childrenComment) {
                // 查询当前子评论的点赞信息
                List<Long> likes = getLikes(postCommentsVo.getId());
                postCommentsVo.setLikes(likes.size());  // 点赞数量
                postCommentsVo.setLikeFlag(isLogin && likes.contains(StpUtil.getLoginIdAsLong()));

                postCommentsVo.setReplies(getAllChildrenComment(postCommentsVo.getId(), isLogin));
            }
        }
        return childrenComment;
    }

    @Override
    public IPage<PostCommentsVo> page(Long postId, Integer currentPage) {
        Page<PostCommentsVo> page = new Page<>(currentPage, 15);

        IPage<PostCommentsVo> commentsVoIPage = postCommentsMapper.getPage(postId, page);

        // 获取当前用户登录状态
        boolean isLogin = StpUtil.isLogin();

        List<PostCommentsVo> collect = commentsVoIPage.getRecords().stream().map(item -> {
            // 查询当前评论的点赞信息
            List<Long> likes = getLikes(item.getId());
            item.setLikes(likes.size());  // 点赞数量
            item.setLikeFlag(isLogin && likes.contains(StpUtil.getLoginIdAsLong()));  // 点赞用户ID列表

            // 查询当前评论的所有子评论
            List<PostCommentsVo> allChildrenComment = getAllChildrenComment(item.getId(), isLogin);
            item.setReplies(allChildrenComment);
            return item;
        }).collect(Collectors.toList());

        commentsVoIPage.setRecords(collect);
        return commentsVoIPage;
    }

    // 评论合法性校验，若校验成功返回登录用户id
    private long validate(PostComments postComments) {
        // 验证当前帖子是否存在
        PostInfo postInfo = postInfoMapper.selectById(postComments.getPostId());
        if (postInfo == null) {
            log.error("帖子'{}'不存在", postComments.getPostId());
            throw new ThinkTankException("当前帖子不存在！");
        }

        // 获取登录用户id
        long loginId = StpUtil.getLoginIdAsLong();

        // 验证用户提交的表单板块id与该帖子所属的板块id是否匹配
        if (!postInfo.getBlockId().equals(postComments.getBlockId())) {
            log.error("当前帖子id'{}'不属于'{}'板块，操作用户id为'{}'", postComments.getPostId(), postComments.getBlockId(), loginId);
            throw new ThinkTankException("操作非法！");
        }

        // 验证当前用户在当前板块是否被禁言
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, loginId);
        queryWrapper.eq(SysUserRole::getRoleId, 104);
        queryWrapper.eq(SysUserRole::getBlockId, postComments.getBlockId());
        Long count = sysUserRoleMapper.selectCount(queryWrapper);
        if (count > 0) {
            log.error("当前板块'{}'下用户'{}'已被禁言，无法发表评论", postComments.getBlockId(), loginId);
            throw new ThinkTankException("您在当前板块下已被禁言，无法发表评论！！");
        }
        return loginId;
    }

    @Transactional
    @Override
    public void replyPost(PostComments postComments) {
        // 评论合法性校验，若校验成功返回登录用户id
        long loginId = validate(postComments);
        postComments.setUserId(loginId);
        postCommentsMapper.insert(postComments);
    }

    @Transactional
    @Override
    public PostComments replyComment(PostComments postComments) {
        long loginId = validate(postComments);

        // 验证父级评论是否存在
        PostComments parent = postCommentsMapper.selectById(postComments.getParentId());
        if (parent == null) {
            log.error("因该评论不存在，用户评论区回复失败，用户id:{},被评论id:{}", loginId, postComments.getParentId());
            throw new ThinkTankException("该评论不存在！");
        }

        // 写入数据库
        postComments.setUserId(loginId);
        postCommentsMapper.insert(postComments);

        postComments.setDelFlag(null);
        postComments.setTopicFlag(null);
        return postComments;
    }

    @Transactional
    @Override
    public void addLikeComment(PostCommentLikes postCommentLikes) {
        // 查询评论记录
        long loginId = StpUtil.getLoginIdAsLong();
        Long commentId = postCommentLikes.getCommentId();
        PostComments postComments = postCommentsMapper.selectById(commentId);

        if (postComments == null) {
            log.error("当前评论id'{}'不存在，操作用户'{}'", commentId, loginId);
            throw new ThinkTankException("评论不存在！");
        }

        // 获取该评论的点赞用户id列表
        List<Long> userLikes = getLikes(postCommentLikes.getCommentId());

        // 点赞记录唯一性验证
        if (userLikes.contains(loginId)) {
            log.error("用户重复点赞，用户id'{}'，评论id'{}'", loginId, commentId);
            throw new ThinkTankException("请勿重复点赞！");
        }

        // 追加当前用户id并更新
        userLikes.add(loginId);
        String namespace = "post:comment:like";
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
        ops.put(namespace, commentId.toString(), ObjectMapperUtil.toJSON(userLikes));
    }

    @Override
    public void removeLikeComment(PostCommentLikes postCommentLikes) {
        // 查询评论记录
        long loginId = StpUtil.getLoginIdAsLong();
        Long commentId = postCommentLikes.getCommentId();
        PostComments postComments = postCommentsMapper.selectById(commentId);

        if (postComments == null) {
            log.error("当前评论id'{}'不存在，操作用户'{}'", commentId, loginId);
            throw new ThinkTankException("评论不存在！");
        }

        // 获取该评论的点赞用户id列表
        List<Long> userLikes = getLikes(postCommentLikes.getCommentId());

        // 验证是否在点赞记录里
        if (!userLikes.contains(loginId)) {
            log.error("用户取消点赞失败，用户id'{}'，评论id'{}'", loginId, postCommentLikes.getCommentId());
            throw new ThinkTankException("取消点赞失败");
        }

        // 删除当前用户id并更新
        userLikes.remove(loginId);
        String namespace = "post:comment:like";
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
        ops.put(namespace, postCommentLikes.getCommentId().toString(), ObjectMapperUtil.toJSON(userLikes));
    }
}
