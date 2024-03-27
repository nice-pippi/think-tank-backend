package com.thinktank.message.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉05⽇ 17:54
 * @Description: WebSocket配置文件
 * @Version: 1.0
 */
@Configuration
public class WebSocketConfig {
    /**
     * 创建并返回一个ServerEndpointExporter实例。
     *
     * @return ServerEndpointExporter实例，用于自动注册WebSocket端点。
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
