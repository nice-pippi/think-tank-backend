package com.thinktank.user.service;

import com.thinktank.generator.dto.SysUserDto;
import com.thinktank.generator.entity.SysUser;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉08⽇ 23:22
 * @Description: 用户管理接口
 * @Version: 1.0
 */
public interface UserService {
    /**
     * 获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    SysUser getUserInfo(Long id);

    /**
     * 修改用户信息
     *
     * @param sysUserDto 用户信息Dto对象
     * @return 修改后的用户信息
     */
    SysUser update(SysUserDto sysUserDto);
}
