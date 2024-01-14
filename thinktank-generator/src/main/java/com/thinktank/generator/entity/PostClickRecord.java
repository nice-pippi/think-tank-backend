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
 * @since 2024-01-14
 */
@TableName("post_click_record")
@ApiModel(value = "PostClickRecord对象", description = "")
public class PostClickRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("帖子点击记录id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @NotNull(groups = {InsertValidation.class}, message = "所属板块id不能为空")
    @ApiModelProperty("所属板块id")
    private Long blockId;

    @NotNull(groups = {InsertValidation.class}, message = "帖子id不能为空")
    @ApiModelProperty("帖子id")
    private Long postId;

    @ApiModelProperty("浏览用户id")
    private Long userId;

    @Min(groups = {InsertValidation.class}, value = 0, message = "当前页码最小不能低于0")
    @Max(groups = {InsertValidation.class}, value = 1, message = "当前页码最大不能高于1")
    @NotNull(groups = {InsertValidation.class}, message = "点击类型不能为空")
    @ApiModelProperty("点击类型（0:直接访问 1:搜索引擎）")
    private Integer clickType;

    @ApiModelProperty("帖子标题")
    private String title;

    @ApiModelProperty("点击帖子时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("最新点击帖子时间")
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

    public Integer getClickType() {
        return clickType;
    }

    public void setClickType(Integer clickType) {
        this.clickType = clickType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        return "PostClickRecord{" +
                "id = " + id +
                ", blockId = " + blockId +
                ", postId = " + postId +
                ", userId = " + userId +
                ", clickType = " + clickType +
                ", title = " + title +
                ", createTime = " + createTime +
                ", updateTime = " + updateTime +
                "}";
    }
}
