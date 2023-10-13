package com.thinktank.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thinktank.admin.service.ApplicationMasterManageService;
import com.thinktank.generator.dto.BlockApplicationBlockDto;
import com.thinktank.generator.dto.BlockApplicationMasterDto;
import com.thinktank.generator.entity.BlockApplicationBlock;
import com.thinktank.generator.entity.BlockApplicationMaster;
import com.thinktank.generator.mapper.BlockApplicationMasterMapper;
import com.thinktank.generator.vo.BlockApplicationBlockVo;
import com.thinktank.generator.vo.BlockApplicationMasterVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public IPage<BlockApplicationMasterVo> page(BlockApplicationMasterDto blockApplicationMasterDto) {
        Page<BlockApplicationMaster> page = new Page<>(blockApplicationMasterDto.getCurrentPage(), blockApplicationMasterDto.getSize());
        return blockApplicationMasterMapper.getApplicationBlockPage(page, blockApplicationMasterDto);
    }

    @Override
    public void allow(Long id) {

    }

    @Override
    public void reject(Long id) {

    }

    @Override
    public void delete(Long id) {

    }
}
