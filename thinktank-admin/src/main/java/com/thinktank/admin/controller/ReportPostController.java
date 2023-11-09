package com.thinktank.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thinktank.admin.service.ReportPostService;
import com.thinktank.common.utils.R;
import com.thinktank.common.validationgroups.QueryValidation;
import com.thinktank.generator.dto.PostReportsDto;
import com.thinktank.generator.vo.PostReportsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 弘
 * @CreateTime: 2023年11⽉08⽇ 16:28
 * @Description: 帖子举报管理
 * @Version: 1.0
 */
@Api(tags = "帖子举报管理接口")
@RestController
@RequestMapping("reportPortManage")
public class ReportPostController {
    @Autowired
    private ReportPostService reportPostService;

    @ApiOperation("帖子举报记录分页查询")
    @PostMapping("page")
    public R<IPage<PostReportsVo>> page(@RequestBody @Validated(QueryValidation.class) PostReportsDto postReportsDto) {
        return R.success(reportPostService.page(postReportsDto));
    }

    @ApiOperation("禁言")
    @PutMapping("prohibit/{id}")
    public R<String> prohibit(@PathVariable("id") Long id) {
        reportPostService.prohibit(id);
        return R.success("已禁言");
    }

    @ApiOperation("驳回举报")
    @PutMapping("reject/{id}")
    public R<String> reject(@PathVariable("id") Long id) {
        reportPostService.reject(id);
        return R.success("已驳回该举报请求");
    }
}
