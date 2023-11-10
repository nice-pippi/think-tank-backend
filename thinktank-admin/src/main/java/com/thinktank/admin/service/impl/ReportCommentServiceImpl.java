package com.thinktank.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thinktank.admin.service.ReportCommentService;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.generator.dto.PostReportsDto;
import com.thinktank.generator.entity.PostComments;
import com.thinktank.generator.entity.PostReports;
import com.thinktank.generator.entity.SysUserRole;
import com.thinktank.generator.mapper.PostCommentsMapper;
import com.thinktank.generator.mapper.PostReportsMapper;
import com.thinktank.generator.mapper.SysUserRoleMapper;
import com.thinktank.generator.vo.PostReportsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: 弘
 * @CreateTime: 2023年11⽉10⽇ 15:22
 * @Description: 评论举报业务接口实现类
 * @Version: 1.0
 */
@Slf4j
@Service
public class ReportCommentServiceImpl implements ReportCommentService {
    @Autowired
    private PostReportsMapper postReportsMapper;

    @Autowired
    private PostCommentsMapper postCommentsMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public IPage<PostReportsVo> page(PostReportsDto postReportsDto) {
        Page<PostReports> page = new Page<>(postReportsDto.getCurrentPage(), postReportsDto.getSize());
        return postReportsMapper.getPostCommentReportPage(page);
    }

    // 举报记录验证
    private PostComments validate(Long id) {
        // 验证该记录是否存在
        PostReports postReports = postReportsMapper.selectById(id);
        if (postReports == null) {
            log.error("举报记录'{}'不存在", id);
            throw new ThinkTankException("该记录不存在！");
        }

        // 验证该记录是否处理过
        if (!postReports.getStatus().equals(0)) {
            log.error("记录'{}'已处理", id);
            throw new ThinkTankException("该记录已处理过！");
        }

        // 帖子评论id
        Long commentId = postReports.getCommentId();

        // 查询该帖子评论记录
        PostComments postComments = postCommentsMapper.selectById(commentId);
        if (postComments == null) {
            log.error("帖子评论'{}'不存在，可能已被删除", commentId);
            throw new ThinkTankException("该帖子评论不存在，可能已被删除。");
        }
        return postComments;
    }

    @Transactional
    @Override
    public void prohibit(Long id) {
        PostComments postComments = validate(id);

        // 被禁言用户id
        Long userId = postComments.getUserId();
        // 该举报记录板块id
        Long blockId = postComments.getBlockId();

        // 查询该用户所在版块身份
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, userId);
        queryWrapper.eq(SysUserRole::getBlockId, blockId);
        SysUserRole sysUserRole = sysUserRoleMapper.selectOne(queryWrapper);

        // 若为空则分配禁言角色
        if (sysUserRole == null) {
            sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(104L);
            sysUserRole.setBlockId(blockId);
            sysUserRoleMapper.insert(sysUserRole);
        } else {
            // 若不为空则将角色更改为禁言角色
            sysUserRole.setRoleId(104L);
            sysUserRoleMapper.update(sysUserRole, queryWrapper);
        }

        // 删除帖子评论
        postCommentsMapper.deleteById(postComments.getId());

        // 更新处理记录
        PostReports postReports = new PostReports();
        postReports.setId(id);
        postReports.setStatus(1);
        postReportsMapper.updateById(postReports);
    }

    @Override
    public void reject(Long id) {
        validate(id);

        // 更新处理记录
        PostReports postReports = new PostReports();
        postReports.setId(id);
        postReports.setStatus(2);
        postReportsMapper.updateById(postReports);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        PostComments postComments = validate(id);

        // 删除帖子评论
        postCommentsMapper.deleteById(postComments.getId());

        // 更新处理记录
        PostReports postReports = new PostReports();
        postReports.setId(id);
        postReports.setStatus(1);
        postReportsMapper.updateById(postReports);
    }
}
