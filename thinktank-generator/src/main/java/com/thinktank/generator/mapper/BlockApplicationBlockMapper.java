package com.thinktank.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thinktank.generator.dto.BlockApplicationBlockDto;
import com.thinktank.generator.entity.BlockApplicationBlock;
import com.thinktank.generator.vo.BlockApplicationBlockVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author pippi
 * @since 2023-09-27
 */
public interface BlockApplicationBlockMapper extends BaseMapper<BlockApplicationBlock> {
    /**
     * 板块创建申请记录分页
     *
     * @param page                     分页对象
     * @param blockApplicationBlockDto 申请记录的板块信息对象
     * @return 分页结果
     */
    IPage<BlockApplicationBlockVo> page(IPage<BlockApplicationBlock> page, @Param("blockApplicationBlockDto") BlockApplicationBlockDto blockApplicationBlockDto);
}
