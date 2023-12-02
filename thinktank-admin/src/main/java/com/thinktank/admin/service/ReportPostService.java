package com.thinktank.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thinktank.generator.dto.PostReportsDto;
import com.thinktank.generator.vo.PostReportsVo;

/**
 * @Author: 弘
 * @CreateTime: 2023年11⽉08⽇ 16:59
 * @Description: 帖子举报业务接口
 * @Version: 1.0
 */
public interface ReportPostService {
    /**
     * 帖子举报记录分页查询
     *
     * @param postReportsDto 传入的帖子举报记录查询条件
     * @return 返回分页后的帖子举报记录数据集合
     */
    IPage<PostReportsVo> page(PostReportsDto postReportsDto);

    /**
     * 禁言
     *
     * @param id 举报记录ID
     */
    void prohibit(Long id);

    /**
     * 驳回举报
     *
     * @param id 举报记录ID
     */
    void reject(Long id);

    /**
     * 删除帖子
     *
     * @param id 举报记录ID
     */
    void delete(Long id);

}
