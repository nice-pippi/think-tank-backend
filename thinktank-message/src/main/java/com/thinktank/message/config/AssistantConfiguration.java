package com.thinktank.message.config;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 弘
 * @CreateTime: 2024年4⽉30⽇ 9:48
 * @Description: Ai助手配置类
 * @Version: 1.0
 */
@Configuration
class AssistantConfiguration {
    /**
     * 创建并配置一个ChatMemory Bean实例，设置最大消息数为10。
     *
     * @return ChatMemory Bean实例
     */
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.withMaxMessages(10);
    }
}
