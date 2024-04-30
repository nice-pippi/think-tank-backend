package com.thinktank.message.service;

/**
 * @Author: 弘
 * @CreateTime: 2024年4⽉30⽇ 15:53
 * @Description: Ai聊天业务接口
 * @Version: 1.0
 */
public interface AiChatService {
    /**
     * 与Ai助手聊天
     *
     * @param userMessage 用户消息
     * @return 聊天结果
     */
    String chat(String userMessage);
}
