package com.thinktank.block.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thinktank.block.service.FollowService;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.generator.entity.BlockFollow;
import com.thinktank.generator.entity.BlockInfo;
import com.thinktank.generator.mapper.BlockFollowMapper;
import com.thinktank.generator.mapper.BlockInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉20⽇ 14:18
 * @Description: 关注板块业务接口实现类
 * @Version: 1.0
 */
@Slf4j
@Service
public class FollowServiceImpl implements FollowService {
    @Autowired
    private BlockFollowMapper blockFollowMapper;

    @Autowired
    private BlockInfoMapper blockInfoMapper;

    @Transactional
    @Override
    public void followBlock(Long id) {
        BlockInfo blockInfo = blockInfoMapper.selectById(id);
        if (blockInfo == null) {
            log.error("板块'{}'不存在", id);
            throw new ThinkTankException("当前板块不存在！");
        }

        // 获取登录用户id
        long loginId = StpUtil.getLoginIdAsLong();

        // 查询是否已经关注过
        LambdaQueryWrapper<BlockFollow> blockFollowLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blockFollowLambdaQueryWrapper.eq(BlockFollow::getBlockId, id);
        blockFollowLambdaQueryWrapper.eq(BlockFollow::getUserId, loginId);
        BlockFollow blockFollow = blockFollowMapper.selectOne(blockFollowLambdaQueryWrapper);

        if (blockFollow != null) {
            throw new ThinkTankException("您已关注过当前板块，无需重复关注！");
        }

        // 新增记录到关注板块记录表
        blockFollow = new BlockFollow();
        blockFollow.setBlockId(id);
        blockFollow.setUserId(loginId);
        blockFollowMapper.insert(blockFollow);
    }

    @Override
    public List<BlockInfo> getAllFollow(Long id) {
        // 获取当前用户所有已关注板块
        LambdaQueryWrapper<BlockFollow> blockFollowLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blockFollowLambdaQueryWrapper.eq(BlockFollow::getUserId, id);
        blockFollowLambdaQueryWrapper.select(BlockFollow::getBlockId);
        List<Long> followListById = blockFollowMapper.selectList(blockFollowLambdaQueryWrapper)
                .stream().map(BlockFollow::getBlockId).collect(Collectors.toList());

        // 如果没有关注的板块则返回空
        if (followListById.size()==0) {
            return null;
        }

        // 根据关注id集合获取关注板块信息集合
        LambdaQueryWrapper<BlockInfo> blockInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blockInfoLambdaQueryWrapper.select(BlockInfo::getId, BlockInfo::getBlockName);
        blockInfoLambdaQueryWrapper.in(BlockInfo::getId, followListById);
        List<BlockInfo> blockInfoList = blockInfoMapper.selectList(blockInfoLambdaQueryWrapper);

        return blockInfoList;
    }


}
