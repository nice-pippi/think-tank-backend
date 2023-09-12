package com.thinktank.api.clients;

import com.thinktank.api.fallbackfactories.ValidateCodeClientFallBackFactory;
import com.thinktank.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉11⽇ 9:11
 * @Description: validatecode的Feign客户端
 * @Version: 1.0
 */
@FeignClient(value = "validatecode", fallbackFactory = ValidateCodeClientFallBackFactory.class)
@RequestMapping("/validatecode")
public interface ValidateCodeClient {
    // 生成验证码
    @PostMapping("generate")
    R<String> generate(@RequestBody Map<String, String> map);

    // 校验验证码
    @GetMapping("validate")
    R<String> validate(@RequestParam("email") String email,@RequestParam("validateCode")  String validateCode);
}
