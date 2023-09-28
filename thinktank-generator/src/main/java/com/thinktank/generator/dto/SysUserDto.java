package com.thinktank.generator.dto;

import com.thinktank.common.validationgroups.InsertValidation;
import com.thinktank.generator.entity.SysUser;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉09⽇ 18:39
 * @Description: 类描述
 * @Version: 1.0
 */
@Data
public class SysUserDto extends SysUser {
    @NotEmpty(groups = {InsertValidation.class}, message = "验证码不能为空")
    private String validateCode;
}
