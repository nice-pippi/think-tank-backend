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
    /**
     * 获取所有角色列表
     * @param id
     * @return
     */
    List<String> getRoleList(Object id);
}
