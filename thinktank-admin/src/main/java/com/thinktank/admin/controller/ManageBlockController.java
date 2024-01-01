package com.thinktank.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thinktank.admin.service.ManageBlockService;
import com.thinktank.common.utils.R;
import com.thinktank.common.validationgroups.InsertValidation;
import com.thinktank.common.validationgroups.QueryValidation;
import com.thinktank.generator.dto.BlockInfoDto;
import com.thinktank.generator.entity.BlockInfo;
import com.thinktank.generator.vo.BlockInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉18⽇ 17:39
 * @Description: 板块管理
 * @Version: 1.0
 */
@Api(tags = "板块管理")
@RestController
@RequestMapping("blockManage")
public class ManageBlockController {
    @Autowired
    private ManageBlockService manageBlockService;

    @ApiOperation("查询所有板块")
    @PostMapping("page")
    public R<IPage<BlockInfoVo>> page(@RequestBody @Validated(QueryValidation.class) BlockInfoDto blockInfoDto) {
        return R.success(manageBlockService.page(blockInfoDto));
    }

    @ApiOperation("新增板块")
    @PostMapping
    public R<String> addBlock(@RequestBody @Validated(InsertValidation.class) BlockInfo blockInfo) {
        manageBlockService.addBlock(blockInfo);
        return R.success("新增板块成功");
    }

    @ApiOperation("删除板块")
    @DeleteMapping("{id}")
    public R<String> deleteBlock(@PathVariable("id") Long id) {
        manageBlockService.deleteBlock(id);
        return R.success("删除板块成功");
    }
}
