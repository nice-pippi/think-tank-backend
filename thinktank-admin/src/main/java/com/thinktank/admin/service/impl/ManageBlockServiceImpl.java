package com.thinktank.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thinktank.admin.service.ManageBlockService;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.generator.dto.BlockInfoDto;
import com.thinktank.generator.entity.BlockBigType;
import com.thinktank.generator.entity.BlockInfo;
import com.thinktank.generator.entity.BlockSmallType;
import com.thinktank.generator.mapper.BlockBigTypeMapper;
import com.thinktank.generator.mapper.BlockInfoMapper;
import com.thinktank.generator.mapper.BlockSmallTypeMapper;
import com.thinktank.generator.vo.BlockInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉18⽇ 17:45
 * @Description: 类描述
 * @Version: 1.0
 */
@Slf4j
@Service
public class ManageBlockServiceImpl implements ManageBlockService {
    @Autowired
    private BlockInfoMapper blockInfoMapper;

    @Autowired
    private BlockBigTypeMapper blockBigTypeMapper;

    @Autowired
    private BlockSmallTypeMapper blockSmallTypeMapper;

    @Override
    public IPage<BlockInfoVo> page(BlockInfoDto blockInfoDto) {
        Page<BlockInfo> page = new Page<>();
        return blockInfoMapper.page(page, blockInfoDto);
    }

    @Transactional
    @Override
    public BlockInfo addBlock(BlockInfo blockInfo) {
        if (blockInfo.getBigTypeId() == null && blockInfo.getSmallTypeId() == null) {
            log.error("板块类型不能为空");
            throw new ThinkTankException("板块类型不能为空");
        }

        BlockBigType blockBigType = blockBigTypeMapper.selectById(blockInfo.getBigTypeId());
        if (blockBigType == null) {
            log.error("板块大分类不存在");
            throw new ThinkTankException("板块大分类不存在");
        }

        BlockSmallType blockSmallType = blockSmallTypeMapper.selectById(blockInfo.getSmallTypeId());
        if (blockSmallType == null) {
            log.error("板块小分类不存在");
            throw new ThinkTankException("板块小分类不存在");
        }

        if (blockInfoMapper.insert(blockInfo) > 0) {
            return blockInfo;
        }

        log.error("新增板块失败，可能存在同名板块");
        throw new ThinkTankException("新增板块失败，可能存在同名板块");
    }


}
