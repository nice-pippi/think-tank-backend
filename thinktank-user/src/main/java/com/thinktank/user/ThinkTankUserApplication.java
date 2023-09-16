package com.thinktank.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉16⽇ 21:40
 * @Description: 类描述
 * @Version: 1.0
 */
@EnableFeignClients(basePackages = "com.thinktank.api.clients")
@SpringBootApplication(scanBasePackages = "com.thinktank")
public class ThinkTankUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThinkTankUserApplication.class, args);
    }
}
