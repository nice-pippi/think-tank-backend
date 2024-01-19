package com.thinktank.auth.mq;

import com.thinktank.auth.config.AddUserLoginRecordsFanoutConfig;
import com.thinktank.common.utils.RabbitMQUtil;
import com.thinktank.generator.entity.SysLoginRecords;
import com.thinktank.generator.mapper.SysLoginRecordsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: 弘
 * @CreateTime: 2024年01⽉19⽇ 15:05
 * @Description: 添加用户登录记录消费者
 * @Version: 1.0
 */
@Slf4j
@Component
public class AddUserLoginRecordsListener {
    @Autowired
    private SysLoginRecordsMapper sysLoginRecordsMapper;

    /**
     * 处理添加用户登录记录
     *
     * @param message 消息内容
     */
    @Transactional
    @RabbitListener(queues = AddUserLoginRecordsFanoutConfig.Queue_Name)
    public void addUserLoginRecords(Message message) {
        SysLoginRecords sysLoginRecords = RabbitMQUtil.getObject(message, SysLoginRecords.class);
        if (sysLoginRecordsMapper.insert(sysLoginRecords) == 0) {
            log.error("添加用户登录记录失败，用户id'{}'", sysLoginRecords.getUserId());
        }
    }
}
