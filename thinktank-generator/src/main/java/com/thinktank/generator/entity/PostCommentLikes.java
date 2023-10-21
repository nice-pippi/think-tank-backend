package com.thinktank.generator.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author pippi
 * @since 2023-10-21
 */
@TableName("post_comment_likes")
@ApiModel(value = "PostCommentLikes对象", description = "")
public class PostCommentLikes implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("评论点赞id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("板块id")
    private Long blockId;

    @ApiModelProperty("帖子id")
    private Long postId;

    @ApiModelProperty("被点赞的评论id")
    private Long commentId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("点赞时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "PostCommentLikes{" +
            "id = " + id +
            ", blockId = " + blockId +
            ", postId = " + postId +
            ", commentId = " + commentId +
            ", userId = " + userId +
            ", createTime = " + createTime +
        "}";
    }
}
