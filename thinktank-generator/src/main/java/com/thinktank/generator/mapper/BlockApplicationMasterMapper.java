package com.thinktank.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thinktank.generator.dto.BlockApplicationMasterDto;
import com.thinktank.generator.entity.BlockApplicationMaster;
import com.thinktank.generator.vo.BlockApplicationMasterVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author pippi
 * @since 2023-10-13
 */
public interface BlockApplicationMasterMapper extends BaseMapper<BlockApplicationMaster> {
    /**
     * 板主申请记录分页
     *
     * @param page
     * @param blockApplicationMasterDto
     * @return
     */
    IPage<BlockApplicationMasterVo> getApplicationMasterPage(Page<BlockApplicationMaster> page, @Param("blockApplicationMasterDto") BlockApplicationMasterDto blockApplicationMasterDto);
}
