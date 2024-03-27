package com.thinktank.post.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 弘
 * @CreateTime: 2024年01⽉14⽇ 16:12
 * @Description: 用于设置RabbitTemplate的ReturnCallback。
 * @Version: 1.0
 */
@Slf4j
@Configuration
public class RabbitTemplateSetReturnCallbackConfig implements ApplicationContextAware {
    /**
     * 由于每个RabbitTemplate只能配置一个ReturnCallback，因此需要在项目加载时配置：
     *
     * @param applicationContext 应用上下文
     * @throws BeansException Beans异常
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取RabbitTemplate
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        // 设置ReturnCallback
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            // 投递失败，记录日志
            log.error("消息投递到队列失败，应答码{}，原因{}，交换机{}，路由键{},消息{}",
                    replyCode, replyText, exchange, routingKey, message.toString());
            // 如果有业务需要，可以重发消息
        });
    }
}
