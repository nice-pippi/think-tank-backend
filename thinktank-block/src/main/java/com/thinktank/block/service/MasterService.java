package com.thinktank.block.service;

import com.thinktank.generator.entity.BlockApplicationMaster;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉23⽇ 18:54
 * @Description: 板主管理接口
 * @Version: 1.0
 */
public interface MasterService {
    /**
     * 申请板主
     *
     * @param blockApplicationMaster
     */
    String applicationMaster(BlockApplicationMaster blockApplicationMaster);
}
