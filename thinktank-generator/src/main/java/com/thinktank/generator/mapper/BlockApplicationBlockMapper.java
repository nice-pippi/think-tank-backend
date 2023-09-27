package com.thinktank.generator.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
    IPage<BlockApplicationBlockVo> getApplicationBlockPage(IPage<BlockApplicationBlock> page, @Param("qw") Wrapper<BlockApplicationBlock> queryWrapper);
}
