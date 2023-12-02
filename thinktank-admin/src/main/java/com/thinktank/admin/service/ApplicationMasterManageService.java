package com.thinktank.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thinktank.generator.dto.BlockApplicationMasterDto;
import com.thinktank.generator.vo.BlockApplicationMasterVo;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉13⽇ 18:24
 * @Description: 板主申请记录管理接口
 * @Version: 1.0
 */
public interface ApplicationMasterManageService {
    /**
     * 获取板主申请板块分页
     *
     * @param blockApplicationMasterDto 包含查询条件的板主申请分页参数
     * @return 查询结果的板主申请分页对象
     */
    IPage<BlockApplicationMasterVo> page(BlockApplicationMasterDto blockApplicationMasterDto);

    /**
     * 通过板主申请
     *
     * @param id 申请id
     */
    void allow(Long id);

    /**
     * 驳回板主申请
     *
     * @param id 申请id
     */
    void reject(Long id);

    /**
     * 删除板主申请记录
     *
     * @param id 申请id
     */
    void delete(Long id);
}
