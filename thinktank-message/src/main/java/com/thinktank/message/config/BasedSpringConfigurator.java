package com.thinktank.message.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.websocket.server.ServerEndpointConfig;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉10⽇ 16:42
 * @Description: 通过该配置类实现WebSocket终端点与Spring容器的集成，实现依赖注入。
 * @Version: 1.0
 */
@Component
public class BasedSpringConfigurator extends ServerEndpointConfig.Configurator implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * 设置应用程序上下文，由Spring容器调用。
     *
     * @param applicationContext Spring应用程序上下文
     * @throws BeansException 如果设置应用程序上下文时发生异常
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BasedSpringConfigurator.applicationContext = applicationContext;
    }

    /**
     * 获取WebSocket终端点的实例，并从Spring容器中获取指定类型的Bean。
     *
     * @param clazz 终端点的类型
     * @return 终端点的实例
     * @throws InstantiationException 如果无法实例化终端点
     */
    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return applicationContext.getBean(clazz);
    }
}
