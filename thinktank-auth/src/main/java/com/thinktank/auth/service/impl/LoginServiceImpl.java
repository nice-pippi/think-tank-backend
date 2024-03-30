package com.thinktank.auth.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thinktank.auth.config.AddUserLoginRecordsFanoutConfig;
import com.thinktank.auth.service.AddUserService;
import com.thinktank.auth.service.LoginService;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.common.utils.ObjectMapperUtil;
import com.thinktank.common.utils.R;
import com.thinktank.common.utils.RabbitMQUtil;
import com.thinktank.generator.entity.SysLoginRecords;
import com.thinktank.generator.entity.SysUser;
import com.thinktank.generator.mapper.SysRoleMenuMapper;
import com.thinktank.generator.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉08⽇ 15:08
 * @Description: 登录管理接口实现类
 * @Version: 1.0
 */
@RefreshScope
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AddUserService addUserService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${weixin.appid}")
    private String appid;

    @Value("${weixin.secret}")
    private String secret;

    @Override
    public String wxLogin(String code, String state) {
        Map<String, String> map = getAccessToken(code); // 获取access_token

        // 获取用户信息
        String accessToken = map.get("access_token");
        String openid = map.get("openid");
        Map<String, String> userinfo = getUserinfo(accessToken, openid);

        // 添加到数据库
        SysUser sysUser = addUserService.addUser(userinfo);

        if (sysUser.getStatus().equals(1)) {
            log.error("用户{}已被限制登录", sysUser.getId());
            throw new ThinkTankException("您已被限制登录！");
        }

        // 会话登录
        StpUtil.login(sysUser.getId().toString());

        // 将用户登录记录发送到队列，由队列进行异步写入数据库操作
        sendLoginRecordToMQ(sysUser.getId(), 1, 1, null);

        // 根据获取用户所有权限
        List<String> permissionList = getPermissionList(sysUser.getId());

        // 返回token以及权限码给用户
        return String.format("?token=%s&permissions=%s", StpUtil.getTokenValue(), permissionList);
    }

    @Override
    public R<String> passwordLogin(SysUser sysUser) {
        // 查询该邮箱是否已存在
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getEmail, sysUser.getEmail());
        SysUser user = validateUser(sysUser, wrapper);

        // 将用户登录记录发送到队列，由队列进行异步写入数据库操作
        sendLoginRecordToMQ(user.getId(), 0, 1, null);

        // 根据获取用户所有权限
        List<String> permissionList = getPermissionList(user.getId());

        // 返回用户token以及权限码
        return R.success(StpUtil.getTokenValue()).add("permissions", permissionList);
    }

    @Override
    public void logout() {
        StpUtil.checkLogin();
        StpUtil.logout();
    }

    // 根据用户id获取用户所有权限
    private List<String> getPermissionList(Long id) {
        // 1.获取用户所有权限
        List<String> permissionList = sysRoleMenuMapper.getPermissionList(id);

        // 将用户权限放入redis缓存中，命名空间为gateway:permissions: + id，减少网关查询权限再次查询数据库的操作
        // 1.设置redis命名空间
        String namespace = "gateway:permissions:" + id;

        // 2.写入redis缓存，以hash形式存储
        HashOperations<String, Object, Object> ops = redisTemplate.opsForHash();
        ops.put(namespace, "permissionList", ObjectMapperUtil.toJSON(permissionList));
        return permissionList;
    }

    @Override
    public String adminLogin(SysUser sysUser) {
        // 查询账号是否存在
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getAccount, sysUser.getAccount());
        validateUser(sysUser, wrapper);
        return StpUtil.getTokenValue();
    }

    /**
     * 校验用户是否存在
     *
     * @param sysUser 用户信息
     * @param wrapper 查询条件
     * @return
     */
    private SysUser validateUser(SysUser sysUser, LambdaQueryWrapper<SysUser> wrapper) {
        SysUser user = sysUserMapper.selectOne(wrapper);

        if (user == null) {
            throw new ThinkTankException("账号有误！");
        }

        // 将查询到的密码与用户提交的密码做匹配
        // 1.密码原文直接匹配
        if (user.getPassword().equals(sysUser.getPassword())) {
            // 为该用户创建会话登录
            StpUtil.login(user.getId().toString());
            return user;
        }
        // 2.BCrypt加密匹配
        boolean result = BCrypt.checkpw(sysUser.getPassword(), user.getPassword());

        // 如果BCrypt加密匹配为false，抛出异常提示用户账号或密码错误
        if (!result) {
            // 将用户登录记录发送到队列，由队列进行异步写入数据库操作
            sendLoginRecordToMQ(user.getId(), 0, 0, "账号或密码错误");
            throw new ThinkTankException("账号或密码错误！");
        }

        // 检查用户是否被限制登录
        if (user.getStatus().equals(1)) {
            // 将用户登录记录发送到队列，由队列进行异步写入数据库操作
            sendLoginRecordToMQ(user.getId(), 0, 0, "已被限制登录");
            log.error("用户{}已被限制登录", sysUser.getId());
            throw new ThinkTankException("您已被限制登录！");
        }

        // 为该用户创建会话登录
        StpUtil.login(user.getId().toString());
        return user;
    }

    /**
     * 根据code请求微信接口获取access_token
     * 返回结果如下
     * {
     * "access_token":"ACCESS_TOKEN",
     * "expires_in":7200,
     * "refresh_token":"REFRESH_TOKEN",
     * "openid":"OPENID",
     * "scope":"SCOPE",
     * "unionid": "UNIONID"
     * }
     *
     * @param code
     * @return
     */
    private Map<String, String> getAccessToken(String code) {
        // 请求微信地址
        String wxUrlTemplate = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
        String wxUrl = String.format(wxUrlTemplate, appid, secret, code);

        log.info("调用微信接口申请access_token, url:{}", wxUrl);

        // 获取微信接口返回的access_token
        String result = restTemplate.exchange(wxUrl, HttpMethod.POST, null, String.class).getBody();

        log.info("调用微信接口申请access_token: 返回值:{}", result);

        Map<String, String> map = ObjectMapperUtil.toObject(result, Map.class);
        return map;
    }

    /**
     * 根据access_token和openid请求微信接口获取用户信息
     *
     * @param access_token 用户的access_token
     * @param openid       用户的openid
     * @return 用户信息的Map集合
     */
    private Map<String, String> getUserinfo(String access_token, String openid) {
        // 请求微信地址
        String wxUrlTemplate = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
        String wxUrl = String.format(wxUrlTemplate, access_token, openid);

        log.info("调用微信接口申请access_token, url:{}", wxUrl);

        // 获取微信接口返回的用户信息
        String result = restTemplate.exchange(wxUrl, HttpMethod.POST, null, String.class).getBody();

        //防止乱码进行转码
        String newResult = new String(result.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

        log.info("调用微信接口申请access_token: 返回值:{}", newResult);

        Map<String, String> map = ObjectMapperUtil.toObject(newResult, Map.class);
        return map;
    }


    /**
     * 将用户登录记录发送到队列，由队列进行异步写入数据库操作
     *
     * @param userId    用户id
     * @param loginType 登录类型
     * @param status    登录状态（0:登录失败 1:登录成功）
     */
    private void sendLoginRecordToMQ(Long userId, Integer loginType, Integer status, String reason) {
        // 创建用户登录记录对象
        SysLoginRecords sysLoginRecords = new SysLoginRecords();
        sysLoginRecords.setUserId(userId);
        sysLoginRecords.setLoginType(loginType);
        sysLoginRecords.setStatus(status);
        if (StringUtils.isNotEmpty(reason)) {
            sysLoginRecords.setReason(reason);
        }

        // 获取CorrelationData对象
        CorrelationData correlationData = RabbitMQUtil.getCorrelationData();
        // 将PostClickRecord对象转换为Message对象
        Message message = RabbitMQUtil.transformMessage(sysLoginRecords);

        // 发送消息
        rabbitTemplate.convertAndSend(AddUserLoginRecordsFanoutConfig.FANOUT_EXCHANGE, "", message, correlationData);
    }

}
