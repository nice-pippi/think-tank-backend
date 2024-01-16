package com.thinktank.generator.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.thinktank.common.validationgroups.InsertValidation;
import com.thinktank.common.validationgroups.QueryValidation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author pippi
 * @since 2024-01-15
 */
@TableName("post_info")
@ApiModel(value = "PostInfo对象", description = "")
public class PostInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("帖子id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @NotNull(groups = {InsertValidation.class, QueryValidation.class}, message = "板块id不能为空")
    @ApiModelProperty("所属板块id")
    private Long blockId;

    @NotEmpty(groups = {InsertValidation.class}, message = "帖子标题不能为空")
    @ApiModelProperty("帖子标题")
    private String title;

    @Size(groups = {InsertValidation.class}, max = 135, message = "标签长度非法")
    @ApiModelProperty("标签")
    private String tag;

    @ApiModelProperty("发帖时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("删除标志（0:未删除 1:已删除）")
    @TableLogic
    private Integer delFlag;

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

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "PostInfo{" +
                "id = " + id +
                ", userId = " + userId +
                ", blockId = " + blockId +
                ", title = " + title +
                ", tag = " + tag +
                ", createTime = " + createTime +
                ", delFlag = " + delFlag +
                "}";
    }
}
