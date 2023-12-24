package com.thinktank.admin.controller;

import com.thinktank.admin.service.ManageUserService;
import com.thinktank.common.utils.R;
import com.thinktank.common.validationgroups.UpdateValidation;
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

    @ApiOperation("禁言用户")
    @PutMapping("prohibit")
    public R<String> prohibit(@RequestBody @Validated(UpdateValidation.class) SysUserRole sysUserRole) {
        System.out.println(sysUserRole);
        manageUserService.prohibit(sysUserRole);
        return R.success("禁言成功");
    }
}
