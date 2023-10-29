package com.thinktank.user.controller;

import com.thinktank.common.utils.R;
import com.thinktank.common.validationgroups.UpdateValidation;
import com.thinktank.generator.dto.SysUserDto;
import com.thinktank.generator.entity.SysUser;
import com.thinktank.user.service.UserService;
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
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation("根据id查询用户信息")
    @GetMapping("{id}")
    public R<SysUser> getUserInfo(@PathVariable("id") Long id) {
        return R.success(userService.getUserInfo(id));
    }

    @ApiOperation("更改用户信息")
    @PutMapping("/")
    public R<SysUser> updateUser(@RequestBody @Validated(UpdateValidation.class) SysUserDto sysUserDto) {
        return R.success(userService.update(sysUserDto));
    }
}
