package com.thinktank.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thinktank.auth.service.AddUserService;
import com.thinktank.generator.entity.SysUser;
import com.thinktank.generator.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉08⽇ 23:23
 * @Description: 用户管理接口实现类
 * @Version: 1.0
 */
@Service
public class AddUserServiceImpl implements AddUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Transactional
    @Override
    public SysUser addUser(Map<String, String> userinfo) {
        String unionid = userinfo.get("unionid");

        // 根据unionid去查数据库，若查到就直接返回该用户信息，反之就新增
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getAccount, unionid);
        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        if (sysUser != null) {
            return sysUser;
        }

        sysUser = new SysUser();
        sysUser.setLoginType(1);
        sysUser.setUsername(userinfo.get("nickname"));
        sysUser.setAvatar(userinfo.get("headimgurl"));
        sysUser.setAccount(unionid);
        sysUser.setPassword(unionid);

        // 添加用户到数据库
        sysUserMapper.insert(sysUser);
        return sysUser;
    }
}
