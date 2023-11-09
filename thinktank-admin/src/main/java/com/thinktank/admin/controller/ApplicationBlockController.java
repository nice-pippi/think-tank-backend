package com.thinktank.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thinktank.admin.service.ApplicationBlockManageService;
import com.thinktank.common.utils.R;
import com.thinktank.common.validationgroups.QueryValidation;
import com.thinktank.generator.dto.BlockApplicationBlockDto;
import com.thinktank.generator.vo.BlockApplicationBlockVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉27⽇ 16:23
 * @Description: 申请创建板块管理
 * @Version: 1.0
 */
@Api(tags = "申请创建板块管理接口")
@RestController
@RequestMapping("applicationBlockManage")
public class ApplicationBlockController {
    @Autowired
    private ApplicationBlockManageService applicationBlockManageService;

    @ApiOperation("板块创建申请记录分页查询")
    @PostMapping("page")
    public R<IPage<BlockApplicationBlockVo>> page(@RequestBody @Validated(QueryValidation.class) BlockApplicationBlockDto blockApplicationBlockDto) {
        IPage<BlockApplicationBlockVo> page = applicationBlockManageService.page(blockApplicationBlockDto);
        return R.success(page);
    }

    @ApiOperation("通过板块创建申请")
    @PutMapping("allow/{id}")
    public R<String> allow(@PathVariable("id") Long id) {
        applicationBlockManageService.allow(id);
        return R.success("已通过该申请");
    }

    @ApiOperation("驳回板块创建申请")
    @PutMapping("reject/{id}")
    public R<String> reject(@PathVariable("id") Long id) {
        applicationBlockManageService.reject(id);
        return R.success("已驳回该申请");
    }

    @ApiOperation("删除板块创建申请记录")
    @DeleteMapping("{id}")
    public R<String> delete(@PathVariable("id") Long id) {
        applicationBlockManageService.delete(id);
        return R.success("已删除该申请记录");
    }
}
