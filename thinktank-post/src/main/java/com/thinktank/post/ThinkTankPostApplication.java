package com.thinktank.post;

import com.thinktank.common.config.SaTokenConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @Author: 弘
 * @CreateTime: 2023年08⽉18⽇ 15:55
 * @Description: 帖子服务
 * @Version: 1.0
 */
@Import(SaTokenConfig.class)
@SpringBootApplication(scanBasePackages = "com.thinktank")
public class ThinkTankPostApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThinkTankPostApplication.class, args);
    }
}
