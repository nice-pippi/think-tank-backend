package com.thinktank.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thinktank.admin.service.ManageBlockClassifyService;
import com.thinktank.api.clients.BlockClient;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.common.utils.R;
import com.thinktank.generator.dto.BlockClassifyDto;
import com.thinktank.generator.entity.BlockBigType;
import com.thinktank.generator.entity.BlockSmallType;
import com.thinktank.generator.mapper.BlockBigTypeMapper;
import com.thinktank.generator.mapper.BlockSmallTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉16⽇ 13:58
 * @Description: 板块分类管理业务接口实现类
 * @Version: 1.0
 */
@Slf4j
@Service
public class ManageBlockClassifyServiceImpl implements ManageBlockClassifyService {
    @Autowired
    private BlockClient blockClient;

    @Autowired
    private BlockBigTypeMapper blockBigTypeMapper;

    @Autowired
    private BlockSmallTypeMapper blockSmallTypeMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 根据板块小分类获取板块大分类是否存在
     *
     * @param bigTypeId 板块大分类ID
     */
    private void getBlockBigTypeExists(Long bigTypeId) {
        BlockBigType blockBigType = blockBigTypeMapper.selectById(bigTypeId);
        if (blockBigType == null) {
            log.error("板块大分类ID不存在");
            throw new ThinkTankException("板块大分类ID不存在");
        }
    }

    @Transactional
    @Override
    public BlockBigType addBlockBigType(BlockBigType blockBigType) {
        LambdaQueryWrapper<BlockBigType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlockBigType::getBigTypeName, blockBigType.getBigTypeName());
        if (blockBigTypeMapper.selectCount(queryWrapper) > 0) {
            log.error("已存在同名板块大分类");
            throw new ThinkTankException("已存在同名板块大分类");
        }
        if (blockBigTypeMapper.insert(blockBigType) > 0) {
            redisTemplate.delete("block:classify");
            return blockBigType;
        }
        log.error("新增板块大分类失败");
        throw new ThinkTankException("新增板块大分类失败");
    }

    @Transactional
    @Override
    public BlockSmallType addBlockSmallType(BlockSmallType blockSmallType) {
        getBlockBigTypeExists(blockSmallType.getBigTypeId());
        LambdaQueryWrapper<BlockSmallType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlockSmallType::getBigTypeId, blockSmallType.getBigTypeId());
        queryWrapper.eq(BlockSmallType::getSmallTypeName, blockSmallType.getSmallTypeName());
        if (blockSmallTypeMapper.selectCount(queryWrapper) > 0) {
            log.error("已存在同名板块小分类");
            throw new ThinkTankException("已存在同名板块小分类");
        }

        if (blockSmallTypeMapper.insert(blockSmallType) > 0) {
            redisTemplate.delete("block:classify");
            return blockSmallType;
        }
        log.error("新增板块小分类失败");
        throw new ThinkTankException("新增板块小分类失败");
    }

    @Override
    public void deleteBlockBigType(Long id) {
        if (blockBigTypeMapper.deleteById(id) > 0) {
            redisTemplate.delete("block:classify");
            return;
        }
        log.error("删除板块大分类失败");
        throw new ThinkTankException("删除板块大分类失败");
    }

    @Override
    public void deleteBlockSmallType(Long id) {
        if (blockSmallTypeMapper.deleteById(id) > 0) {
            redisTemplate.delete("block:classify");
            return;
        }
        log.error("删除板块小分类失败");
        throw new ThinkTankException("删除板块小分类失败");
    }
}
