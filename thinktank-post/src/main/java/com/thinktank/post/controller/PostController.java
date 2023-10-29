package com.thinktank.post.controller;

import com.thinktank.common.utils.R;
import com.thinktank.common.validationgroups.InsertValidation;
import com.thinktank.common.validationgroups.QueryValidation;
import com.thinktank.generator.dto.PostInfoDto;
import com.thinktank.generator.vo.PostInfoVo;
import com.thinktank.post.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉20⽇ 19:04
 * @Description: 帖子接口
 * @Version: 1.0
 */
@Api(tags = "帖子接口")
@RestController
@RequestMapping("postAction")
public class PostController {
    @Autowired
    private PostService postService;

    @ApiOperation("发布帖子")
    @PostMapping
    public R<String> publish(@RequestBody @Validated(InsertValidation.class) PostInfoDto postInfoDto) {
        postService.publish(postInfoDto);
        return R.success("发布成功");
    }

    @ApiOperation("删除帖子")
    @DeleteMapping("{postId}")
    public R<String> delete(@PathVariable("id") Long postId) {
        postService.delete(postId);
        return R.success("删除成功");
    }

    @ApiOperation("首页大厅获取最新帖子")
    @GetMapping("getLatestPosts")
    public R<List<PostInfoVo>> getLatestPosts() {
        return R.success(postService.getLatestPosts());
    }

    @ApiOperation(("根据板块id获取当前板块下帖子分页"))
    @PostMapping("/page")
    public R<List<PostInfoVo>> page(@RequestBody @Validated(QueryValidation.class) PostInfoDto postInfoDto) {
        return R.success(postService.page(postInfoDto));
    }

    @ApiOperation("根据帖子id获取帖子标题")
    @GetMapping("/getTitle/{postId}")
    public R<String> getTitle(@PathVariable("postId") Long postId) {
        return R.success(postService.getTitle(postId));
    }

}


