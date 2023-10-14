package com.thinktank.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thinktank.generator.entity.SysUser;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author pippi
 * @since 2023-09-16
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    List<SysUser> getAllBlockMasterByBlockId(Long id);

    List<SysUser> getAllBlockSmallMasterByBlockId(Long id);
}
