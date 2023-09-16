package com.thinktank.file;

import com.thinktank.common.config.SaTokenConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉15⽇ 13:02
 * @Description: 文件服务
 * @Version: 1.0
 */
@Import(SaTokenConfig.class)
@SpringBootApplication(scanBasePackages = "com.thinktank")
public class ThinkTankFileApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThinkTankFileApplication.class, args);
    }
}
