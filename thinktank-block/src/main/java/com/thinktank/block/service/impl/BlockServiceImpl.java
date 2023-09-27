package com.thinktank.block.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinktank.block.service.BlockService;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.common.utils.ObjectMapperUtil;
import com.thinktank.generator.dto.BlockClassifyDto;
import com.thinktank.generator.entity.BlockApplicationBlock;
import com.thinktank.generator.entity.BlockBigType;
import com.thinktank.generator.entity.BlockInfo;
import com.thinktank.generator.entity.BlockSmallType;
import com.thinktank.generator.mapper.BlockApplicationBlockMapper;
import com.thinktank.generator.mapper.BlockBigTypeMapper;
import com.thinktank.generator.mapper.BlockInfoMapper;
import com.thinktank.generator.mapper.BlockSmallTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉22⽇ 14:58
 * @Description: 板块接口实现类
 * @Version: 1.0
 */
@Slf4j
@Service
public class BlockServiceImpl implements BlockService {
    @Autowired
    private BlockBigTypeMapper blockBigTypeMapper;

    @Autowired
    private BlockSmallTypeMapper blockSmallTypeMapper;

    @Autowired
    private BlockApplicationBlockMapper blockApplicationBlockMapper;

    @Autowired
    private BlockInfoMapper blockInfoMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<BlockClassifyDto> getBlockClassify() {
        ValueOperations ops = redisTemplate.opsForValue();

        // 查询redis中是否存在板块分类，若存在直接返回
        if (ops.get("blockClassify") != null) {
            // 获取 Redis 中的值
            String blockClassifyJson = ops.get("blockClassify").toString();

            // 使用 ObjectMapper 将 JSON 字符串转换为 List<BlockClassifyDto>
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, BlockClassifyDto.class);

            try {
                List<BlockClassifyDto> blockClassifyDtoList = objectMapper.readValue(blockClassifyJson, javaType);
                return blockClassifyDtoList;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
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

        // 写入redis
        ops.set("blockClassify", ObjectMapperUtil.toJSON(collect));
        return collect;
    }

    @Transactional
    @Override
    public void applicationBlock(BlockApplicationBlock blockApplicationBlock) {
        // 获取当前登录用户id
        long id = StpUtil.getLoginIdAsLong();

        // 查询是否存在同名板块
        LambdaQueryWrapper<BlockInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlockInfo::getBlockName, blockApplicationBlock.getBlockName());
        Long count = blockInfoMapper.selectCount(wrapper);
        if (count > 0) {
            log.error("已存在'{}'板块，申请用户id为:{}", blockApplicationBlock.getBlockName(), id);
            throw new ThinkTankException("已存在同名板块！");
        }

        // 将创建板块申请记录到数据库
        blockApplicationBlock.setUserId(id);
        blockApplicationBlockMapper.insert(blockApplicationBlock);
    }
}
