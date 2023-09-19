package com.thinktank.auth.service;

import com.thinktank.auth.dto.SysUserDto;
import com.thinktank.common.utils.R;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉10⽇ 20:10
 * @Description: 注册接口
 * @Version: 1.0
 */
public interface RegisterService {
    /**
     * 注册
     *
     * @param sysUserDto 用户信息实体类
     */
    void register(SysUserDto sysUserDto);
}
