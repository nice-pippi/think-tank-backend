package com.thinktank.block.controller;

import com.thinktank.block.service.BlockService;
import com.thinktank.common.utils.R;
import com.thinktank.common.validationgroups.InsertValidation;
import com.thinktank.common.validationgroups.UpdateValidation;
import com.thinktank.generator.dto.BlockClassifyDto;
import com.thinktank.generator.entity.BlockApplicationBlock;
import com.thinktank.generator.entity.BlockBigType;
import com.thinktank.generator.entity.BlockInfo;
import com.thinktank.generator.entity.BlockSmallType;
import com.thinktank.generator.vo.BlockInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉22⽇ 14:46
 * @Description: 板块接口
 * @Version: 1.0
 */
@Api(tags = "板块管理接口")
@RestController
public class BlockController {
    @Autowired
    private BlockService blockService;

    @ApiOperation("获取所有板块分类")
    @GetMapping("getBlockClassify")
    public R<List<BlockClassifyDto>> getBlockClassify() {
        return R.success(blockService.getBlockClassify());
    }

    @ApiOperation("申请创建板块")
    @PostMapping("applicationBlock")
    public R<String> applicationBlock(@RequestBody @Validated(InsertValidation.class) BlockApplicationBlock blockApplicationBlock) {
        blockService.applicationBlock(blockApplicationBlock);
        return R.success("已提交板块创建申请，请耐心等待管理员审核处理。");
    }

    @ApiOperation("获取板块信息")
    @GetMapping("{id}")
    public R<BlockInfoVo> getBlockInfo(@PathVariable("id") Long id) {
        return R.success(blockService.getBlockInfo(id));
    }

    @ApiOperation("更改板块信息")
    @PostMapping
    public R<BlockInfoVo> updateInfo(@RequestBody @Validated(UpdateValidation.class) BlockInfo blockInfo) {
        return R.success(blockService.update(blockInfo));
    }

    @ApiOperation("获取板块大分类集合")
    @GetMapping("getBlockBigTypeList")
    public R<List<BlockBigType>> getBlockBigTypeList() {
        return R.success(blockService.getBlockBigTypeList());
    }

    @ApiOperation("获取板块小分类集合")
    @GetMapping("getBlockSmallTypeList")
    public R<List<BlockSmallType>> getBlockSmallTypeList() {
        return R.success(blockService.getBlockSmallTypeList());
    }

    @ApiOperation("根据板块小分类ID查询所有板块")
    @GetMapping("/getAllBlockBySmallTypeId/{smallTypeId}")
    public R<List<BlockInfo>> getAllBlockBySmallTypeId(@PathVariable("smallTypeId") Long smallTypeId) {
        return R.success(blockService.getAllBlockBySmallTypeId(smallTypeId));
    }

    @ApiOperation("获取10个热门板块")
    @GetMapping("getHotBlock")
    public R<List<BlockInfo>> getHotBlock() {
        return R.success(blockService.getHotBlock());
    }
}
