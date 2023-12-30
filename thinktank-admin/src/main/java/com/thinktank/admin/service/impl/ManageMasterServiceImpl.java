package com.thinktank.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thinktank.admin.service.ManageMasterService;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.generator.dto.MasterInfoDto;
import com.thinktank.generator.entity.SysUserRole;
import com.thinktank.generator.mapper.SysUserRoleMapper;
import com.thinktank.generator.vo.MasterInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉26⽇ 14:23
 * @Description: 板块板主业务接口实现类
 * @Version: 1.0
 */
@Slf4j
@Service
public class ManageMasterServiceImpl implements ManageMasterService {
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public IPage<MasterInfoVo> page(MasterInfoDto masterInfoDto) {
        if (masterInfoDto.getCurrentPage() == null || masterInfoDto.getCurrentPage() < 0) {
            log.error("当前页码非法");
            throw new ThinkTankException("当前页码非法");
        }
        if (masterInfoDto.getSize() == null || masterInfoDto.getSize() < 0) {
            log.error("每页显示数量非法");
            throw new ThinkTankException("每页显示数量非法");
        }
        Page<SysUserRole> page = new Page<>(masterInfoDto.getCurrentPage(), masterInfoDto.getSize());
        return sysUserRoleMapper.page(page, masterInfoDto);
    }

    @Transactional
    @Override
    public void updateRole(SysUserRole sysUserRole) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, sysUserRole.getUserId());
        queryWrapper.eq(SysUserRole::getBlockId, sysUserRole.getBlockId());
        if (sysUserRole.getRoleId().equals(0L)) {
            if (sysUserRoleMapper.delete(queryWrapper) == 0) {
                log.error("删除用户角色失败");
                throw new ThinkTankException("删除用户角色失败");
            }
        } else {
            if (sysUserRoleMapper.update(sysUserRole, queryWrapper) == 0) {
                log.error("更新用户角色失败");
                throw new ThinkTankException("更新用户角色失败");
            }
        }

        // 删除redis中当前板块板主集合缓存
        String namespace = "block:master:" + sysUserRole.getBlockId();
        redisTemplate.delete(namespace);
    }
}
