package com.thinktank.api.config;

import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.stp.StpUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉16⽇ 15:57
 * @Description: feign拦截器, 在feign请求发出之前，加入一些操作
 * @Version: 1.0
 */
@Component
public class FeignInterceptor implements RequestInterceptor {
    // 为 Feign 的 RCP调用 添加请求头Same-Token
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(SaSameUtil.SAME_TOKEN, SaSameUtil.getToken());

        //将satoken添加到请求头中,使被调用方有会话状态
        requestTemplate.header(StpUtil.getTokenName(), StpUtil.getTokenValue());
    }
}