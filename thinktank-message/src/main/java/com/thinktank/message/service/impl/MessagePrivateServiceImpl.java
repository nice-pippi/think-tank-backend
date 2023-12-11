package com.thinktank.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thinktank.generator.entity.MessagePrivate;
import com.thinktank.generator.mapper.MessagePrivateMapper;
import com.thinktank.message.service.MessagePrivateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉09⽇ 15:26
 * @Description: 类描述
 * @Version: 1.0
 */
@Slf4j
@Service
public class MessagePrivateServiceImpl implements MessagePrivateService {
    @Autowired
    private MessagePrivateMapper messagePrivateMapper;

    @Override
    public List<MessagePrivate> getPrivateMessage(Long chatRoomId) {
        LambdaQueryWrapper<MessagePrivate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MessagePrivate::getChatRoomId, chatRoomId);
        return messagePrivateMapper.selectList(queryWrapper);
    }

}
