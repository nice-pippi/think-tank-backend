package com.thinktank.auth.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thinktank.api.clients.ValidateCodeClient;
import com.thinktank.auth.dto.SysUserDto;
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
    public void register(SysUserDto sysUserDto) {
        // 查询该邮箱是否已被注册
        String email = sysUserDto.getEmail();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getEmail, email);
        Long count = sysUserMapper.selectCount(wrapper);

        // 若大于0，提示用户该账号已被注册
        if (count > 0) {
            throw new ThinkTankException("该邮箱已被注册过，请更换其他邮箱！");
        }

        // 远程调用校验验证码服务
        R<String> result = validatecodeClient.validate(sysUserDto.getEmail(), sysUserDto.getValidateCode());

        // 若状态不为200则抛出异常
        if (result.getStatus() != 200) {
            throw new ThinkTankException(result.getMsg());
        }

        // 将用户信息写入数据库
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserDto, sysUser);
        sysUser.setLoginType(0);
        sysUser.setAvatar("/user-avatar/default_avatar.png"); // 用户默认头像
        sysUser.setAccount(UUID.randomUUID().toString());
        String newPassword = BCrypt.hashpw(sysUserDto.getPassword(), BCrypt.gensalt()); // 将密码做BCrypt加密
        sysUser.setPassword(newPassword);
        sysUserMapper.insert(sysUser);

        // 为该用户分配普通角色权限
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(sysUser.getId());
        sysUserRole.setRoleId(104L);
        sysUserRoleMapper.insert(sysUserRole);
    }
}
