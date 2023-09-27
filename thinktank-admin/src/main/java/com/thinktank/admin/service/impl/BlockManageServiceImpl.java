package com.thinktank.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thinktank.admin.service.BlockManageService;
import com.thinktank.generator.dto.BlockApplicationBlockDto;
import com.thinktank.generator.entity.BlockApplicationBlock;
import com.thinktank.generator.mapper.BlockApplicationBlockMapper;
import com.thinktank.generator.vo.BlockApplicationBlockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉27⽇ 16:54
 * @Description: 板块管理接口实现类
 * @Version: 1.0
 */
@Service
public class BlockManageServiceImpl implements BlockManageService {
    @Autowired
    private BlockApplicationBlockMapper blockApplicationBlockMapper;

    @Override
    public IPage<BlockApplicationBlockVo> getApplicationBlockPage(BlockApplicationBlockDto blockApplicationBlockDto) {
        // 根据条件查询分页
        LambdaQueryWrapper<BlockApplicationBlock> wrapper = new LambdaQueryWrapper<>();

        // 若大分类id存在则新增匹配条件
        wrapper.eq(blockApplicationBlockDto.getBigTypeId() != null,
                BlockApplicationBlock::getBigTypeId,
                blockApplicationBlockDto.getBigTypeId());
        // 若大分类id存在则新增匹配条件
        wrapper.eq(blockApplicationBlockDto.getSmallTypeId() != null,
                BlockApplicationBlock::getSmallTypeId,
                blockApplicationBlockDto.getSmallTypeId());
        wrapper.orderByDesc(BlockApplicationBlock::getCreateTime);

        Page<BlockApplicationBlock> page = new Page<>(blockApplicationBlockDto.getCurrentPage(), blockApplicationBlockDto.getSize());
        IPage<BlockApplicationBlockVo> result = blockApplicationBlockMapper.getApplicationBlockPage(page, wrapper);
        return result;
    }
}
