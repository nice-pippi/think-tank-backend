package com.thinktank.user.service;

import com.thinktank.common.utils.R;
import com.thinktank.generator.entity.SysUser;
import com.thinktank.user.dto.SysUserDto;

import java.util.Map;

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
     * @return
     */
    R<SysUser> getUserInfo(Long id);

    /**
     * 修改用户信息
     *
     * @param sysUserDto
     * @return
     */
    R<SysUser> update(SysUserDto sysUserDto);
}
