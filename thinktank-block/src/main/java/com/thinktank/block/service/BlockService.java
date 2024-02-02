package com.thinktank.block.service;

import com.thinktank.generator.dto.BlockClassifyDto;
import com.thinktank.generator.entity.BlockApplicationBlock;
import com.thinktank.generator.entity.BlockBigType;
import com.thinktank.generator.entity.BlockInfo;
import com.thinktank.generator.entity.BlockSmallType;
import com.thinktank.generator.vo.BlockInfoVo;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉22⽇ 14:57
 * @Description: 板块业务接口
 * @Version: 1.0
 */
public interface BlockService {
    /**
     * 获取所有板块分类
     *
     * @return 返回板块分类的列表
     */
    List<BlockClassifyDto> getBlockClassify();

    /**
     * 申请创建板块
     *
     * @param blockApplicationBlock 板块申请信息
     */
    void applicationBlock(BlockApplicationBlock blockApplicationBlock);

    /**
     * 根据id获取板块信息
     *
     * @param id 板块的id
     * @return 返回板块信息的视图对象
     */
    BlockInfoVo getBlockInfo(Long id);

    /**
     * 更改板块信息
     *
     * @param blockInfo 板块信息
     * @return 返回更改后的板块信息的视图对象
     */
    BlockInfoVo update(BlockInfo blockInfo);

    /**
     * 获取所有板块大分类
     *
     * @return 返回板块大分类的列表
     */
    List<BlockBigType> getBlockBigTypeList();

    /**
     * 获取所有板块小分类
     *
     * @return 返回板块小分类的列表
     */
    List<BlockSmallType> getBlockSmallTypeList();

    /**
     * 根据小分类ID获取所有板块
     *
     * @param smallTypeId 小分类ID
     * @return 返回板块的列表
     */
    List<BlockInfo> getAllBlockBySmallTypeId(Long smallTypeId);

    /**
     * 获取10个热门板块
     *
     * @return 返回热门板块的列表
     */
    List<BlockInfo> getHotBlock();

    /**
     * 验证当前登录是否指定板块的板主
     *
     * @param id 板块ID
     * @return true: 是 false: 否
     */
    Boolean isMaster(Long id);
}
