package com.thinktank.validatecode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉10⽇ 16:48
 * @Description: 验证码服务
 * @Version: 1.0
 */
@SpringBootApplication(scanBasePackages = "com.thinktank")
public class ThinkTankValidateCodeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThinkTankValidateCodeApplication.class, args);
    }

}
