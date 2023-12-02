package com.thinktank.post.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.generator.entity.PostComments;
import com.thinktank.generator.entity.PostInfo;
import com.thinktank.generator.entity.PostReports;
import com.thinktank.generator.mapper.PostCommentsMapper;
import com.thinktank.generator.mapper.PostInfoMapper;
import com.thinktank.generator.mapper.PostReportsMapper;
import com.thinktank.post.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: 弘
 * @CreateTime: 2023年11⽉06⽇ 17:17
 * @Description: 举报业务接口实现类
 * @Version: 1.0
 */
@Slf4j
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private PostInfoMapper postInfoMapper;

    @Autowired
    private PostCommentsMapper postCommentsMapper;

    @Autowired
    private PostReportsMapper postReportsMapper;

    @Transactional
    @Override
    public void report(PostReports postReports) {
        // 当前登录用户id
        long loginId = StpUtil.getLoginIdAsLong();

        // 验证当前帖子id是否存在
        PostInfo postInfo = postInfoMapper.selectById(postReports.getPostId());
        if (postInfo == null) {
            log.warn("举报失败，帖子'{}'不存在，操作用户'{}'", postReports.getPostId(), loginId);
            throw new ThinkTankException("当前帖子不存在！");
        }

        // 验证当前帖子所属板块与用户提交的板块id是否相同
        if (!postInfo.getBlockId().equals(postReports.getBlockId())) {
            log.warn("举报失败，帖子'{}'所属板块id与用户提交的板块id'{}'不相同，操作用户id'{}'", postReports.getPostId(), postReports.getBlockId(), loginId);
            throw new ThinkTankException("操作非法！");
        }

        // 若评论id不为空，则验证当前评论id合法性
        if (postReports.getCommentId() != null) {
            PostComments postComments = postCommentsMapper.selectById(postReports.getCommentId());
            // 验证当前评论id是否存在
            if (postComments == null) {
                log.warn("举报失败，评论'{}'不存在，操作用户'{}'", postReports.getPostId(), loginId);
                throw new ThinkTankException("当前评论不存在！");
            }
            // 验证当前评论id与帖子id是否匹配
            if (!postComments.getPostId().equals(postReports.getPostId())) {
                log.warn("举报失败，当前评论'{}'与帖子'{}'不匹配，操作用户'{}'", postReports.getCommentId(), postReports.getPostId(), loginId);
                throw new ThinkTankException("操作非法");
            }
        }

        // 验证是否重复举报
        LambdaQueryWrapper<PostReports> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostReports::getPostId, postReports.getPostId());
        queryWrapper.eq(PostReports::getUserId, loginId);
        queryWrapper.eq(postReports.getCommentId() == null, PostReports::getCommentId, 0);
        queryWrapper.eq(postReports.getCommentId() != null, PostReports::getCommentId, postReports.getCommentId());
        PostReports report = postReportsMapper.selectOne(queryWrapper);
        if (report != null) {
            log.warn("用户'{}'重复举报,举报记录id为'{}'", loginId, report.getId());
            throw new ThinkTankException("请勿重复举报！");
        }

        // 验证当前举报类似是否为'其它'，若不是，则将举报理由清空
        if (!postReports.getReportTypeId().equals(8)) {
            postReports.setReason(null);
        }

        // 将举报记录写入数据库
        postReports.setUserId(loginId);
        postReportsMapper.insert(postReports);
    }
}
