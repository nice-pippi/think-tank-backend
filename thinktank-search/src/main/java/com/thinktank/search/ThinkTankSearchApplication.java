package com.thinktank.search;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉16⽇ 20:49
 * @Description: 搜索服务
 * @Version: 1.0
 */
@MapperScan("com.thinktank.generator.mapper")
@SpringBootApplication(scanBasePackages = "com.thinktank")
public class ThinkTankSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThinkTankSearchApplication.class, args);
    }
}
