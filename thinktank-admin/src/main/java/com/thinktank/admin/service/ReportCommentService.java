package com.thinktank.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thinktank.generator.dto.PostReportsDto;
import com.thinktank.generator.vo.PostReportsVo;

/**
 * @Author: 弘
 * @CreateTime: 2023年11⽉10⽇ 15:22
 * @Description: 评论举报业务接口
 * @Version: 1.0
 */
public interface ReportCommentService {
    /**
     * 帖子评论举报记录分页查询
     *
     * @param postReportsDto
     * @return
     */
    IPage<PostReportsVo> page(PostReportsDto postReportsDto);

    /**
     * 禁言
     *
     * @param id
     */
    void prohibit(Long id);

    /**
     * 驳回举报
     *
     * @param id
     */
    void reject(Long id);

    /**
     * 删除帖子评论
     *
     * @param id
     */
    void delete(Long id);
}
