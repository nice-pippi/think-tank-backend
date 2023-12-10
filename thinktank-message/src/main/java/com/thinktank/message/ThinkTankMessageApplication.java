package com.thinktank.message;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉05⽇ 17:24
 * @Description: 类描述
 * @Version: 1.0
 */
@MapperScan("com.thinktank.generator.mapper")
@SpringBootApplication(scanBasePackages = "com.thinktank")
public class ThinkTankMessageApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThinkTankMessageApplication.class, args);
    }
}
