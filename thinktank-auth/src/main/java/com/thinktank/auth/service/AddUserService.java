package com.thinktank.auth.service;

import com.thinktank.generator.entity.SysUser;

import java.util.Map;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉08⽇ 23:22
 * @Description: 用户管理接口
 * @Version: 1.0
 */
public interface AddUserService {
    /**
     * 添加用户
     * @param userinfo
     * @return
     */
    SysUser addUser(Map<String, String> userinfo);
}
