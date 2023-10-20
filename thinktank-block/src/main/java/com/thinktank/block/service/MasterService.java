package com.thinktank.block.service;

import com.thinktank.generator.entity.BlockApplicationMaster;
import com.thinktank.generator.vo.BlockMasterListVo;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉23⽇ 18:54
 * @Description: 板主管理业务接口
 * @Version: 1.0
 */
public interface MasterService {
    /**
     * 申请板主
     *
     * @param blockApplicationMaster
     */
    String applicationMaster(BlockApplicationMaster blockApplicationMaster);

    /**
     * 获取当前板块板主以及小板主信息
     *
     * @param id
     * @return
     */
    BlockMasterListVo getAllBlockMasterById(Long id);
}
