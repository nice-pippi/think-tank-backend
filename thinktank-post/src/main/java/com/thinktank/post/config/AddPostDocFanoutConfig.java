package com.thinktank.post.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉26⽇ 15:47
 * @Description: 用于添加帖子信息文档的Fanout类型交换机
 * @Version: 1.0
 */
@Slf4j
@Configuration
public class AddPostDocFanoutConfig implements ApplicationContextAware {
    public static final String FANOUT_EXCHANGE = "postdoc.fanout";

    public static final String Queue_Name = "fanout.queue.postdoc";

    public static final String DIRECT_EXCHANGE_ERROR = "postdoc.error.direct";

    private static final String ERROR_QUEUE = "direct.queue.post.error";

    /**
     * 声明交换机
     *
     * @return Fanout类型交换机
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    /**
     * 队列
     */
    @Bean
    public Queue fanoutQueue() {
        return new Queue(Queue_Name);
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindingQueue1(Queue fanoutQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue).to(fanoutExchange);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取RabbitTemplate
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        // 设置ReturnCallback
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            // 投递失败，记录日志
            log.info("消息发送失败，应答码{}，原因{}，交换机{}，路由键{},消息{}",
                    replyCode, replyText, exchange, routingKey, message.toString());
            // 如果有业务需要，可以重发消息
        });
    }

    /**
     * 中定义处理失败消息的交换机
     *
     * @return
     */
    @Bean
    public DirectExchange errorMessageExchange() {
        return new DirectExchange(DIRECT_EXCHANGE_ERROR);
    }

    /**
     * 定义处理失败消息的队列
     *
     * @return
     */
    @Bean
    public Queue errorQueue() {
        return new Queue(ERROR_QUEUE, true);
    }

    /**
     * 绑定队列和交换机
     *
     * @param errorQueue
     * @param errorMessageExchange
     * @return
     */
    @Bean
    public Binding errorBinding(Queue errorQueue, DirectExchange errorMessageExchange) {
        return BindingBuilder.bind(errorQueue).to(errorMessageExchange).with("error");
    }

    /**
     * 定义一个RepublishMessageRecoverer，关联队列和交换机
     *
     * @param rabbitTemplate
     * @return
     */
    @Bean
    public MessageRecoverer republishMessageRecoverer(RabbitTemplate rabbitTemplate) {
        return new RepublishMessageRecoverer(rabbitTemplate, DIRECT_EXCHANGE_ERROR, "error");
    }
}
