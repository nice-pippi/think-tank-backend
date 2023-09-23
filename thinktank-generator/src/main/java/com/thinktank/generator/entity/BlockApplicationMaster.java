package com.thinktank.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.thinktank.common.validationgroups.InsertValidation;
import com.thinktank.common.validationgroups.UpdateValidation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.*;

/**
 * <p>
 *
 * </p>
 *
 * @author pippi
 * @since 2023-09-23
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
    @Min(groups = {InsertValidation.class}, value = 102L,message = "板主角色id最小不能低于102")
    @Max(groups = {InsertValidation.class}, value = 103L,message = "板主角色id最大不能高于103")
    @Positive
    @NotNull(groups = {InsertValidation.class}, message = "板主角色id不能为空")
    @ApiModelProperty("申请板主角色id（102:板主 103:小版主）")
    private Long roleId;

    @ApiModelProperty("申请用户id")
    private Long userId;

    @NotNull(groups = {InsertValidation.class}, message = "小分类板块id不能为空")
    @ApiModelProperty("小分类板块id")
    private Long smallTypeId;

    @NotEmpty(groups = {InsertValidation.class}, message = "申请理由不能为空")
    @ApiModelProperty("申请理由")
    private String reason;

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

    public Long getSmallTypeId() {
        return smallTypeId;
    }

    public void setSmallTypeId(Long smallTypeId) {
        this.smallTypeId = smallTypeId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
                ", smallTypeId = " + smallTypeId +
                ", reason = " + reason +
                ", status = " + status +
                "}";
    }
}
