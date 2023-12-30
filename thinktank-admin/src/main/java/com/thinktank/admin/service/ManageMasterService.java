package com.thinktank.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thinktank.generator.dto.MasterInfoDto;
import com.thinktank.generator.entity.SysUserRole;
import com.thinktank.generator.vo.MasterInfoVo;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉26⽇ 14:22
 * @Description: 板块板主业务接口
 * @Version: 1.0
 */
public interface ManageMasterService {
    /**
     * 分页查询板主
     *
     * @param masterInfoDto 查询参数
     * @return 分页查询结果
     */
    IPage<MasterInfoVo> page(MasterInfoDto masterInfoDto);

    /**
     * 更新角色
     *
     * @param sysUserRole 角色信息
     */
    void updateRole(SysUserRole sysUserRole);
}
