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
@TableName("post_comments")
@ApiModel(value = "PostComments对象", description = "")
public class PostComments implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("帖子评论id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("板块id")
    private Long blockId;

    @ApiModelProperty("帖子id")
    private Long postId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("父评论id（该字段若为空则代表本身就是父评论）")
    private Long parentId;

    @ApiModelProperty("评论内容（富文本html格式）")
    private String content;

    @ApiModelProperty("评论时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("是否主题帖（0:不是 1:是）")
    private Integer topicFlag;

    @ApiModelProperty("删除标志（0:未删除 1:已删除）")
    private Integer deleteFlag;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Integer getTopicFlag() {
        return topicFlag;
    }

    public void setTopicFlag(Integer topicFlag) {
        this.topicFlag = topicFlag;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Override
    public String toString() {
        return "PostComments{" +
            "id = " + id +
            ", blockId = " + blockId +
            ", postId = " + postId +
            ", userId = " + userId +
            ", parentId = " + parentId +
            ", content = " + content +
            ", createTime = " + createTime +
            ", topicFlag = " + topicFlag +
            ", deleteFlag = " + deleteFlag +
        "}";
    }
}
