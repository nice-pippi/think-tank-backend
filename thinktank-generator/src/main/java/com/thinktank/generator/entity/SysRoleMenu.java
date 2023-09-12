package com.thinktank.generator.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("sys_role_menu")
@ApiModel(value = "SysRoleMenu对象", description = "")
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long roleId;

    private Long menuId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "SysRoleMenu{" +
            "roleId = " + roleId +
            ", menuId = " + menuId +
        "}";
    }
}
