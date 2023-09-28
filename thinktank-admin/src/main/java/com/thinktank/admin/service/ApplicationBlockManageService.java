package com.thinktank.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thinktank.generator.dto.BlockApplicationBlockDto;
import com.thinktank.generator.vo.BlockApplicationBlockVo;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉27⽇ 16:36
 * @Description: 板块管理接口
 * @Version: 1.0
 */
public interface ApplicationBlockManageService {
    /**
     * 获取申请板块分页
     *
     * @param blockApplicationBlockDto
     * @return
     */
    IPage<BlockApplicationBlockVo> getApplicationBlockPage(BlockApplicationBlockDto blockApplicationBlockDto);

    /**
     * 通过板块创建申请
     *
     * @param id 申请id
     */
    void allow(Long id);

    /**
     * 驳回板块创建申请
     *
     * @param id 申请id
     */
    void reject(Long id);

    /**
     * 删除板块创建申请记录
     * @param id
     */
    void delete(Long id);
}
