package com.thinktank.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thinktank.generator.dto.SysUserDto;
import com.thinktank.generator.entity.SysUser;
import com.thinktank.generator.entity.SysUserRole;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉23⽇ 19:21
 * @Description: 用户管理业务接口
 * @Version: 1.0
 */
public interface ManageUserService {
    /**
     * 分页查询板主
     *
     * @param sysUserDto 查询参数
     * @return 分页查询结果
     */
    IPage<SysUser> page(SysUserDto sysUserDto);

    /**
     * 更新密码
     *
     * @param sysUser 用户对象
     */
    void updatePassword(SysUser sysUser);

    /**
     * 更新登录类型
     *
     * @param sysUser 用户对象
     */
    void updateStatus(SysUser sysUser);

    /**
     * 禁言用户
     *
     * @param sysUserRole 用户角色对象
     */
    void prohibit(SysUserRole sysUserRole);
}
