package com.thinktank.admin.service;

import com.thinktank.generator.vo.UserLoginCountBySevenDayVo;
import com.thinktank.generator.vo.UserStatisticsVo;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2024年01⽉16⽇ 16:55
 * @Description: 数据分析业务接口
 * @Version: 1.0
 */
public interface AnalysisService {
    UserStatisticsVo getUserStatistics();

    List<UserLoginCountBySevenDayVo> getUserLoginCountBySevenDay();
}
