package com.thinktank.generator.mapper;

import com.thinktank.generator.entity.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pippi
 * @since 2023-09-11
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    List<String> getRoleList(Object id);
}
