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
                        "/favicon.ico",
                        // 以下为开放swagger地址
                        "/**/swagger-ui/**",
                        "/**/webjars/**",
                        "/**/v2/api-docs",
                        "/**/swagger-resources/**"
                )
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    // 所有地址都需要登录校验
                    SaRouter.match("/**")
                            // 排除登录校验
                            .notMatch(
                                    "/auth/register", // 用户注册
                                    "/auth/passwordLogin", // 密码登录
                                    "/auth/adminLogin", // 管理员登录
                                    "/auth/wxLogin", // 微信登录
                                    "/validatecode/generate", // 生成验证码
                                    "/validatecode/validate", // 校验验证码
                                    "/block/getBlockClassify", // 获取板块分类
                                    "/block/{id}", // 获取板块信息
                                    "/block/master/{id}", // 查看当前板块板主以及小板主信息
                                    "/search/searchBlock", // 搜索板块
                                    "/search/searchPost", // 搜索帖子
                                    "/post/comment/page/**", // 帖子评论分页
                                    "/post/postAction/getLatestPosts", // 获取最新帖子
                                    "/post/postAction/page/**" // 根据板块id获取当前板块下帖子分页
                            )
                            .check(r -> StpUtil.checkLogin());
                    // 权限认证 -- 不同模块, 校验不同权限
                    SaRouter.match("/admin/**", r -> StpUtil.checkRole("super-admin")); // 检查是否具有超级管理员身份
                    SaRouter.match(SaHttpMethod.POST).match("/block/", r -> StpUtil.checkPermission("block:update")); // 检查更改板块权限
                    SaRouter.match("/search/block/**", r -> StpUtil.checkRole("super-admin")); // 检查板块信息文档管理权限
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