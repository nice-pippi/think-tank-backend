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
public interface BlockManageService {
    /**
     * 获取申请板块分页
     *
     * @param blockApplicationBlockDto
     * @return
     */
    IPage<BlockApplicationBlockVo> getApplicationBlockPage(BlockApplicationBlockDto blockApplicationBlockDto);
}
