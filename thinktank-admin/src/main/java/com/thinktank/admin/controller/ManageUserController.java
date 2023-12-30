package com.thinktank.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thinktank.admin.service.ManageUserService;
import com.thinktank.common.utils.R;
import com.thinktank.common.validationgroups.UpdateValidation;
import com.thinktank.generator.dto.SysUserDto;
import com.thinktank.generator.entity.SysUser;
import com.thinktank.generator.entity.SysUserRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉23⽇ 18:50
 * @Description: 用户管理
 * @Version: 1.0
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("userManage")
public class ManageUserController {
    @Autowired
    private ManageUserService manageUserService;

    @ApiOperation("分页查询用户")
    @PostMapping("page")
    public R<IPage<SysUser>> page(@RequestBody SysUserDto sysUserDto) {
        return R.success(manageUserService.page(sysUserDto));
    }

    @ApiOperation("修改用户密码")
    @PutMapping("updatePassword")
    public R<String> updatePassword(@RequestBody SysUser sysUser) {
        manageUserService.updatePassword(sysUser);
        return R.success("更改密码成功");
    }

    @ApiOperation("修改用户状态")
    @PutMapping("updateStatus")
    public R<String> updateStatus(@RequestBody SysUser sysUser) {
        manageUserService.updateStatus(sysUser);
        return R.success("更改用户状态成功");
    }

    @ApiOperation("禁言用户")
    @PutMapping("prohibit")
    public R<String> prohibit(@RequestBody @Validated(UpdateValidation.class) SysUserRole sysUserRole) {
        System.out.println(sysUserRole);
        manageUserService.prohibit(sysUserRole);
        return R.success("禁言成功");
    }
}
