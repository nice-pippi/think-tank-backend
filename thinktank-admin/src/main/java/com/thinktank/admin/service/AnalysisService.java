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
    /**
     * 用户统计分析
     *
     * @return 用户统计分析结果
     */
    UserStatisticsVo getUserStatistics();

    /**
     * 统计近一周每日用户登录人数情况
     *
     * @return 近一周每日用户登录人数情况
     */
    List<UserLoginCountBySevenDayVo> getUserLoginCountBySevenDay();

    /**
     * 获取待处理板块申请记录数量
     *
     * @return 待处理板块申请记录数量
     */
    Integer getTodoBlockApplyCount();

    /**
     * 获取待处理板主申请数量
     *
     * @return 待处理板主申请数量
     */
    Integer getTodoMasterApplyCount();

    /**
     * 获取待处理举报帖子数量
     *
     * @return 待处理举报帖子数量
     */
    Integer getTodoReportPostCount();

    /**
     * 获取待处理举报评论数量
     *
     * @return 待处理举报评论数量
     */
    Integer getTodoReportCommentCount();
}
