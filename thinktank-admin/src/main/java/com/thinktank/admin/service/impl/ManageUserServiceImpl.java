package com.thinktank.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thinktank.admin.service.ManageUserService;
import com.thinktank.generator.entity.SysUserRole;
import com.thinktank.generator.mapper.SysUserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉23⽇ 19:21
 * @Description: 用户管理业务接口实现类
 * @Version: 1.0
 */
@Slf4j
@Service
public class ManageUserServiceImpl implements ManageUserService {
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Transactional
    @Override
    public void prohibit(SysUserRole sysUserRole) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, sysUserRole.getUserId());
        queryWrapper.eq(SysUserRole::getBlockId, sysUserRole.getBlockId());

        Long roleId = 104L;
        sysUserRole.setRoleId(roleId);
        if (sysUserRoleMapper.selectCount(queryWrapper) > 0) {
            sysUserRoleMapper.update(sysUserRole, queryWrapper);
        } else {
            sysUserRoleMapper.insert(sysUserRole);
        }
    }
}
