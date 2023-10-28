package com.thinktank.post.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.generator.entity.PostComments;
import com.thinktank.generator.entity.PostInfo;
import com.thinktank.generator.entity.SysUserRole;
import com.thinktank.generator.mapper.PostCommentsMapper;
import com.thinktank.generator.mapper.PostInfoMapper;
import com.thinktank.generator.mapper.SysUserRoleMapper;
import com.thinktank.generator.vo.PostCommentsVo;
import com.thinktank.post.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    // 递归获取当前评论下所有子评论
    private List<PostCommentsVo> getAllChildrenComment(Long id) {
        List<PostCommentsVo> childrenComment = postCommentsMapper.getAllChildrenComment(id);
        if (childrenComment.size() > 0) {
            for (PostCommentsVo postCommentsVo : childrenComment) {
                postCommentsVo.setReplies(postCommentsMapper.getAllChildrenComment(postCommentsVo.getId()));
            }
        }
        return childrenComment;
    }

    @Override
    public IPage<PostCommentsVo> page(Long postId, Integer currentPage) {
        Page<PostCommentsVo> page = new Page<>(currentPage, 15);

        IPage<PostCommentsVo> commentsVoIPage = postCommentsMapper.getPage(postId, page);
        List<PostCommentsVo> collect = commentsVoIPage.getRecords().stream().map(item -> {
            // 查询当前评论的所有子评论
            List<PostCommentsVo> allChildrenComment = getAllChildrenComment(item.getId());
            item.setReplies(allChildrenComment);
            return item;
        }).collect(Collectors.toList());

        commentsVoIPage.setRecords(collect);
        return commentsVoIPage;
    }

    @Transactional
    @Override
    public void replyPost(PostComments postComments) {
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

        postComments.setUserId(loginId);
        postCommentsMapper.insert(postComments);
    }


}
