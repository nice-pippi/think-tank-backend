package com.thinktank.auth.service;

import com.thinktank.common.utils.R;
import com.thinktank.generator.entity.SysUser;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉08⽇ 15:07
 * @Description: 登录管理接口
 * @Version: 1.0
 */
public interface LoginService {
    /**
     * 微信登录
     *
     * @param code  微信返回的code
     * @param state 微信返回的state
     * @return
     */
    String wxLogin(String code, String state);

    /**
     * 账号密码登录
     *
     * @param sysUser
     * @return
     */
    R<String> passwordLogin(SysUser sysUser);

    /**
     * 管理员登录
     *
     * @param sysUser
     * @return
     */
    String adminLogin(SysUser sysUser);
}
