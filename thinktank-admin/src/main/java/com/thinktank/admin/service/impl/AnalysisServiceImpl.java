package com.thinktank.admin.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.thinktank.admin.service.AnalysisService;
import com.thinktank.generator.entity.SysUser;
import com.thinktank.generator.mapper.SysLoginRecordsMapper;
import com.thinktank.generator.mapper.SysUserMapper;
import com.thinktank.generator.vo.UserLoginCountBySevenDayVo;
import com.thinktank.generator.vo.UserStatisticsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Override
    public UserStatisticsVo getUserStatistics() {
        List<SysUser> list = sysUserMapper.selectList(null);

        // 统计新用户数量，当天注册的用户数量为新用户
        long newUserCount = list.stream().filter(item -> {
            LocalDateTime createTime = item.getCreateTime();
            return createTime.getDayOfYear() == LocalDateTime.now().getDayOfYear();
        }).count();

        // 统计男女比例情况
        long maleCount = list.stream().filter(item -> item.getSex() == 1).count();
        long femaleCount = list.stream().filter(item -> item.getSex() == 0).count();

        UserStatisticsVo userStatisticsVo = new UserStatisticsVo();
        userStatisticsVo.setUserCount(list.size());
        userStatisticsVo.setNewUserCount(newUserCount);
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
}
