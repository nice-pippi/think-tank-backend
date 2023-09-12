package com.thinktank.validatecode.service;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉10⽇ 18:08
 * @Description: 发送邮箱接口
 * @Version: 1.0
 */
public interface SendMailService {
    void sendMail(String title, String acceptEmail, String context);
}
