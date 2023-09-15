package com.thinktank.auth;

import com.thinktank.common.config.SaTokenConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: 弘
 * @CreateTime: 2023年08⽉15⽇ 11:36
 * @Description: 认证服务
 * @Version: 1.0
 */

@Import(SaTokenConfig.class)
@EnableFeignClients(basePackages = "com.thinktank.api.clients")
@SpringBootApplication(scanBasePackages = "com.thinktank")
public class ThinkTankAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThinkTankAuthApplication.class, args);
    }

    // 使用OkHttp3ClientHttpRequestFactory作为RestTemplate的底层实现
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory());
        return restTemplate;
    }
}
