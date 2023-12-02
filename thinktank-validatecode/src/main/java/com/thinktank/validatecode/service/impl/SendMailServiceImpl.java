package com.thinktank.validatecode.service.impl;

import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.validatecode.service.SendMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉10⽇ 18:09
 * @Description: 发送邮箱接口
 * @Version: 1.0
 */
@Slf4j
@RefreshScope
@Service
public class SendMailServiceImpl implements SendMailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${mailInfo.from}")
    private String from;

    @Override
    public void sendMail(String title, String acceptEmail, String context) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);  // 发送人
        message.setTo(acceptEmail);  // 接收人电子邮箱
        message.setSubject(title);  // 邮件标题
        message.setText(context);   // 邮件内容
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            log.warn("邮箱不存在:{}", acceptEmail);
            throw new ThinkTankException("邮箱不存在！");
        }
    }
}
