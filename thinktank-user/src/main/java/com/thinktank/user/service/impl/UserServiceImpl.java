package com.thinktank.user.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.thinktank.api.clients.ValidateCodeClient;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.common.utils.R;
import com.thinktank.generator.dto.SysUserDto;
import com.thinktank.generator.entity.SysUser;
import com.thinktank.generator.mapper.SysUserMapper;
import com.thinktank.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉08⽇ 23:23
 * @Description: 用户管理接口实现类
 * @Version: 1.0
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private ValidateCodeClient validateCodeClient;

    @Override
    public SysUser getUserInfo(Long id) {
        // 查询用户
        SysUser sysUser = sysUserMapper.selectById(id);

        if (sysUser == null) {
            throw new ThinkTankException("用户不存在！");
        }

        sysUser.setPassword(null); // 密码是敏感数据，需要做空值处理
        return sysUser;
    }

    @Transactional
    @Override
    public SysUser update(SysUserDto sysUserDto) {
        // 如果用户身份不是管理员身份，则继续判断用户传来的id是否当前登录会话的id
        if (!StpUtil.hasRole("admin")) {
            if (sysUserDto.getId() != StpUtil.getLoginIdAsLong()) {
                throw new ThinkTankException("无法更改他人用户信息!");
            }
        }

        // 查询用户信息
        SysUser userInfo = getUserInfo(sysUserDto.getId());

        // 用户信息拷贝
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserDto, sysUser);

        // 判断是否修改邮箱
        if (sysUserDto.getEmail() != null && !sysUserDto.getEmail().equals(userInfo.getEmail())) {
            // 判断该邮箱是否已被使用
            LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysUser::getEmail, sysUserDto.getEmail());
            Long count = sysUserMapper.selectCount(wrapper);
            if (count > 0) {
                throw new ThinkTankException("该邮箱已被使用!");
            }

            // 远程调用校验验证码服务
            validate(sysUserDto);
        }

        // 判断是否修改密码（密码不为空代表修改密码）
        if (StringUtils.isNotEmpty(sysUserDto.getPassword())) {
            // 远程调用校验验证码服务
            validate(sysUserDto);
            String password = sysUserDto.getPassword();
            sysUser.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        }

        // 更改用户信息
        sysUserMapper.updateById(sysUser);
        return sysUser;
    }

    @Override
    public void updatePassword(SysUserDto sysUserDto) {
        // 验证邮箱、验证码和新密码是否为空
        if (StringUtils.isEmpty(sysUserDto.getEmail())) {
            throw new ThinkTankException("邮箱不能为空!");
        }
        if (StringUtils.isEmpty(sysUserDto.getValidateCode())) {
            throw new ThinkTankException("验证码不能为空!");
        }
        if (StringUtils.isEmpty(sysUserDto.getPassword())) {
            throw new ThinkTankException("新密码不能为空!");
        }

        // 远程调用校验验证码服务
        validate(sysUserDto);

        // 构建更新语句，设置新的加密密码
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysUser::getEmail, sysUserDto.getEmail());
        updateWrapper.set(SysUser::getPassword, BCrypt.hashpw(sysUserDto.getPassword(), BCrypt.gensalt()));
        int update = sysUserMapper.update(null, updateWrapper);

        // 如果更新记录数为0，说明该邮箱未注册，抛出异常
        if (update == 0) {
            throw new ThinkTankException("该邮箱未注册!");
        }
    }

    private void validate(SysUserDto sysUserDto) {
        R<String> result = validateCodeClient.validate(sysUserDto.getEmail(), sysUserDto.getValidateCode());
        if (result.getStatus() != 200) {
            throw new ThinkTankException(result.getMsg());
        }
    }
}
