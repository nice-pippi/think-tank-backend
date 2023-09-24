package com.thinktank.block.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thinktank.block.service.MasterService;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.common.utils.R;
import com.thinktank.generator.entity.BlockApplicationMaster;
import com.thinktank.generator.entity.SysUser;
import com.thinktank.generator.mapper.BlockApplicationMasterMapper;
import com.thinktank.generator.mapper.SysUserMapper;
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
    private SysUserMapper sysUserMapper;

    @Autowired
    private BlockApplicationMasterMapper blockApplicationMasterMapper;

    @Transactional
    @Override
    public String applicationMaster(BlockApplicationMaster blockApplicationMaster) {
        // 获取当前登录用户id
        long id = StpUtil.getLoginIdAsLong();

        // 查询是否已提交过该板块板主申请
        LambdaQueryWrapper<BlockApplicationMaster> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlockApplicationMaster::getUserId, id);
        wrapper.eq(BlockApplicationMaster::getSmallTypeId, blockApplicationMaster.getSmallTypeId());
        BlockApplicationMaster one = blockApplicationMasterMapper.selectOne(wrapper);

        // 如果未查到该申请记录，则记录到申请记录表中
        if (one == null) {
            blockApplicationMaster.setUserId(id);
            blockApplicationMasterMapper.insert(blockApplicationMaster);
            return "已提交申请，请耐心等待管理员审核处理。";
        }

        // 查询申请状态 （0:待处理 1:已通过）
        String masterType = (one.getRoleId().equals(102L)) ? "板主" : "小板主"; // 102为版主身份
        if (one.getStatus().equals(0)) {
            log.error("用户'{}'在'{}'板块申请{}身份还在审核中", id, one.getSmallTypeId(), masterType);
            throw new ThinkTankException(String.format("你当前板块的%s申请还在审核当中，请勿重复申请！", masterType));
        }
        if (one.getStatus().equals(1)) {
            log.error("用户'{}'已是'{}'板块{}身份", id, one.getSmallTypeId(), masterType);
            throw new ThinkTankException(String.format("你已经是本板块的%s，无法重复申请本板块其他身份！", masterType));
        }

        // 重新申请，将驳回状态变更为待处理状态
        one.setStatus(0);
        blockApplicationMasterMapper.updateById(one);
        return "已重新提交申请，请耐心等待管理员审核处理。";
    }
}
