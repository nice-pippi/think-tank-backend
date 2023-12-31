package com.thinktank.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thinktank.generator.dto.BlockInfoDto;
import com.thinktank.generator.entity.BlockInfo;
import com.thinktank.generator.vo.BlockInfoVo;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉18⽇ 17:44
 * @Description: 板块管理业务接口
 * @Version: 1.0
 */
public interface ManageBlockService {
    /**
     * 分页查询板块
     *
     * @param blockInfoDto 板块信息实体类
     * @return 分页查询结果
     */
    IPage<BlockInfoVo> page(BlockInfoDto blockInfoDto);

    /**
     * 新增板块
     *
     * @param blockInfo 板块信息实体类
     */
    void addBlock(BlockInfo blockInfo);

    /**
     * 删除板块
     *
     * @param id 板块id
     */
    void deleteBlock(Long id);

    /**
     * 根据小分类id查询所有板块
     *
     * @param smallTypeId 小分类id
     * @return 板块集合
     */
    List<BlockInfo> getAllBlockBySmallTypeId(Long smallTypeId);
}
