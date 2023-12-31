package com.thinktank.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thinktank.admin.service.ReportCommentService;
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
 * @Description: 帖子评论举报管理
 * @Version: 1.0
 */
@Api(tags = "帖子评论举报管理接口")
@RestController
@RequestMapping("reportCommentManage")
public class ReportCommentController {
    @Autowired
    private ReportCommentService reportCommentService;

    @ApiOperation("帖子评论举报记录分页查询")
    @PostMapping("page")
    public R<IPage<PostReportsVo>> page(@RequestBody @Validated(QueryValidation.class) PostReportsDto postReportsDto) {
        return R.success(reportCommentService.page(postReportsDto));
    }

    @ApiOperation("禁言")
    @PutMapping("/prohibit/{id}")
    public R<String> prohibit(@PathVariable("id") Long id) {
        reportCommentService.prohibit(id);
        return R.success("已禁言");
    }

    @ApiOperation("驳回举报")
    @PutMapping("/reject/{id}")
    public R<String> reject(@PathVariable("id") Long id) {
        reportCommentService.reject(id);
        return R.success("已驳回该举报请求");
    }

    @ApiOperation("删除帖子评论")
    @DeleteMapping("{id}")
    public R<String> delete(@PathVariable("id") Long id) {
        reportCommentService.delete(id);
        return R.success("已删除该帖子");
    }
}
