package com.thinktank.search.controller;

import com.thinktank.common.utils.R;
import com.thinktank.search.service.PostInfoDocService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉24⽇ 14:38
 * @Description: 帖子信息文档管理接口
 * @Version: 1.0
 */
@Api(tags = "帖子信息文档管理接口")
@RestController
@RequestMapping("post")
public class PostInfoDocController {
    @Autowired
    private PostInfoDocService postInfoDocService;

    @ApiOperation("删除帖子信息文档")
    @RequestMapping("{id}")
    public R<String> deletePostInfoDoc(@PathVariable Long id) {
        postInfoDocService.deletePostInfoDoc(id);
        return R.success("删除成功");
    }
}
