package com.thinktank.generator.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.thinktank.common.validationgroups.InsertValidation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.*;

/**
 * <p>
 *
 * </p>
 *
 * @author pippi
 * @since 2023-10-13
 */
@TableName("block_application_master")
@ApiModel(value = "BlockApplicationMaster对象", description = "")
public class BlockApplicationMaster implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("申请id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * Min + Max + Positive 结合使用确保板主角色id只能为102或103
     */
    @Min(groups = {InsertValidation.class}, value = 102L, message = "板主角色id最小不能低于102")
    @Max(groups = {InsertValidation.class}, value = 103L, message = "板主角色id最大不能高于103")
    @Positive
    @NotNull(groups = {InsertValidation.class}, message = "板主角色id不能为空")
    @ApiModelProperty("申请板主角色id（102:板主 103:小版主）")
    private Long roleId;

    @ApiModelProperty("申请用户id")
    private Long userId;

    @NotNull(groups = {InsertValidation.class}, message = "板块id不能为空")
    @ApiModelProperty("板块id")
    private Long blockId;

    @NotEmpty(groups = {InsertValidation.class}, message = "申请理由不能为空")
    @ApiModelProperty("申请理由")
    private String reason;

    @ApiModelProperty("申请时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("处理结果（0:待处理 1:已通过 2:驳回）")
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BlockApplicationMaster{" +
                "id = " + id +
                ", roleId = " + roleId +
                ", userId = " + userId +
                ", blockId = " + blockId +
                ", reason = " + reason +
                ", createTime = " + createTime +
                ", updateTime = " + updateTime +
                ", status = " + status +
                "}";
    }
}
