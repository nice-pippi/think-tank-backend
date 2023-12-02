package com.thinktank.validatecode.service;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉10⽇ 15:18
 * @Description: 生成验证码、验证验证码接口
 * @Version: 1.0
 */
public interface ValidateCodeService {
    /**
     * 生成验证码
     *
     * @param email 邮箱
     */
    void generateCode(String email);

    /**
     * 校验验证码
     *
     * @param email        邮箱
     * @param validateCode 验证码
     */
    void validateCode(String email, String validateCode);
}
