package com.thinktank.common.config;

import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.stp.StpLogic;
import com.thinktank.common.utils.R;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉15⽇ 10:57
 * @Description: sa-token配置类(其他类需要的时候用Import导入)
 * @Version: 1.0
 */

public class SaTokenConfig implements WebMvcConfigurer {
    // Sa-Token 整合 jwt (Simple 简单模式)
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }

    // 注册 Sa-Token 全局过滤器
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude("/**")
                .setAuth(obj -> {
                    SaSameUtil.checkCurrentRequestToken(); // 校验 Same-Token 身份凭证
                })
                .setError(e -> {
                    return R.error(e.getMessage());
                });
    }
}
