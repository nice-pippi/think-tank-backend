package com.thinktank.generator.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.thinktank.common.validationgroups.InsertValidation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author pippi
 * @since 2023-10-21
 */
@TableName("post_score")
@ApiModel(value = "PostScore对象", description = "")
public class PostScore implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("帖子评分id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @NotNull(groups = {InsertValidation.class}, message = "板块id不能为空")
    @ApiModelProperty("板块id")
    private Long blockId;

    @NotNull(groups = {InsertValidation.class}, message = "帖子id不能为空")
    @ApiModelProperty("被评分的帖子id")
    private Long postId;

    @ApiModelProperty("用户id")
    private Long userId;

    @Min(groups = {InsertValidation.class}, value = 1, message = "评分分数不能低于1")
    @Max(groups = {InsertValidation.class}, value = 5, message = "评分分数不能高于5")
    @NotNull(groups = {InsertValidation.class}, message = "评分分数不能为空")
    @ApiModelProperty("评分分数")
    private Integer score;

    @ApiModelProperty("评分时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更改评分时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "PostScore{" +
                "id = " + id +
                ", blockId = " + blockId +
                ", postId = " + postId +
                ", userId = " + userId +
                ", score = " + score +
                ", createTime = " + createTime +
                ", updateTime = " + updateTime +
                "}";
    }
}
