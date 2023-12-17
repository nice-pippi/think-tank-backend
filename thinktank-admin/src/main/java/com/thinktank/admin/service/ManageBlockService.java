package com.thinktank.admin.service;

import com.thinktank.common.utils.R;
import com.thinktank.generator.dto.BlockClassifyDto;
import com.thinktank.generator.entity.BlockBigType;
import com.thinktank.generator.entity.BlockSmallType;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉16⽇ 13:58
 * @Description: 板块管理业务接口
 * @Version: 1.0
 */
public interface ManageBlockService {
    /**
     * 获取板块分类列表
     *
     * @return 返回板块分类列表
     */
    R<List<BlockClassifyDto>> getBlockClassify();

    /**
     * 新增板块大分类
     *
     * @param blockBigType 板块大分类对象
     * @return 返回新增板块大分类对象
     */
    BlockBigType addBlockBigType(BlockBigType blockBigType);

    /**
     * 新增板块小分类
     *
     * @param blockSmallType 板块小分类对象
     * @return 返回新增板块小分类对象
     */
    BlockSmallType addBlockSmallType(BlockSmallType blockSmallType);

    /**
     * 删除板块大分类
     *
     * @param id 大板块分类ID
     */
    void deleteBlockBigType(Long id);

    /**
     * 删除板块小分类
     *
     * @param id 小板块分类ID
     */
    void deleteBlockSmallType(Long id);
}
