package com.thinktank.admin.service.impl;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.stp.StpUtil;
import com.thinktank.admin.service.AnalysisService;
import com.thinktank.admin.vo.UserStatisticsVo;
import com.thinktank.generator.entity.SysUser;
import com.thinktank.generator.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private RedisTemplate<String, String> redisTemplate;

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

        // 统计微信登录和密码登录比例情况
        long weChatCount = list.stream().filter(item -> item.getLoginType() == 1).count();
        long passwordCount = list.stream().filter(item -> item.getLoginType() == 0).count();

        UserStatisticsVo userStatisticsVo = new UserStatisticsVo();
        userStatisticsVo.setUserCount(list.size());
        userStatisticsVo.setNewUserCount(newUserCount);
        userStatisticsVo.setSexRatio(maleCount + "/" + femaleCount);
        userStatisticsVo.setLoginTypeRatio(weChatCount + "/" + passwordCount);

        // 获取在线人数
        // 1.1 获取所有已登录的会话id
        List<String> sessionIdList = StpUtil.searchSessionId("", 0, -1, false);

        // 1.2 set在线人数
        userStatisticsVo.setOnlineCount(sessionIdList.size());

        return userStatisticsVo;
    }
}
