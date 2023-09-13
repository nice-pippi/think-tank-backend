package com.thinktank.auth.controller;

import com.thinktank.auth.service.UserService;
import com.thinktank.common.utils.R;
import com.thinktank.generator.entity.SysUser;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("{id}")
    public R<SysUser> getUserInfo(@PathVariable("id") Long id) {
        return userService.getUserInfo(id);
    }
}
