package com.thinktank.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thinktank.generator.dto.MasterInfoDto;
import com.thinktank.generator.entity.SysUserRole;
import com.thinktank.generator.vo.MasterInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author pippi
 * @since 2023-09-11
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    /**
     * 获取所有角色列表
     *
     * @param id 用户ID
     * @return 所有角色列表
     */
    List<String> getRoleList(Object id);

    /**
     * 根据分页信息和主账号信息查询用户角色信息
     *
     * @param page          分页对象
     * @param masterInfoDto 角色信息对象
     * @return 分页结果
     */
    IPage<MasterInfoVo> page(Page<SysUserRole> page, @Param("masterInfoDto") MasterInfoDto masterInfoDto);
}
