package com.thinktank.message.service;

import com.thinktank.generator.entity.MessagePrivate;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉09⽇ 15:26
 * @Description: 私信业务接口
 * @Version: 1.0
 */
public interface MessagePrivateService {
    /**
     * 根据聊天室id获取私聊消息
     *
     * @param chatRoomId 聊天室id
     * @return 聊天记录
     */
    List<MessagePrivate> getPrivateMessage(Long chatRoomId);
}
