package com.thinktank.api.fallbackfactories;

import com.thinktank.api.clients.UserClient;
import com.thinktank.api.entity.SysUserDto;
import com.thinktank.common.utils.R;
import com.thinktank.generator.entity.SysUser;
import feign.hystrix.FallbackFactory;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉16⽇ 12:26
 * @Description: 调用用户服务降级处理
 * @Version: 1.0
 */
public class UserClientFallBackFactory implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable throwable) {
        return new UserClient() {
            @Override
            public R<SysUser> getUserInfo(Long id) {
                return null;
            }

            @Override
            public R<SysUser> updateUser(SysUserDto sysUserDto) {
                return R.error("更改用户服务调用过程异常");
            }
        };
    }
}
