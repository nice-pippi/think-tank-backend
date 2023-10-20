package com.thinktank.block;

import com.thinktank.common.config.SaTokenConfig;
import com.thinktank.common.exception.SentinelExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉21⽇ 14:33
 * @Description: 板块服务
 * @Version: 1.0
 */
@EnableFeignClients(basePackages = "com.thinktank.api.clients")
@Import({SaTokenConfig.class, SentinelExceptionHandler.class})
@SpringBootApplication(scanBasePackages = "com.thinktank")
public class ThinkTankBlockApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThinkTankBlockApplication.class, args);
    }
}
