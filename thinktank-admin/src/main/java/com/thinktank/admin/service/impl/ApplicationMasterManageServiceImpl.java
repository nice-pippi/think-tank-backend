package com.thinktank.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thinktank.admin.service.ApplicationMasterManageService;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.generator.dto.BlockApplicationMasterDto;
import com.thinktank.generator.entity.BlockApplicationMaster;
import com.thinktank.generator.entity.SysUserRole;
import com.thinktank.generator.mapper.BlockApplicationMasterMapper;
import com.thinktank.generator.mapper.SysUserRoleMapper;
import com.thinktank.generator.vo.BlockApplicationMasterVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉13⽇ 18:27
 * @Description: 板主申请记录管理接口实现类
 * @Version: 1.0
 */
@Slf4j
@Service
public class ApplicationMasterManageServiceImpl implements ApplicationMasterManageService {
    @Autowired
    private BlockApplicationMasterMapper blockApplicationMasterMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public IPage<BlockApplicationMasterVo> page(BlockApplicationMasterDto blockApplicationMasterDto) {
        Page<BlockApplicationMaster> page = new Page<>(blockApplicationMasterDto.getCurrentPage(), blockApplicationMasterDto.getSize());
        return blockApplicationMasterMapper.getApplicationMasterPage(page, blockApplicationMasterDto);
    }

    // 获取该id的记录是否存在
    private BlockApplicationMaster getRecordExists(Long id) {
        BlockApplicationMaster blockApplicationMaster = blockApplicationMasterMapper.selectById(id);

        if (blockApplicationMaster == null) {
            log.error("未找到记录，该记录id为:{}", id);
            throw new ThinkTankException("未找到该申请记录!");
        }
        return blockApplicationMaster;
    }

    @Transactional
    @Override
    public void allow(Long id) {
        BlockApplicationMaster blockApplicationMaster = getRecordExists(id);

        // 若不为‘0’，则代表该申请记录已处理，无需重复处理
        if (!blockApplicationMaster.getStatus().equals(0)) {
            log.error("该记录已处理过，申请记录id:{}", blockApplicationMaster.getId());
            throw new ThinkTankException("该记录已处理过！");
        }

        // 更改申请记录状态为’通过‘
        blockApplicationMaster.setStatus(1);
        blockApplicationMasterMapper.updateById(blockApplicationMaster);

        // 为该用户分配该板块的板主/小板主角色
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(blockApplicationMaster.getUserId());
        sysUserRole.setRoleId(blockApplicationMaster.getRoleId());
        sysUserRole.setBlockId(blockApplicationMaster.getBlockId());
        sysUserRoleMapper.insert(sysUserRole);
    }

    @Transactional
    @Override
    public void reject(Long id) {
        BlockApplicationMaster blockApplicationMaster = getRecordExists(id);

        // 若不为‘0’，则代表该申请记录已处理，无需重复处理
        if (!blockApplicationMaster.getStatus().equals(0)) {
            log.error("该记录已处理过，申请记录id:{}", blockApplicationMaster.getId());
            throw new ThinkTankException("该记录已处理过！");
        }

        // 更改申请记录状态为’驳回‘
        blockApplicationMaster.setStatus(2);
        blockApplicationMasterMapper.updateById(blockApplicationMaster);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        BlockApplicationMaster blockApplicationMaster = getRecordExists(id);
        blockApplicationMasterMapper.deleteById(blockApplicationMaster.getId());
    }
}
