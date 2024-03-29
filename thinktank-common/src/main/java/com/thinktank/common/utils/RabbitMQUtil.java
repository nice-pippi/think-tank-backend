package com.thinktank.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @Author: 弘
 * @CreateTime: 2024年01⽉14⽇ 14:47
 * @Description: Rabbit MQ 工具类
 * @Version: 1.0
 */
@Slf4j
public class RabbitMQUtil {
    /**
     * 获取关联数据并返回
     *
     * @return CorrelationData对象
     */
    public static CorrelationData getCorrelationData() {
        // 1.生成全局唯一的消息ID，并封装到CorrelationData中
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        // 2.添加回调函数
        correlationData.getFuture().addCallback(
                confirm -> {
                    if (confirm.isAck()) {
                        // 3.1.ack，消息成功
                        log.debug("消息成功投递到交换机, ID:{}", correlationData.getId());
                    } else {
                        // 3.2.nack，消息失败
                        log.error("消息投递交换机失败, ID:{}, 原因{}", correlationData.getId(), confirm.getReason());
                    }
                },
                throwable -> log.error("消息投递交换机过程中发生异常, ID:{}, 原因{}", correlationData.getId(), throwable.getMessage())
        );
        return correlationData;
    }

    /**
     * 转换消息对象
     *
     * @param object 对象
     * @return 消息对象
     */
    public static Message transformMessage(Object object) {
        String json = ObjectMapperUtil.toJSON(object);
        return MessageBuilder.withBody(json.getBytes(StandardCharsets.UTF_8)).build(); // 消息内容
    }

    /**
     * 获取指定类型的对象
     *
     * @param message 消息
     * @param clazz   类型
     * @return 对象
     */
    public static <T> T getObject(Message message, Class<T> clazz) {
        byte[] bytes = message.getBody();
        String json = new String(bytes);
        return ObjectMapperUtil.toObject(json, clazz);
    }
}
