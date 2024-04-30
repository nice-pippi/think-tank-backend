package com.thinktank.message.service;

import dev.langchain4j.service.spring.AiService;

/**
 * @Author: 弘
 * @CreateTime: 2024年4⽉30⽇ 9:58
 * @Description: 带有注释 @AiChatService 的接口将自动注册为 bean，并与上下文中可用的所有以下组件（bean）连接：
 * - ChatLanguageModel
 * - StreamingChatLanguageModel
 * - ChatMemory
 * - ChatMemoryProvider
 * - ContentRetriever
 * - RetrievalAugmentor
 * - All beans containing methods annotated with  @Tool
 * @Version: 1.0
 */
@AiService
public interface Assistant {

    /**
     * 与Ai助手聊天
     *
     * @param userMessage 用户消息
     * @return 聊天结果
     */
    String chat(String userMessage);
}