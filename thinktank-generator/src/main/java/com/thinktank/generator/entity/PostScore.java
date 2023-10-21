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
@TableName("post_score")
@ApiModel(value = "PostScore对象", description = "")
public class PostScore implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("帖子评分id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("板块id")
    private Long blockId;

    @ApiModelProperty("被评分的帖子id")
    private Long postId;

    @ApiModelProperty("用户id")
    private Long userId;

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
