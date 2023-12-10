package com.thinktank.message.config;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉10⽇ 16:42
 * @Description: 类描述
 * @Version: 1.0
 */

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.websocket.server.ServerEndpointConfig;


@Component
public class BasedSpringConfigurator extends ServerEndpointConfig.Configurator implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BasedSpringConfigurator.applicationContext = applicationContext;
    }

    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return applicationContext.getBean(clazz);
    }
}
