package com.thinktank.admin.controller;

import com.thinktank.admin.service.ManageBlockClassifyService;
import com.thinktank.common.utils.R;
import com.thinktank.generator.dto.BlockClassifyDto;
import com.thinktank.generator.entity.BlockBigType;
import com.thinktank.generator.entity.BlockSmallType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉16⽇ 13:54
 * @Description: 板块分类管理
 * @Version: 1.0
 */
@Api(tags = "板块分类管理")
@RestController
@RequestMapping("blockClassifyManage")
public class ManageBlockClassifyController {
    @Autowired
    private ManageBlockClassifyService manageBlockService;

    @ApiOperation("获取所有板块分类")
    @GetMapping("getBlockClassify")
    public R<List<BlockClassifyDto>> getBlockClassify() {
        return manageBlockService.getBlockClassify();
    }

    @ApiOperation("新增板块大分类")
    @PostMapping("addBlockBigType")
    public R<BlockBigType> addBlockBigType(@RequestBody BlockBigType blockBigType) {
        return R.success(manageBlockService.addBlockBigType(blockBigType));
    }

    @ApiOperation("新增板块小分类")
    @PostMapping("addBlockSmallType")
    public R<BlockSmallType> addBlockSmallType(@RequestBody BlockSmallType blockSmallType) {
        return R.success(manageBlockService.addBlockSmallType(blockSmallType));
    }

    @ApiOperation("删除板块大分类")
    @DeleteMapping("/deleteBlockBigType/{id}")
    public R<String> deleteBlockBigType(@PathVariable("id") Long id) {
        manageBlockService.deleteBlockBigType(id);
        return R.success("删除成功");
    }

    @ApiOperation("删除板块小分类")
    @DeleteMapping("/deleteBlockSmallType/{id}")
    public R<String> deleteBlockSmallType(@PathVariable("id") Long id) {
        manageBlockService.deleteBlockSmallType(id);
        return R.success("删除成功");
    }
}
