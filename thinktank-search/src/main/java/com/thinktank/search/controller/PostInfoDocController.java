package com.thinktank.search.controller;

import com.thinktank.common.utils.R;
import com.thinktank.search.doc.PostInfoDoc;
import com.thinktank.search.service.PostInfoDocService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉21⽇ 19:20
 * @Description: 帖子信息文档管理接口
 * @Version: 1.0
 */
@Api(tags = "帖子信息文档管理接口")
@RestController
@RequestMapping("post")
public class PostInfoDocController {
    @Autowired
    private PostInfoDocService postInfoService;

    @ApiOperation("根据帖子信息新增或更改板块信息文档")
    @PostMapping("{id}")
    public R<PostInfoDoc> addBPostInfoDoc(@PathVariable("id") Long id) {
        return R.success(postInfoService.addPostInfoDoc(id));
    }
}
