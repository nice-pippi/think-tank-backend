package com.thinktank.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thinktank.admin.service.ManageUserService;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.generator.dto.SysUserDto;
import com.thinktank.generator.entity.SysUser;
import com.thinktank.generator.entity.SysUserRole;
import com.thinktank.generator.mapper.SysUserMapper;
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
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public IPage<SysUser> page(SysUserDto sysUserDto) {
        if (sysUserDto.getCurrentPage() == null || sysUserDto.getCurrentPage() < 0) {
            log.error("当前页码非法");
            throw new ThinkTankException("当前页码非法");
        }
        if (sysUserDto.getSize() == null || sysUserDto.getSize() < 0) {
            log.error("每页显示数量非法");
            throw new ThinkTankException("每页显示数量非法");
        }
        Page<SysUser> page = new Page<>(sysUserDto.getCurrentPage(), sysUserDto.getSize());
        return sysUserMapper.page(page, sysUserDto);
    }

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
