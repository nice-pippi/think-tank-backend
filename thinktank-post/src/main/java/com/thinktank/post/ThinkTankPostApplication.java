package com.thinktank.post;

import com.thinktank.common.config.SaTokenConfig;
import com.thinktank.common.exception.SentinelExceptionHandler;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @Author: 弘
 * @CreateTime: 2023年08⽉18⽇ 15:55
 * @Description: 帖子服务
 * @Version: 1.0
 */
@EnableFeignClients(basePackages = "com.thinktank.api.clients")
@Import({SaTokenConfig.class, SentinelExceptionHandler.class})
@SpringBootApplication(scanBasePackages = "com.thinktank")
public class ThinkTankPostApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThinkTankPostApplication.class, args);
    }

    /**
     * 修改MQ默认消息转换器，采用JSON转换器
     *
     * @return
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
