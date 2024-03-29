package com.thinktank.auth.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.thinktank.auth.service.LoginService;
import com.thinktank.common.utils.R;
import com.thinktank.common.validationgroups.QueryValidation;
import com.thinktank.generator.entity.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉05⽇ 16:15
 * @Description: 登录状态管理
 * @Version: 1.0
 */
@Api(tags = "登录状态管理接口")
@RestController
@RequestMapping("login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @ApiOperation("微信扫码登录")
    @GetMapping("wxLogin")
    public void wxLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        String auth = loginService.wxLogin(code, state);
        response.sendRedirect("http://www.think-tank.cn/Result" + auth);
    }

    @ApiOperation("账号密码登录")
    @PostMapping("passwordLogin")
    public R<String> passwordLogin(@RequestBody @Validated({QueryValidation.class}) SysUser sysUser) {
        return loginService.passwordLogin(sysUser);
    }

    @ApiOperation("验证登录状态")
    @GetMapping("isLogin")
    public R<Boolean> test() {
        return R.success(StpUtil.isLogin());
    }

    @ApiOperation("注销登录")
    @GetMapping("logout")
    public R<String> logout() {
        loginService.logout();
        return R.success("注销成功！");
    }

    @ApiOperation("管理员登录")
    @PostMapping("adminLogin")
    public R<String> adminLogin(@RequestBody SysUser sysUser) {
        return R.success(loginService.adminLogin(sysUser));
    }
}
