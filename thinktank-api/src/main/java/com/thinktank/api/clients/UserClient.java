package com.thinktank.api.clients;

import com.thinktank.api.config.FeignInterceptor;
import com.thinktank.api.entity.SysUserDto;
import com.thinktank.api.fallbackfactories.UserClientFallBackFactory;
import com.thinktank.common.utils.R;
import com.thinktank.generator.entity.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉16⽇ 12:16
 * @Description: User的Feign客户端
 * @Version: 1.0
 */
@FeignClient(value = "auth", configuration = FeignInterceptor.class, fallbackFactory = UserClientFallBackFactory.class)
@RequestMapping("/auth/user")
public interface UserClient {
    // 获取用户信息
    @GetMapping("{id}")
    R<SysUser> getUserInfo(@PathVariable("id") Long id);

    // 修改用户信息
    @PostMapping
    R<SysUser> updateUser(@RequestBody SysUserDto sysUserDto);
}
