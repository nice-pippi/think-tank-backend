package com.thinktank.block.service;

import com.thinktank.generator.dto.BlockClassifyDto;
import com.thinktank.generator.entity.BlockApplicationBlock;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉22⽇ 14:57
 * @Description: 板块接口
 * @Version: 1.0
 */
public interface BlockService {
    /**
     * 获取所有板块分类
     *
     * @return
     */
    List<BlockClassifyDto> getBlockClassify();

    /**
     * 申请创建板块
     *
     * @param blockApplicationBlock
     */
    void applicationBlock(BlockApplicationBlock blockApplicationBlock);
}
