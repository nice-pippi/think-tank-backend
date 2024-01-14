package com.thinktank.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.util.concurrent.ListenableFutureCallback;

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
        correlationData.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.warn("消息发送异常, ID:{}, 原因{}", correlationData.getId(), throwable.getMessage());
            }

            @Override
            public void onSuccess(CorrelationData.Confirm confirm) {
                if (confirm.isAck()) {
                    // 3.1.ack表示消息成功
                    log.debug("消息发送成功, ID:{}", correlationData.getId());
                } else {
                    // 3.2.nack表示消息失败
                    log.warn("消息发送失败, ID:{}, 原因{}", correlationData.getId(), confirm.getReason());
                }
            }
        });
        return correlationData;
    }

    /**
     * 获取消息对象
     *
     * @param object 对象
     * @return 消息对象
     */
    public static Message getMessage(Object object) {
        String json = ObjectMapperUtil.toJSON(object);
        return MessageBuilder.withBody(json.getBytes(StandardCharsets.UTF_8)).build(); // 消息内容
    }
}
