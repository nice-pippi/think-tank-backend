package com.thinktank.post.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thinktank.common.utils.R;
import com.thinktank.common.validationgroups.DeleteValidation;
import com.thinktank.common.validationgroups.InsertValidation;
import com.thinktank.generator.entity.PostCommentLikes;
import com.thinktank.generator.entity.PostComments;
import com.thinktank.generator.vo.PostCommentsVo;
import com.thinktank.post.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉23⽇ 18:19
 * @Description: 评论接口
 * @Version: 1.0
 */
@Api(tags = "评论接口")
@RestController
@RequestMapping("comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @ApiOperation("帖子评论分页")
    @GetMapping("/page/{postId}/{currentPage}")
    public R<IPage<PostCommentsVo>> page(@PathVariable("postId") Long postId, @PathVariable("currentPage") Integer currentPage) {
        return R.success(commentService.page(postId, currentPage));
    }

    @ApiOperation("对帖子发表评论")
    @PostMapping("replyPost")
    public R<String> replyPost(@RequestBody @Validated(InsertValidation.class) PostComments postComments) {
        commentService.replyPost(postComments);
        return R.success("评论已发布");
    }

    @ApiOperation("回复评论")
    @PostMapping("replyComment")
    public R<PostComments> replyComment(@RequestBody @Validated(InsertValidation.class) PostComments postComments) {
        return R.success(commentService.replyComment(postComments));
    }

    @ApiOperation("评论点赞")
    @PostMapping("addLikeComment")
    public R<String> addLikeComment(@RequestBody @Validated(InsertValidation.class) PostCommentLikes postCommentLikes) {
        commentService.addLikeComment(postCommentLikes);
        return R.success("点赞成功~");
    }

    @ApiOperation("取消评论点赞")
    @PostMapping("removeLikeComment")
    public R<String> removeLikeComment(@RequestBody @Validated(DeleteValidation.class) PostCommentLikes postCommentLikes) {
        commentService.removeLikeComment(postCommentLikes);
        return R.success("已取消点赞");
    }
}
