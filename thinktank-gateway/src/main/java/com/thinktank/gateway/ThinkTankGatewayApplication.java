package com.thinktank.gateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: 弘
 * @CreateTime: 2023年08⽉14⽇ 23:07
 * @Description: 网关
 * @Version: 1.0
 */
@MapperScan("com.thinktank.generator.mapper")
@SpringBootApplication
public class ThinkTankGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThinkTankGatewayApplication.class, args);
    }
}
