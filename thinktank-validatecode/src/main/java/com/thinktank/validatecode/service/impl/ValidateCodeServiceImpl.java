package com.thinktank.validatecode.service.impl;

import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.common.utils.R;
import com.thinktank.common.utils.ValidateCodeUtil;
import com.thinktank.validatecode.service.SendMailService;
import com.thinktank.validatecode.service.ValidateCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉10⽇ 15:18
 * @Description: 生成验证码、验证验证码接口实现类
 * @Version: 1.0
 */
@Slf4j
@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SendMailService sendMailService;

    @Override
    public void generateCode(String email) {
        // redis命名空间
        String namespace = "validatecode:" + email;
        ValueOperations ops = redisTemplate.opsForValue();

        // 从redis中验证是否存在当前邮箱的key
        if (ops.get(namespace) != null) {
            // 若已存在查看当前生命周期是否大于等于240，若是，则代表获取验证码间隔没到1分钟
            if (redisTemplate.getExpire(namespace) >= 240) {
                log.error("获取验证码间隔未到1分钟,邮箱：{}", email);
                throw new ThinkTankException("每次发送验证码间隔为1分钟，请稍后再发送验证码！");
            }
        }

        // 生成验证码
        Integer validateCode = ValidateCodeUtil.generateValidateCode(6);

        // 邮箱内容
        String context = "验证码为：" + validateCode + "，请在5分钟内完成验证！";

        // 将该验证码发送到指定邮箱
        sendMailService.sendMail("注册验证码", email, context);
        log.info("邮箱：{}，验证码：{}", email, validateCode);

        // 将该邮箱作为key，和生成的验证码做绑定，存入redis中，并且生命周期设置为5分钟
        ops.set(namespace, validateCode, Duration.ofMinutes(5));
    }

    @Override
    public void validateCode(String email, String validateCode) {
        // redis命名空间
        String namespace = "validatecode:" + email;
        ValueOperations ops = redisTemplate.opsForValue();

        Object redisCode = ops.get(namespace);
        // 若为空代表没有查到该key，告诉用户重新发送验证码
        if (redisCode == null) {
            throw new ThinkTankException("请重新发送验证码！");
        }

        // 将用户提供的验证码和该key对应的验证码做匹配
        if (!validateCode.equals(redisCode.toString())) {
            throw new ThinkTankException("验证码错误！");
        }

        // 删除redis中该邮箱的记录
        redisTemplate.delete(namespace);
    }
}
