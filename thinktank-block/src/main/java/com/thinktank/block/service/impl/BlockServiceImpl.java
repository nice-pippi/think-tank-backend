package com.thinktank.block.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.thinktank.block.dto.BlockClassifyDto;
import com.thinktank.block.service.BlockService;
import com.thinktank.generator.entity.BlockBigType;
import com.thinktank.generator.entity.BlockSmallType;
import com.thinktank.generator.mapper.BlockBigTypeMapper;
import com.thinktank.generator.mapper.BlockSmallTypeMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉22⽇ 14:58
 * @Description: 板块接口实现类
 * @Version: 1.0
 */
@Service
public class BlockServiceImpl implements BlockService {
    @Autowired
    private BlockBigTypeMapper blockBigTypeMapper;

    @Autowired
    private BlockSmallTypeMapper blockSmallTypeMapper;

    @Override
    public List<BlockClassifyDto> getBlockClassify() {
        // 查询所有板块大分类
        LambdaQueryWrapper<BlockBigType> blockBigTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<BlockBigType> blockBigTypes = blockBigTypeMapper.selectList(blockBigTypeLambdaQueryWrapper);

        // 将大分类信息拷贝到BlockClassifyDto
        List<BlockClassifyDto> blockClassifyDtoList = blockBigTypes.stream().map(item -> {
            BlockClassifyDto blockClassifyDto = new BlockClassifyDto();
            BeanUtils.copyProperties(item, blockClassifyDto);
            return blockClassifyDto;
        }).collect(Collectors.toList());

        // 查询所有板块的小分类，set到blockClassifyDtoList的subCategories属性
        List<BlockClassifyDto> collect = blockClassifyDtoList.stream().map(item -> {
            LambdaQueryWrapper<BlockSmallType> blockSmallTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            blockSmallTypeLambdaQueryWrapper.eq(BlockSmallType::getBigTypeId, item.getId());
            List<BlockSmallType> blockSmallTypes = blockSmallTypeMapper.selectList(blockSmallTypeLambdaQueryWrapper);
            item.setSubCategories(blockSmallTypes);
            return item;
        }).collect(Collectors.toList());

        return collect;
    }
}
