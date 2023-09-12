package com.thinktank.api.fallbackfactories;

import com.thinktank.api.clients.ValidateCodeClient;
import com.thinktank.common.utils.R;
import feign.hystrix.FallbackFactory;

import java.util.Map;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉11⽇ 18:36
 * @Description: 调用验证码降级处理
 * @Version: 1.0
 */
public class ValidateCodeClientFallBackFactory implements FallbackFactory<ValidateCodeClient> {

    @Override
    public ValidateCodeClient create(Throwable throwable) {
        return new ValidateCodeClient() {
            @Override
            public R<String> generate(Map<String, String> map) {
                return null;
            }

            @Override
            public R<String> validate(String email, String validateCode) {
                return R.error("服务器出现问题暂时无法注册，请稍后再试。");
            }
        };
    }
}
