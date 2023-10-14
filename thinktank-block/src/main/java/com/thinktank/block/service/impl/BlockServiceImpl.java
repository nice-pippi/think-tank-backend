package com.thinktank.block.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fasterxml.jackson.core.type.TypeReference;
import com.thinktank.block.service.BlockService;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.common.utils.ObjectMapperUtil;
import com.thinktank.common.utils.RedisCacheUtil;
import com.thinktank.generator.dto.BlockClassifyDto;
import com.thinktank.generator.entity.BlockApplicationBlock;
import com.thinktank.generator.entity.BlockBigType;
import com.thinktank.generator.entity.BlockInfo;
import com.thinktank.generator.entity.BlockSmallType;
import com.thinktank.generator.mapper.BlockApplicationBlockMapper;
import com.thinktank.generator.mapper.BlockBigTypeMapper;
import com.thinktank.generator.mapper.BlockInfoMapper;
import com.thinktank.generator.mapper.BlockSmallTypeMapper;
import com.thinktank.generator.vo.BlockInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public List<BlockClassifyDto> getBlockClassify() {
        // redis命名空间
        String namespace = "block:classify";
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        // 查询redis中是否存在板块分类，若存在直接返回
        String result = ops.get(namespace);
        if (result != null) {
            return RedisCacheUtil.getObjectByTypeReference(result, new TypeReference<List<BlockClassifyDto>>() {
            });
        }

        // 为查询大板块分类分配一个锁
        RLock lock = redissonClient.getLock("blockclassify");

        // 开启锁
        lock.lock();
        List<BlockClassifyDto> collect;

        try {
            // 查询redis中是否存在板块分类，若存在直接返回
            result = ops.get(namespace);
            if (result != null) {
                return RedisCacheUtil.getObjectByTypeReference(result, new TypeReference<List<BlockClassifyDto>>() {
                });
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
            collect = blockClassifyDtoList.stream().map(item -> {
                LambdaQueryWrapper<BlockSmallType> blockSmallTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
                blockSmallTypeLambdaQueryWrapper.eq(BlockSmallType::getBigTypeId, item.getId());
                List<BlockSmallType> blockSmallTypes = blockSmallTypeMapper.selectList(blockSmallTypeLambdaQueryWrapper);
                item.setSubCategories(blockSmallTypes);
                return item;
            }).collect(Collectors.toList());

            // 写入redis
            ops.set(namespace, ObjectMapperUtil.toJSON(collect));
        } finally {
            // 释放锁
            lock.unlock();
        }
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

    @Override
    public BlockInfoVo getBlockInfo(Long id) {
        // redis命名空间
        String namespace = "block:info:" + id;
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        // 若命中缓存，则直接返回缓存数据
        String result = ops.get(namespace);
        if (result != null) {
            return RedisCacheUtil.getObject(result, BlockInfoVo.class);
        }

        // 为不同板块信息分配一个锁
        RLock lock = redissonClient.getLock("block:info:lock:" + id);

        // 开启锁
        lock.lock();
        BlockInfoVo blockInfoVo;

        try {
            // 若命中缓存，则直接返回缓存数据
            result = ops.get(namespace);
            if (result != null) {
                return RedisCacheUtil.getObject(result, BlockInfoVo.class);
            }

            // 查询板块信息
            LambdaQueryWrapper<BlockInfo> blockInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            blockInfoLambdaQueryWrapper.eq(BlockInfo::getId, id);
            List<SFunction<BlockInfo, ?>> columnList = new ArrayList<>();
            columnList.add(BlockInfo::getId);
            columnList.add(BlockInfo::getSmallTypeId);
            columnList.add(BlockInfo::getBigTypeId);
            columnList.add(BlockInfo::getAvatar);
            columnList.add(BlockInfo::getBlockName);
            columnList.add(BlockInfo::getDescription);
            blockInfoLambdaQueryWrapper.select(columnList);
            BlockInfo blockInfo = blockInfoMapper.selectOne(blockInfoLambdaQueryWrapper);

            if (blockInfo == null) {
                log.error("板块'{}'不存在", id);
                throw new ThinkTankException("该板块不存在！");
            }

            // 根据板块信息的小分类id查询小分类名称
            LambdaQueryWrapper<BlockSmallType> blockSmallTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            blockSmallTypeLambdaQueryWrapper.eq(BlockSmallType::getId, blockInfo.getSmallTypeId());
            blockSmallTypeLambdaQueryWrapper.select(BlockSmallType::getSmallTypeName, BlockSmallType::getBigTypeId);
            BlockSmallType blockSmallType = blockSmallTypeMapper.selectOne(blockSmallTypeLambdaQueryWrapper);

            // 根据小分类id所属大分类id查询大分类名称
            LambdaQueryWrapper<BlockBigType> blockBigTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            blockBigTypeLambdaQueryWrapper.eq(BlockBigType::getId, blockInfo.getBigTypeId());
            blockBigTypeLambdaQueryWrapper.select(BlockBigType::getBigTypeName);
            BlockBigType blockBigType = blockBigTypeMapper.selectOne(blockBigTypeLambdaQueryWrapper);

            // 将信息copy到BlockInfoVo
            blockInfoVo = new BlockInfoVo();
            BeanUtils.copyProperties(blockInfo, blockInfoVo);
            blockInfoVo.setSmallTypeName(blockSmallType.getSmallTypeName());
            blockInfoVo.setBigTypeName(blockBigType.getBigTypeName());

            // 写入redis缓存，不同的key设置不同的生命周期，防止出现缓存雪崩的问题
            ops.set(namespace, ObjectMapperUtil.toJSON(blockInfoVo), Duration.ofHours(new Random().nextInt(20)));
        } finally {
            // 释放锁
            lock.unlock();
        }

        return blockInfoVo;
    }

    @Transactional
    @Override
    public BlockInfoVo update(BlockInfo blockInfo) {
        // 去除头像url的‘http://192.168.88.150:9000’前缀
        String baseUrl = blockInfo.getAvatar();
        if (!baseUrl.contains("/block-avatar")) {
            throw new ThinkTankException("头像url地址格式有误！");
        }
        String url = baseUrl.substring(baseUrl.indexOf("/block-avatar"));
        blockInfo.setAvatar(url);

        // 保存板块信息到数据库
        blockInfoMapper.updateById(blockInfo);

        // redis命名空间
        String namespace = "block:info:" + blockInfo.getId();

        // 删除redis中该板块的信息
        redisTemplate.delete(namespace);

        // 根据板块id查询板块信息
        BlockInfoVo blockInfoVo = getBlockInfo(blockInfo.getId());

        // 写入redis
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(namespace, ObjectMapperUtil.toJSON(blockInfoVo), Duration.ofHours(new Random().nextInt(20)));

        // 返回板块信息给用户
        return blockInfoVo;
    }

    @Override
    public List<BlockBigType> getBlockBigTypeList() {
        // redis命名空间
        String namespace = "block:classify:big";
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        String result = ops.get(namespace);

        // 若命中缓存，则直接返回数据
        if (result != null) {
            return RedisCacheUtil.getObjectByTypeReference(result, new TypeReference<List<BlockBigType>>() {
            });
        }

        // 为不同板块信息分配一个锁
        RLock lock = redissonClient.getLock(namespace + ":lock:");

        // 开启锁
        lock.lock();
        List<BlockBigType> list;

        try {
            // 若命中缓存，则直接返回数据
            result = ops.get(namespace);
            if (result != null) {
                return RedisCacheUtil.getObjectByTypeReference(result, new TypeReference<List<BlockBigType>>() {
                });
            }

            // 查询大分类集合
            LambdaQueryWrapper<BlockBigType> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(BlockBigType::getBigTypeName, BlockBigType::getId);
            list = blockBigTypeMapper.selectList(queryWrapper);

            // 写入redis
            ops.set(namespace, ObjectMapperUtil.toJSON(list));
        } finally {
            lock.unlock();
        }
        return list;
    }

    @Override
    public List<BlockSmallType> getBlockSmallTypeList() {
        // redis命名空间
        String namespace = "block:classify:small";
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        String result = ops.get(namespace);

        // 若命中缓存，则直接返回数据
        if (result != null) {
            return RedisCacheUtil.getObjectByTypeReference(result, new TypeReference<List<BlockSmallType>>() {
            });
        }

        // 为不同板块信息分配一个锁
        RLock lock = redissonClient.getLock(namespace + ":lock:");

        // 开启锁
        lock.lock();
        List<BlockSmallType> list;

        try {
            // 若命中缓存，则直接返回数据
            result = ops.get(namespace);
            if (result != null) {
                return RedisCacheUtil.getObjectByTypeReference(result, new TypeReference<List<BlockSmallType>>() {
                });
            }

            // 查询小分类集合
            LambdaQueryWrapper<BlockSmallType> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(BlockSmallType::getSmallTypeName, BlockSmallType::getId);
            list = blockSmallTypeMapper.selectList(queryWrapper);

            // 写入redis
            ops.set(namespace, ObjectMapperUtil.toJSON(list));
        } finally {
            lock.unlock();
        }
        return list;
    }
}
