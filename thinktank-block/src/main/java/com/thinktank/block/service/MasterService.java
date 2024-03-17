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
     * @param blockApplicationMaster 申请板主实例
     * @return 返回申请结果字符串
     */
    String applicationMaster(BlockApplicationMaster blockApplicationMaster);

    /**
     * 获取当前板块板主以及小板主信息
     *
     * @param id 板块ID
     * @return 返回当前板块板主以及小板主信息
     */
    BlockMasterListVo getAllBlockMasterById(Long id);

    /**
     * 验证当前登录是否指定板块的板主
     *
     * @param id 板块ID
     * @return true: 是 false: 否
     */
    Boolean isMaster(Long id);
}
