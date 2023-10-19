package com.thinktank.admin;

import com.thinktank.common.config.SaTokenConfig;
import com.thinktank.common.exception.SentinelExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

/**
 * @Author: 弘
 * @CreateTime: 2023年08⽉20⽇ 21:13
 * @Description: 后台管理服务
 * @Version: 1.0
 */
@EnableFeignClients(basePackages = "com.thinktank.api.clients")
@Import({SentinelExceptionHandler.class, SaTokenConfig.class})
@SpringBootApplication(scanBasePackages = "com.thinktank")
public class ThinkTankAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThinkTankAdminApplication.class, args);
    }
}
