package com.thinktank.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thinktank.generator.dto.SysUserDto;
import com.thinktank.generator.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
    /**
     * 根据板块id获取当前板块所有板主
     *
     * @param id 板块id
     * @return 所有板主列表
     */
    List<SysUser> getAllBlockMasterByBlockId(Long id);

    /**
     * 根据板块id获取当前板块所有小板主
     *
     * @param id 板块id
     * @return
     */
    List<SysUser> getAllBlockSmallMasterByBlockId(Long id);

    /**
     * 随机抽取用户
     *
     * @param count 要抽取的用户数量
     * @return 抽取的用户id列表
     */
    @Select("select id from sys_user order by rand() limit #{count}")
    List<Long> selectRandomList(Integer count);

    IPage<SysUser> page(Page<SysUser> page,@Param("sysUserDto") SysUserDto sysUserDto);
}
