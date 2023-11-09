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
    IPage<PostReportsVo> page(PostReportsDto postReportsDto);

    void prohibit(Long id);

    void reject(Long id);
}
