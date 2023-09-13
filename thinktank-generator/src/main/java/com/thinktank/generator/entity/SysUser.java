package com.thinktank.generator.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.thinktank.common.validationgroups.InsertValidation;
import com.thinktank.common.validationgroups.QueryValidation;
import com.thinktank.common.validationgroups.UpdateValidation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author pippi
 * @since 2023-09-11
 */
@TableName("sys_user")
@ApiModel(value = "SysUser对象", description = "")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("头像")
    private String avatar;

    @NotEmpty(groups = {InsertValidation.class}, message = "用户名不能为空")
    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("账号")
    private String account;

    @Email(groups = {InsertValidation.class, UpdateValidation.class, QueryValidation.class}, message = "邮箱格式有误")
    @ApiModelProperty("邮箱")
    private String email;

    @NotEmpty(groups = {InsertValidation.class, QueryValidation.class}, message = "密码不能为空")
    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("性别")
    private Integer sex;

    @ApiModelProperty("个人签名")
    private String description;

    @ApiModelProperty("用户状态（0:正常 1:禁言）")
    private Integer status;

    @ApiModelProperty("注册时间")
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        return "SysUser{" +
                "id = " + id +
                ", avatar = " + avatar +
                ", username = " + username +
                ", account = " + account +
                ", email = " + email +
                ", password = " + password +
                ", sex = " + sex +
                ", description = " + description +
                ", status = " + status +
                ", createTime = " + createTime +
                ", updateTime = " + updateTime +
                ", delFlag = " + delFlag +
                "}";
    }
}
