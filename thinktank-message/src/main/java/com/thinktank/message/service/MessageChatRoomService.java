package com.thinktank.message.service;

import com.thinktank.generator.entity.MessageChatRoom;
import com.thinktank.generator.vo.MessageChatRoomVo;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉11⽇ 18:39
 * @Description: 私信聊天室业务接口
 * @Version: 1.0
 */
public interface MessageChatRoomService {
    /**
     * @return 返回所有聊天室列表
     */
    List<MessageChatRoomVo> getAllChatRoom();

    /**
     * @param messageChatRoom 聊天室实体类
     */
    void addChatRoom(MessageChatRoom messageChatRoom);
}
