package com.thinktank.api.fallbackfactories;

import com.thinktank.api.clients.ValidateCodeClient;
import com.thinktank.common.utils.R;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉11⽇ 18:36
 * @Description: 调用验证码服务降级处理
 * @Version: 1.0
 */
@Slf4j
public class ValidateCodeClientFallBackFactory implements FallbackFactory<ValidateCodeClient> {
    @Override
    public ValidateCodeClient create(Throwable throwable) {
        String message = throwable.getMessage();
        log.error("调用验证码服务异常:{}", message);
        return new ValidateCodeClient() {
            @Override
            public R<String> generate(Map<String, String> map) {
                return R.error(message);
            }

            @Override
            public R<String> validate(String email, String validateCode) {
                return R.error(message);
            }
        };
    }
}
