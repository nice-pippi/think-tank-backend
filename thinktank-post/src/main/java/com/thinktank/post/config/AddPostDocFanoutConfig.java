package com.thinktank.post.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉26⽇ 15:47
 * @Description: 用于处理添加帖子信息文档的Fanout类型交换机
 * @Version: 1.0
 */
@Slf4j
@Configuration
public class AddPostDocFanoutConfig {
    public static final String FANOUT_EXCHANGE = "postdoc.fanout";

    public static final String Queue_Name = "fanout.queue.postdoc";

    public static final String DIRECT_EXCHANGE_ERROR = "postdoc.error.direct";

    private static final String ERROR_QUEUE = "direct.queue.postdoc.error";

    /**
     * 声明交换机
     *
     * @return Fanout类型交换机
     */
    @Bean
    public FanoutExchange fanoutExchangeByAddPostDoc() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    /**
     * 返回一个队列对象
     *
     * @return 返回一个队列对象
     */
    @Bean
    public Queue fanoutQueueByAddPostDoc() {
        return new Queue(Queue_Name);
    }

    /**
     * 创建一个绑定队列和泛洪交换机的绑定
     *
     * @param fanoutQueueByAddPostDoc    泛洪队列
     * @param fanoutExchangeByAddPostDoc 泛洪交换机
     * @return 绑定对象
     */
    @Bean
    public Binding bindingQueueByAddPostDoc(Queue fanoutQueueByAddPostDoc, FanoutExchange fanoutExchangeByAddPostDoc) {
        return BindingBuilder.bind(fanoutQueueByAddPostDoc).to(fanoutExchangeByAddPostDoc);
    }

    /**
     * 定义处理失败消息的交换机
     *
     * @return 返回一个DirectExchange对象
     */
    @Bean
    public DirectExchange errorMessageExchangeByAddPostDoc() {
        return new DirectExchange(DIRECT_EXCHANGE_ERROR);
    }

    /**
     * 定义处理失败消息的队列
     *
     * @return 处理失败消息的队列
     */
    @Bean
    public Queue errorQueueByAddPostDoc() {
        return new Queue(ERROR_QUEUE, true);
    }

    /**
     * 绑定队列和交换机
     *
     * @param errorQueueByAddPostDoc           需要绑定的队列
     * @param errorMessageExchangeByAddPostDoc 需要绑定的交换机
     * @return 绑定结果
     */
    @Bean
    public Binding errorBindingByAddPostDoc(Queue errorQueueByAddPostDoc, DirectExchange errorMessageExchangeByAddPostDoc) {
        return BindingBuilder.bind(errorQueueByAddPostDoc).to(errorMessageExchangeByAddPostDoc).with("error");
    }

    /**
     * 定义一个RepublishMessageRecoverer，关联队列和交换机，将失败消息投递到指定的交换机
     *
     * @param rabbitTemplate RabbitMQ的模板对象，用于发送消息
     * @return 返回一个MessageRecoverer对象，用于消息恢复
     */
    @Bean
    public MessageRecoverer republishMessageRecovererByAddPostDoc(RabbitTemplate rabbitTemplate) {
        return new RepublishMessageRecoverer(rabbitTemplate, DIRECT_EXCHANGE_ERROR, "error");
    }
}
