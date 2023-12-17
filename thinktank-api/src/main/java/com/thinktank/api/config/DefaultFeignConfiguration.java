package com.thinktank.api.config;

import com.thinktank.api.fallbackfactories.BlockClientFallBackFactory;
import com.thinktank.api.fallbackfactories.SearchClientFallBackFactory;
import com.thinktank.api.fallbackfactories.UserClientFallBackFactory;
import com.thinktank.api.fallbackfactories.ValidateCodeClientFallBackFactory;
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉11⽇ 13:46
 * @Description: 类描述
 * @Version: 1.0
 */
@Configuration
public class DefaultFeignConfiguration {
    // 日志水平
    @Bean
    public Logger.Level logLevel() {
        return Logger.Level.BASIC;
    }

    // 将降级处理的类注册到bean，否则其他服务无法调用降级服务的方法
    @Bean
    public ValidateCodeClientFallBackFactory validateCodeClientFallBackFactory() {
        return new ValidateCodeClientFallBackFactory();
    }

    @Bean
    public UserClientFallBackFactory userClientFallBackFactory() {
        return new UserClientFallBackFactory();
    }

    @Bean
    public SearchClientFallBackFactory searchClientFallBackFactory() {
        return new SearchClientFallBackFactory();
    }

    @Bean
    public BlockClientFallBackFactory blockClientFallBackFactory() {
        return new BlockClientFallBackFactory();
    }
}
