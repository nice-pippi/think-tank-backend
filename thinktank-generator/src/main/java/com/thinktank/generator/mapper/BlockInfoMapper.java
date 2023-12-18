package com.thinktank.generator.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thinktank.generator.dto.BlockInfoDto;
import com.thinktank.generator.entity.BlockInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thinktank.generator.vo.BlockInfoVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pippi
 * @since 2023-10-07
 */
public interface BlockInfoMapper extends BaseMapper<BlockInfo> {
    IPage<BlockInfoVo> page(Page<BlockInfo> page, @Param("blockInfoDto") BlockInfoDto blockInfoDto);
}
