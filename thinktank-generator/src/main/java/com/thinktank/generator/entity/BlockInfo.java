package com.thinktank.generator.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.thinktank.common.validationgroups.InsertValidation;
import com.thinktank.common.validationgroups.UpdateValidation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author pippi
 * @since 2023-10-07
 */
@TableName("block_info")
@ApiModel(value = "BlockInfo对象", description = "")
public class BlockInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(groups = { UpdateValidation.class}, message = "板块id不能为空")
    @ApiModelProperty("板块id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("大分类板块id")
    private Long bigTypeId;

    @ApiModelProperty("小分类板块id")
    private Long smallTypeId;

    @ApiModelProperty("板块头像")
    private String avatar;

    @NotEmpty(groups = {InsertValidation.class, UpdateValidation.class}, message = "板块名称不能为空")
    @ApiModelProperty("板块名称")
    private String blockName;

    @NotEmpty(groups = {InsertValidation.class, UpdateValidation.class}, message = "板块介绍不能为空")
    @ApiModelProperty("板块介绍")
    private String description;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("删除标志（0:未删除 1:已删除）")
    @TableLogic
    private Integer delFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBigTypeId() {
        return bigTypeId;
    }

    public void setBigTypeId(Long bigTypeId) {
        this.bigTypeId = bigTypeId;
    }

    public Long getSmallTypeId() {
        return smallTypeId;
    }

    public void setSmallTypeId(Long smallTypeId) {
        this.smallTypeId = smallTypeId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "BlockInfo{" +
                "id = " + id +
                ", bigTypeId = " + bigTypeId +
                ", smallTypeId = " + smallTypeId +
                ", avatar = " + avatar +
                ", blockName = " + blockName +
                ", description = " + description +
                ", createTime = " + createTime +
                ", updateTime = " + updateTime +
                ", delFlag = " + delFlag +
                "}";
    }
}
