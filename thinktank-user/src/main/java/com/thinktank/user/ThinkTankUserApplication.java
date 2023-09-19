package com.thinktank.user;

import com.thinktank.api.config.FeignInterceptor;
import com.thinktank.common.config.SaTokenConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉16⽇ 21:40
 * @Description: 类描述
 * @Version: 1.0
 */
@Import({SaTokenConfig.class})
@EnableFeignClients(basePackages = "com.thinktank.api.clients")
@SpringBootApplication(scanBasePackages = "com.thinktank")
public class ThinkTankUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThinkTankUserApplication.class, args);
    }
}
