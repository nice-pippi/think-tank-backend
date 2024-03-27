package com.thinktank.message.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thinktank.generator.entity.MessageChatRoom;
import com.thinktank.generator.mapper.MessageChatRoomMapper;
import com.thinktank.generator.vo.MessageChatRoomVo;
import com.thinktank.message.service.MessageChatRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉11⽇ 18:47
 * @Description: 私信聊天室业务接口实现类
 * @Version: 1.0
 */
@Slf4j
@Service
public class MessageChatRoomServiceImpl implements MessageChatRoomService {
    @Autowired
    private MessageChatRoomMapper messageChatRoomMapper;

    @Override
    public List<MessageChatRoomVo> getAllChatRoom() {
        long loginId = StpUtil.getLoginIdAsLong();
        List<MessageChatRoomVo> privateMessageList = messageChatRoomMapper.getAllChatRoom(loginId);
        privateMessageList.sort(Comparator.comparing(MessageChatRoomVo::getUpdateTime).reversed());
        return privateMessageList;
    }

    @Transactional
    @Override
    public void addChatRoom(MessageChatRoom messageChatRoom) {
        LambdaQueryWrapper<MessageChatRoom> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(MessageChatRoom::getUserIdA, messageChatRoom.getUserIdA())
                .eq(MessageChatRoom::getUserIdB, messageChatRoom.getUserIdB())
                .or()
                .eq(MessageChatRoom::getUserIdA, messageChatRoom.getUserIdB())
                .eq(MessageChatRoom::getUserIdB, messageChatRoom.getUserIdA());

        Long count = messageChatRoomMapper.selectCount(queryWrapper);
        if (count == 0) {
            messageChatRoomMapper.insert(messageChatRoom);
        }
    }
}
