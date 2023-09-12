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
 * @since 2023-09-11
 */
@TableName("post_comments")
@ApiModel(value = "PostComments对象", description = "")
public class PostComments implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("帖子评论id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("帖子id")
    private Long postId;

    @ApiModelProperty("父评论id（该字段若为空则代表本身就是父评论）")
    private Long parentId;

    @ApiModelProperty("评论内容（富文本html格式）")
    private String content;

    @ApiModelProperty("评论时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "PostComments{" +
            "id = " + id +
            ", userId = " + userId +
            ", postId = " + postId +
            ", parentId = " + parentId +
            ", content = " + content +
            ", createTime = " + createTime +
        "}";
    }
}
