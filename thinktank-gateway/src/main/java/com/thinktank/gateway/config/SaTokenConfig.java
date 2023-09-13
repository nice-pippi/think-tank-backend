package com.thinktank.gateway.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {
    // 注册 Sa-Token全局过滤器
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**")    /* 拦截全部path */
                // 开放地址
                .addExclude(
                        "/auth/register", // 注册
                        "/auth/passwordLogin", // 登录
                        "/auth/wxLogin", // 微信登录
                        "/auth/{id}", // 获取用户信息
                        "/validatecode/generate" // 发送验证码
                )
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    // 所有地址都需要登录校验
                    SaRouter.match("/**", r -> StpUtil.checkLogin());
                    // 权限认证 -- 不同模块, 校验不同权限
                    SaRouter.match("/post/**", r -> StpUtil.checkPermission("post"));
                })
                // 鉴权之前执行的操作
                .setBeforeAuth(obj -> {
                    SaHolder.getResponse()
                            // ---------- 设置跨域响应头 ----------
                            // 允许指定域访问跨域资源
                            .setHeader("Access-Control-Allow-Origin", "*")
                            // 允许所有请求方式
                            .setHeader("Access-Control-Allow-Methods", "*")
                            // 允许的header参数
                            .setHeader("Access-Control-Allow-Headers", "*")
                            // 指定本次预检请求的有效期，单位为秒，在此期间不用发出另一条预检请求。
                            .setHeader("Access-Control-Max-Age", "3600");
                    // 如果是预检请求，则立即返回到前端
                    SaRouter.match(SaHttpMethod.OPTIONS).back();
                })
                // 异常处理方法：每次setAuth函数出现异常时进入
                .setError(e -> {
                    return SaResult.error(e.getMessage());
                });
    }
}