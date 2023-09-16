package com.thinktank.auth.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.thinktank.api.clients.ValidateCodeClient;
import com.thinktank.auth.service.LoginService;
import com.thinktank.common.utils.R;
import com.thinktank.common.validationgroups.QueryValidation;
import com.thinktank.generator.entity.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private ValidateCodeClient validatecodeClient;

    @ApiOperation("账号密码登录")
    @PostMapping("passwordLogin")
    public R<String> passwordLogin(@RequestBody @Validated({QueryValidation.class}) SysUser sysUser) {
        return loginService.passwordLogin(sysUser);
    }

    @ApiOperation("微信扫码登录")
    @GetMapping("wxLogin")
    public void wxLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String token = loginService.wxLogin(code, state);
        response.sendRedirect("http://localhost:8585/Result?token=" + token);
    }

    @ApiOperation("验证登录状态")
    @GetMapping("isLogin")
    public R<Boolean> test() {
        return R.success(StpUtil.isLogin());
    }

    @ApiOperation("注销登录")
    @GetMapping("logout")
    public R<String> logout() {
        StpUtil.logout();
        return R.success("注销成功！");
    }
}
