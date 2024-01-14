package com.thinktank.post.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thinktank.common.utils.R;
import com.thinktank.common.validationgroups.InsertValidation;
import com.thinktank.common.validationgroups.QueryValidation;
import com.thinktank.generator.dto.PostInfoDto;
import com.thinktank.generator.entity.PostClickRecords;
import com.thinktank.generator.entity.PostInfo;
import com.thinktank.generator.vo.PostHotVo;
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
    public R<String> delete(@PathVariable("postId") Long postId) {
        postService.delete(postId);
        return R.success("删除成功");
    }

    @ApiOperation("首页大厅帖子推荐")
    @GetMapping("getIndexPosts")
    public R<List<PostInfoVo>> getIndexPosts() {
        return R.success(postService.getLatestPosts());
    }

    @ApiOperation(("当前板块下帖子分页查询"))
    @PostMapping("page")
    public R<List<PostInfoVo>> page(@RequestBody @Validated(QueryValidation.class) PostInfoDto postInfoDto) {
        return postService.page(postInfoDto);
    }

    @ApiOperation("根据帖子id获取帖子标题")
    @GetMapping("/getTitle/{postId}")
    public R<String> getTitle(@PathVariable("postId") Long postId) {
        return R.success(postService.getTitle(postId));
    }

    @ApiOperation("根据用户id获取已发布的帖子")
    @GetMapping("/getPageByPublishedPosts/{id}/{currentPage}")
    public R<List<PostInfoVo>> getPageByPublishedPosts(@PathVariable("id") Long id, @PathVariable("currentPage") Integer currentPage) {
        return postService.getPageByPublishedPosts(id, currentPage);
    }

    @ApiOperation("收藏帖子")
    @PostMapping("/addFavorite/{postId}")
    public R<String> addFavoritePost(@PathVariable("postId") Long postId) {
        postService.addFavoritePost(postId);
        return R.success("已收藏");
    }

    @ApiOperation("取消收藏帖子")
    @PostMapping("/removeFavorite/{postId}")
    public R<String> removeFavoritePost(@PathVariable("postId") Long postId) {
        postService.removeFavoritePost(postId);
        return R.success("已取消收藏");
    }

    @ApiOperation("验证当前登录用户是否收藏了当前帖子")
    @GetMapping("/isFavorite/{postId}")
    public R<Boolean> isFavorite(@PathVariable("postId") Long postId) {
        return R.success(postService.isFavorite(postId));
    }

    @ApiOperation("分页查询用户收藏的帖子")
    @GetMapping("/getFavoritePage/{userId}/{currentPage}")
    public R<IPage<PostInfo>> getFavoritePage(@PathVariable("userId") Long userId, @PathVariable("currentPage") Integer currentPage) {
        return R.success(postService.getFavoritePage(userId, currentPage));
    }

    @ApiOperation("获取热门帖子top5")
    @GetMapping("getHotPostByTop5")
    public R<List<PostHotVo>> getHotPostByTop5() {
        return R.success(postService.getHotPostByTop5());
    }

    @ApiOperation("获取热门帖子top30")
    @GetMapping("getHotPostByTop30")
    public R<List<PostHotVo>> getHotPostByTop30() {
        return R.success(postService.getHotPostByTop30());
    }

    @ApiOperation("新增点击帖子记录")
    @PostMapping("addPostClickRecord")
    public R<String> addPostClickRecord(@RequestBody @Validated(InsertValidation.class) PostClickRecords postClickRecords) {
        postService.addPostClickRecord(postClickRecords);
        return R.success("新增成功");
    }
}


