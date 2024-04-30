package com.thinktank.message.service.impl;

import com.thinktank.message.service.AiChatService;
import com.thinktank.message.service.Assistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 弘
 * @CreateTime: 2024年4⽉30⽇ 15:55
 * @Description: Ai聊天业务接口实现类
 * @Version: 1.0
 */
@Service
public class AiChatServiceImpl implements AiChatService {
    @Autowired
    private Assistant assistant;

    @Override
    public String chat(String userMessage) {
        return assistant.chat(userMessage);
    }
}
