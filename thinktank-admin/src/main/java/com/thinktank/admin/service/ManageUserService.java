package com.thinktank.admin.service;

import com.thinktank.generator.entity.SysUserRole;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉23⽇ 19:21
 * @Description: 用户管理业务接口
 * @Version: 1.0
 */
public interface ManageUserService {
    /**
     * 禁言用户
     *
     * @param sysUserRole 用户角色对象
     */
    void prohibit(SysUserRole sysUserRole);
}
