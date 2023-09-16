package com.thinktank.auth.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.thinktank.auth.dto.SysUserDto;
import com.thinktank.auth.service.UserService;
import com.thinktank.common.utils.R;
import com.thinktank.common.validationgroups.QueryValidation;
import com.thinktank.common.validationgroups.UpdateValidation;
import com.thinktank.generator.entity.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉09⽇ 14:53
 * @Description: 用户信息管理
 * @Version: 1.0
 */
@Api(tags = "用户信息接口")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation("根据id查询用户信息")
    @GetMapping("{id}")
    public R<SysUser> getUserInfo(@PathVariable("id") Long id) {
        return userService.getUserInfo(id);
    }

    @ApiOperation("更改用户信息")
    @PostMapping
    public R<String> updateUser(@RequestBody SysUserDto sysUserDto) {
        return userService.update(sysUserDto);
    }
}
