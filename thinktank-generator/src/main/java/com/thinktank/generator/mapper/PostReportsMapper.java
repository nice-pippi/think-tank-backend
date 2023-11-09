package com.thinktank.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thinktank.generator.entity.PostReports;
import com.thinktank.generator.vo.PostReportsVo;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author pippi
 * @since 2023-11-09
 */
public interface PostReportsMapper extends BaseMapper<PostReports> {
    /**
     * 帖子举报记录分页查询
     *
     * @param page
     * @return
     */
    IPage<PostReportsVo> getPostReportPage(Page<PostReports> page);
}
