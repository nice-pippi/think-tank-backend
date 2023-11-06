package com.thinktank.post.controller;

import com.thinktank.common.utils.R;
import com.thinktank.common.validationgroups.InsertValidation;
import com.thinktank.generator.entity.PostReports;
import com.thinktank.post.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 弘
 * @CreateTime: 2023年11⽉06⽇ 16:36
 * @Description: 举报接口
 * @Version: 1.0
 */
@Api(tags = "举报接口")
@RestController
@RequestMapping("report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @ApiOperation("举报帖子/评论")
    @PostMapping
    public R<String> report(@RequestBody @Validated(InsertValidation.class) PostReports postReports) {
        reportService.report(postReports);
        return R.success("已提交举报记录，请耐心等候处理结果。");
    }
}
