package com.thinktank.validatecode;

import com.thinktank.common.config.SaTokenConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉10⽇ 16:48
 * @Description: 验证码服务
 * @Version: 1.0
 */
@Import(SaTokenConfig.class)
@SpringBootApplication(scanBasePackages = "com.thinktank")
public class ThinkTankValidateCodeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThinkTankValidateCodeApplication.class, args);
    }
}
