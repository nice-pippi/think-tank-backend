package com.thinktank.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thinktank.admin.service.BlockManageService;
import com.thinktank.common.utils.R;
import com.thinktank.generator.dto.BlockApplicationBlockDto;
import com.thinktank.generator.vo.BlockApplicationBlockVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉27⽇ 16:23
 * @Description: 板块管理
 * @Version: 1.0
 */
@Api(tags = "板块管理接口")
@RestController
@RequestMapping("/block")
public class BlockManageController {
    @Autowired
    private BlockManageService blockManageService;

    @ApiOperation("创建板块申请的分页查询")
    @PostMapping("applicationBlockPage")
    public R<IPage<BlockApplicationBlockVo>> getApplicationBlockPage(@RequestBody BlockApplicationBlockDto blockApplicationBlockDto) {
        IPage<BlockApplicationBlockVo> page = blockManageService.getApplicationBlockPage(blockApplicationBlockDto);
        return R.success(page);
    }
}
