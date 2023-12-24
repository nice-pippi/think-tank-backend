package com.thinktank.generator.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.thinktank.common.validationgroups.InsertValidation;
import com.thinktank.common.validationgroups.UpdateValidation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author pippi
 * @since 2023-09-11
 */
@TableName("sys_user_role")
@ApiModel(value = "SysUserRole对象", description = "")
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(groups = {UpdateValidation.class}, message = "用户id不能为空")
    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("角色id")
    private Long roleId;

    @NotNull(groups = {UpdateValidation.class}, message = "板块id不能为空")
    @ApiModelProperty("板块id")
    private Long blockId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    @Override
    public String toString() {
        return "SysUserRole{" +
            "userId = " + userId +
            ", roleId = " + roleId +
            ", blockId = " + blockId +
        "}";
    }
}
