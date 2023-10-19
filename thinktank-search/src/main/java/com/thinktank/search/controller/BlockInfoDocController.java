package com.thinktank.search.controller;

import com.thinktank.common.utils.R;
import com.thinktank.search.doc.BlockInfoDoc;
import com.thinktank.search.service.BlockInfoDocService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉18⽇ 16:57
 * @Description: 板块信息文档管理接口
 * @Version: 1.0
 */

@Api(tags = "板块信息文档管理接口")
@RestController
@RequestMapping("block")
public class BlockInfoDocController {

    @Autowired
    private BlockInfoDocService blockInfoDocService;

    @ApiOperation("根据板块id新增板块信息文档")
    @PostMapping("{id}")
    public R<BlockInfoDoc> addBlockInfoDoc(@PathVariable("id") Long id) {
        return R.success(blockInfoDocService.addBlockInfoDoc(id));
    }
}
