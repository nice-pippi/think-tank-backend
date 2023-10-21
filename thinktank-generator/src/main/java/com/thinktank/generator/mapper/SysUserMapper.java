package com.thinktank.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thinktank.generator.entity.SysUser;
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
     * @param id
     * @return
     */
    List<SysUser> getAllBlockMasterByBlockId(Long id);

    /**
     * 根据板块id获取当前板块所有小板主
     *
     * @param id
     * @return
     */
    List<SysUser> getAllBlockSmallMasterByBlockId(Long id);

    /**
     * 新增私信记录到用户私信表中
     *
     * @param count
     * @return
     */
    @Select("select id from sys_user order by rand() limit #{count}")
    List<Long> selectRandomList(Integer count);
}
