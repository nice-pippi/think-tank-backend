package com.thinktank.message.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thinktank.generator.entity.MessagePrivate;
import com.thinktank.generator.mapper.MessagePrivateMapper;
import com.thinktank.generator.vo.MessageChatRoomVo;
import com.thinktank.message.service.MessagePrivateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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
    public List<MessageChatRoomVo> getPrivateMessageList() {
        long loginId = StpUtil.getLoginIdAsLong();
        List<MessageChatRoomVo> privateMessageList = messagePrivateMapper.getPrivateMessageList(loginId);
        privateMessageList.sort(Comparator.comparing(MessageChatRoomVo::getUpdateTime).reversed());
        return privateMessageList;
    }

    @Override
    public List<MessagePrivate> getPrivateMessage(Long chatRoomId) {
        LambdaQueryWrapper<MessagePrivate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MessagePrivate::getChatRoomId, chatRoomId);
        return messagePrivateMapper.selectList(queryWrapper);
    }

}
