package com.thinktank.auth.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thinktank.api.clients.ValidateCodeClient;
import com.thinktank.auth.dto.RegisterOrUpdateSysUserDto;
import com.thinktank.auth.service.RegisterService;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.common.utils.R;
import com.thinktank.generator.entity.SysUser;
import com.thinktank.generator.entity.SysUserRole;
import com.thinktank.generator.mapper.SysUserMapper;
import com.thinktank.generator.mapper.SysUserRoleMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉10⽇ 20:12
 * @Description: 注册接口实现类
 * @Version: 1.0
 */
@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private ValidateCodeClient validatecodeClient;

    @Autowired
    private RedisTemplate redisTemplate;


    @Transactional
    @Override
    public R<String> register(RegisterOrUpdateSysUserDto registerOrUpdateSysUserDto) {
        // 查询该邮箱是否已被注册
        String email = registerOrUpdateSysUserDto.getEmail();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getEmail, email);
        Long count = sysUserMapper.selectCount(wrapper);

        // 若大于0，提示用户该账号已被注册
        if (count > 0) {
            throw new ThinkTankException("该邮箱已被注册过，请更换其他邮箱！");
        }

        // 远程调用校验验证码服务
        R<String> result = validatecodeClient.validate(registerOrUpdateSysUserDto.getEmail(), registerOrUpdateSysUserDto.getValidateCode());

        // 若状态未200代表验证码校验成功
        if (result.getStatus() == 200) {
            // 将用户信息写入数据库
            SysUser sysUser = new SysUser();
            BeanUtils.copyProperties(registerOrUpdateSysUserDto, sysUser);
            sysUser.setAvatar("https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png");
            sysUser.setAccount(UUID.randomUUID().toString());
            String newPassword = BCrypt.hashpw(registerOrUpdateSysUserDto.getPassword(), BCrypt.gensalt()); // 将密码做BCrypt加密
            sysUser.setPassword(newPassword);
            sysUserMapper.insert(sysUser);

            // 为该用户分配普通角色权限
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(sysUser.getId());
            sysUserRole.setRoleId(104L);
            sysUserRoleMapper.insert(sysUserRole);

            // 删除该邮箱对应的key
            redisTemplate.delete(registerOrUpdateSysUserDto.getEmail());

            return R.success("注册成功，快去登录吧~");
        }
        // 返回校验失败结果
        return result;
    }
}
