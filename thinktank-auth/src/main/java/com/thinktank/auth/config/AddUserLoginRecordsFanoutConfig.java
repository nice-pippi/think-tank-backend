package com.thinktank.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 弘
 * @CreateTime: 2024年01⽉19⽇ 14:57
 * @Description: 用于处理添加用户登录记录的Fanout类型交换机
 * @Version: 1.0
 */
@Slf4j
@Configuration
public class AddUserLoginRecordsFanoutConfig {
    public static final String FANOUT_EXCHANGE = "user.login.record.fanout";

    public static final String Queue_Name = "fanout.queue.user.login.record";

    public static final String DIRECT_EXCHANGE_ERROR = "user.login.record.error.direct";

    private static final String ERROR_QUEUE = "direct.queue.user.login.record.error";

    /**
     * 声明交换机
     *
     * @return Fanout类型交换机
     */
    @Bean
    public FanoutExchange fanoutExchangeByAddUserLoginRecord() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    /**
     * 返回一个队列对象
     *
     * @return 返回一个队列对象
     */
    @Bean
    public Queue fanoutQueueByAddUserLoginRecord() {
        return new Queue(Queue_Name);
    }

    /**
     * 创建一个绑定队列和泛洪交换机的绑定
     *
     * @param fanoutQueueByAddUserLoginRecord    泛洪队列
     * @param fanoutExchangeByAddUserLoginRecord 泛洪交换机
     * @return 绑定对象
     */
    @Bean
    public Binding bindingQueueByAddUserLoginRecord(Queue fanoutQueueByAddUserLoginRecord, FanoutExchange fanoutExchangeByAddUserLoginRecord) {
        return BindingBuilder.bind(fanoutQueueByAddUserLoginRecord).to(fanoutExchangeByAddUserLoginRecord);
    }

    /**
     * 定义处理失败消息的交换机
     *
     * @return 返回一个DirectExchange对象
     */
    @Bean
    public DirectExchange errorMessageExchangeByAddUserLoginRecord() {
        return new DirectExchange(DIRECT_EXCHANGE_ERROR);
    }

    /**
     * 定义处理失败消息的队列
     *
     * @return 处理失败消息的队列
     */
    @Bean
    public Queue errorQueueByAddUserLoginRecord() {
        return new Queue(ERROR_QUEUE, true);
    }

    /**
     * 绑定队列和交换机
     *
     * @param errorQueueByAddUserLoginRecord           需要绑定的队列
     * @param errorMessageExchangeByAddUserLoginRecord 需要绑定的交换机
     * @return 绑定结果
     */
    @Bean
    public Binding errorBindingByAddUserLoginRecord(Queue errorQueueByAddUserLoginRecord, DirectExchange errorMessageExchangeByAddUserLoginRecord) {
        return BindingBuilder.bind(errorQueueByAddUserLoginRecord).to(errorMessageExchangeByAddUserLoginRecord).with("error");
    }

    /**
     * 定义一个RepublishMessageRecoverer，关联队列和交换机，将失败消息投递到指定的交换机
     *
     * @param rabbitTemplate RabbitMQ的模板对象，用于发送消息
     * @return 返回一个MessageRecoverer对象，用于消息恢复
     */
    @Bean
    public MessageRecoverer republishMessageRecovererByAddUserLoginRecord(RabbitTemplate rabbitTemplate) {
        return new RepublishMessageRecoverer(rabbitTemplate, DIRECT_EXCHANGE_ERROR, "error");
    }
}
