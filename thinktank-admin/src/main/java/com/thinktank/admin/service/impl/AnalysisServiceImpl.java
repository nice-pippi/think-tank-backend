package com.thinktank.admin.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thinktank.admin.service.AnalysisService;
import com.thinktank.generator.entity.BlockApplicationBlock;
import com.thinktank.generator.entity.BlockApplicationMaster;
import com.thinktank.generator.entity.PostReports;
import com.thinktank.generator.entity.SysUser;
import com.thinktank.generator.mapper.*;
import com.thinktank.generator.vo.UserLoginCountBySevenDayVo;
import com.thinktank.generator.vo.UserStatisticsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2024年01⽉16⽇ 16:56
 * @Description: 数据分析业务接口实现类
 * @Version: 1.0
 */
@Slf4j
@Service
public class AnalysisServiceImpl implements AnalysisService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysLoginRecordsMapper sysLoginRecordsMapper;

    @Autowired
    private BlockApplicationBlockMapper blockApplicationBlockMapper;

    @Autowired
    private BlockApplicationMasterMapper blockApplicationMasterMapper;

    @Autowired
    private PostReportsMapper postReportsMapper;

    @Override
    public UserStatisticsVo getUserStatistics() {
        List<SysUser> list = sysUserMapper.selectList(null);

        // 统计今新用户注册数量
        long newUserCount = list.stream()
                .filter(user -> user.getCreateTime().toLocalDate().equals(LocalDate.now()))
                .count();

        // 统计男女比例情况
        long maleCount = list.stream().filter(item -> item.getSex() == 1).count();
        long femaleCount = list.stream().filter(item -> item.getSex() == 0).count();

        UserStatisticsVo userStatisticsVo = new UserStatisticsVo();
        userStatisticsVo.setUserCount(list.size());
        userStatisticsVo.setNewUserCount((int) newUserCount);
        userStatisticsVo.setSexRatio(maleCount + "/" + femaleCount);

        // 获取在线人数
        // 1.1 获取所有已登录的会话id
        List<String> sessionIdList = StpUtil.searchSessionId("", 0, -1, false);

        // 1.2 set在线人数
        userStatisticsVo.setOnlineCount(sessionIdList.size());

        return userStatisticsVo;
    }

    @Override
    public List<UserLoginCountBySevenDayVo> getUserLoginCountBySevenDay() {
        // 构造前7天数据的list
        List<UserLoginCountBySevenDayVo> list = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        for (int i = 7; i > 0; i--) {
            LocalDate date = currentDate.minusDays(i);
            int month = date.getMonthValue();
            int day = date.getDayOfMonth();
            UserLoginCountBySevenDayVo userLoginCountBySevenDayVo = new UserLoginCountBySevenDayVo();
            userLoginCountBySevenDayVo.setLoginDate(month + "-" + day);
            userLoginCountBySevenDayVo.setUserCount(0);
            list.add(userLoginCountBySevenDayVo);
        }

        // 获取近一周每日用户登录人数情况
        List<UserLoginCountBySevenDayVo> userLoginCountBySevenDayVoList = sysLoginRecordsMapper.getUserLoginCountBySevenDay();

        // 匹配日期，日期相同的则set对应日期的登录人数
        for (UserLoginCountBySevenDayVo vo : list) {
            for (UserLoginCountBySevenDayVo dbVo : userLoginCountBySevenDayVoList) {
                if (vo.getLoginDate().equals(dbVo.getLoginDate())) {
                    vo.setUserCount(dbVo.getUserCount());
                    break;
                }
            }
        }
        return list;
    }

    @Override
    public Integer getTodoBlockApplyCount() {
        LambdaQueryWrapper<BlockApplicationBlock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlockApplicationBlock::getStatus, 0);
        return blockApplicationBlockMapper.selectCount(queryWrapper).intValue();
    }

    @Override
    public Integer getTodoMasterApplyCount() {
        LambdaQueryWrapper<BlockApplicationMaster> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlockApplicationMaster::getStatus, 0);
        return blockApplicationMasterMapper.selectCount(queryWrapper).intValue();
    }

    @Override
    public Integer getTodoReportPostCount() {
        LambdaQueryWrapper<PostReports> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostReports::getStatus, 0);
        queryWrapper.eq(PostReports::getCommentId, 0);
        return postReportsMapper.selectCount(queryWrapper).intValue();
    }

    @Override
    public Integer getTodoReportCommentCount() {
        LambdaQueryWrapper<PostReports> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostReports::getStatus, 0);
        queryWrapper.ne(PostReports::getCommentId, 0);
        return postReportsMapper.selectCount(queryWrapper).intValue();
    }
}
