package com.thinktank.auth.controller;

import com.thinktank.auth.dto.RegisterOrUpdateSysUserDto;
import com.thinktank.auth.service.RegisterService;
import com.thinktank.common.utils.R;
import com.thinktank.common.validationgroups.InsertValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉09⽇ 14:23
 * @Description: 用户注册
 * @Version: 1.0
 */
@Api(tags = "注册接口")
@RestController
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @ApiOperation("用户邮箱密码注册")
    @PostMapping("register")
    public R<String> register(@RequestBody @Validated(InsertValidation.class) RegisterOrUpdateSysUserDto registerOrUpdateSysUserDto) {
        return registerService.register(registerOrUpdateSysUserDto);
    }
}
