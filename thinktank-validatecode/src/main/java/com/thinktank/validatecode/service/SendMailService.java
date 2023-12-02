package com.thinktank.validatecode.service;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉10⽇ 18:08
 * @Description: 发送邮箱接口
 * @Version: 1.0
 */
public interface SendMailService {
    /**
     * 发送邮件
     *
     * @param title       邮件标题
     * @param acceptEmail 接收邮件的邮箱地址
     * @param context     邮件内容
     */
    void sendMail(String title, String acceptEmail, String context);

}
