package com.thinktank.search.controller;

import com.thinktank.common.utils.R;
import com.thinktank.common.validationgroups.QueryValidation;
import com.thinktank.search.doc.BlockInfoDoc;
import com.thinktank.search.doc.PostInfoDoc;
import com.thinktank.search.dto.BlockInfoDocDto;
import com.thinktank.search.dto.PostInfoDocDto;
import com.thinktank.search.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉18⽇ 20:06
 * @Description: 搜索服务接口
 * @Version: 1.0
 */
@Api(tags = "搜索服务接口")
@RestController
public class SearchController {
    @Autowired
    private SearchService searchService;

    @ApiOperation("搜索板块")
    @PostMapping("searchBlock")
    public R<List<BlockInfoDoc>> searchBlock(@RequestBody @Validated(QueryValidation.class) BlockInfoDocDto blockInfoDocDto) {
        return searchService.searchBlock(blockInfoDocDto);
    }

    @ApiOperation("搜索帖子")
    @PostMapping("searchPost")
    public R<List<PostInfoDoc>> searchPost(@RequestBody @Validated(QueryValidation.class) PostInfoDocDto postInfoDocDto) {
        return searchService.searchPost(postInfoDocDto);
    }
}
