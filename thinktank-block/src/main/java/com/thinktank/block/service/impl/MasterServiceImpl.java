package com.thinktank.block.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thinktank.block.service.MasterService;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.generator.entity.BlockApplicationMaster;
import com.thinktank.generator.entity.BlockFollow;
import com.thinktank.generator.entity.BlockInfo;
import com.thinktank.generator.entity.SysUserRole;
import com.thinktank.generator.mapper.BlockApplicationMasterMapper;
import com.thinktank.generator.mapper.BlockFollowMapper;
import com.thinktank.generator.mapper.BlockInfoMapper;
import com.thinktank.generator.mapper.SysUserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉23⽇ 18:54
 * @Description: 板主管理接口实现类
 * @Version: 1.0
 */
@Slf4j
@Service
public class MasterServiceImpl implements MasterService {
    @Autowired
    private BlockApplicationMasterMapper blockApplicationMasterMapper;

    @Autowired
    private BlockInfoMapper blockInfoMapper;

    @Autowired
    private BlockFollowMapper blockFollowMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Transactional
    @Override
    public String applicationMaster(BlockApplicationMaster blockApplicationMaster) {
        // 查询板块是否存在
        BlockInfo blockInfo = blockInfoMapper.selectById(blockApplicationMaster.getBlockId());
        if (blockInfo == null) {
            log.error("板块'{}'不存在", blockApplicationMaster.getBlockId());
            throw new ThinkTankException(String.format("你当前申请板块id不存在！"));
        }

        // 获取当前登录用户id
        long id = StpUtil.getLoginIdAsLong();

        // 验证用户是否已关注该板块
        LambdaQueryWrapper<BlockFollow> blockFollowLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blockFollowLambdaQueryWrapper.eq(BlockFollow::getBlockId, blockApplicationMaster.getBlockId());
        blockFollowLambdaQueryWrapper.eq(BlockFollow::getUserId, id);
        Long count = blockFollowMapper.selectCount(blockFollowLambdaQueryWrapper);
        if (count == 0) {
            log.error("用户:'{}'未关注'{}'板块，无法申请板主", id, blockApplicationMaster.getBlockId());
            throw new ThinkTankException("你暂未关注本板块，无法申请板主身份！");
        }

        // 验证用户在当前板块是否被禁言
        LambdaQueryWrapper<SysUserRole> sysUserRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserRoleLambdaQueryWrapper.eq(SysUserRole::getBlockId, blockApplicationMaster.getBlockId());
        sysUserRoleLambdaQueryWrapper.eq(SysUserRole::getUserId, id);
        sysUserRoleLambdaQueryWrapper.eq(SysUserRole::getRoleId, 104);
        Long count2 = sysUserRoleMapper.selectCount(sysUserRoleLambdaQueryWrapper);
        if (count2 > 0) {
            log.error("用户:'{}'在'{}'板块已被禁言，无法申请板主", id, blockApplicationMaster.getBlockId());
            throw new ThinkTankException("你在本板块已被禁言，无法申请板主身份！");
        }

        // 查询是否已提交过该板块板主申请
        LambdaQueryWrapper<BlockApplicationMaster> blockApplicationMasterLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blockApplicationMasterLambdaQueryWrapper.eq(BlockApplicationMaster::getUserId, id);
        blockApplicationMasterLambdaQueryWrapper.eq(BlockApplicationMaster::getBlockId, blockApplicationMaster.getBlockId());
        BlockApplicationMaster one = blockApplicationMasterMapper.selectOne(blockApplicationMasterLambdaQueryWrapper);

        // 如果未查到该申请记录，则记录到申请记录表中
        if (one == null) {
            blockApplicationMaster.setUserId(id);
            blockApplicationMasterMapper.insert(blockApplicationMaster);
            return "已提交申请，请耐心等待管理员审核处理。";
        }

        // 查询申请状态 （0:待处理 1:已通过）
        String masterType = (one.getRoleId().equals(102L)) ? "板主" : "小板主"; // 102为版主身份
        if (one.getStatus().equals(0)) {
            log.error("用户'{}'在'{}'板块申请{}身份还在审核中", id, one.getBlockId(), masterType);
            throw new ThinkTankException(String.format("你当前板块的%s申请还在审核当中，请勿重复申请！", masterType));
        }
        if (one.getStatus().equals(1)) {
            log.error("用户'{}'已是'{}'板块{}身份", id, one.getBlockId(), masterType);
            throw new ThinkTankException(String.format("你已经是本板块的%s，无法重复申请本板块其他身份！", masterType));
        }

        // 重新申请，将驳回状态变更为待处理状态
        one.setStatus(0);
        blockApplicationMasterMapper.updateById(one);
        return "已重新提交申请，请耐心等待管理员审核处理。";
    }
}
