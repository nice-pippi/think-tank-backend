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
 * @since 2024-01-19
 */
@TableName("sys_login_records")
@ApiModel(value = "SysLoginRecords对象", description = "")
public class SysLoginRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("登录记录id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("登录类型（0:密码登录 1:微信登录）")
    private Integer loginType;

    @ApiModelProperty("用户状态（0:登录失败 1:登录成功）")
    private Integer status;

    @ApiModelProperty("登录失败原因")
    private String reason;

    @ApiModelProperty("登录时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

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

    public Integer getLoginType() {
        return loginType;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "SysLoginRecords{" +
            "id = " + id +
            ", userId = " + userId +
            ", loginType = " + loginType +
            ", status = " + status +
            ", reason = " + reason +
            ", createTime = " + createTime +
        "}";
    }
}
