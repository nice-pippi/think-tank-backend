package com.thinktank.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thinktank.generator.entity.SysLoginRecords;
import com.thinktank.generator.vo.UserLoginCountBySevenDayVo;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author pippi
 * @since 2024-01-19
 */
public interface SysLoginRecordsMapper extends BaseMapper<SysLoginRecords> {
    List<UserLoginCountBySevenDayVo> getUserLoginCountBySevenDay();
}
