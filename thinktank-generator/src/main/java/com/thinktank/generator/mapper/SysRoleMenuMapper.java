package com.thinktank.generator.mapper;

import com.thinktank.generator.entity.SysRoleMenu;
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
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
    /**
     * 获取所有权限列表
     * @param id
     * @return
     */
    List<String> getPermissionList(Object id);
}
