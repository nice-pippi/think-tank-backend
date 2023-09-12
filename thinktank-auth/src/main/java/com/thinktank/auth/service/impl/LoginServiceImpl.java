package com.thinktank.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinktank.auth.service.LoginService;
import com.thinktank.auth.service.UserService;
import com.thinktank.common.utils.ObjectMapperUtil;
import com.thinktank.generator.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
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
    private UserService userService;

    @Autowired
    private LoginServiceImpl loginServiceProxy;

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
        SysUser sysUser = userService.addUser(userinfo);

        // 会话登录
        StpUtil.login(sysUser.getId());

        // 返回token给用户
        return StpUtil.getTokenValue();
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
        ObjectMapper mapper = new ObjectMapper();

        Map<String, String> map = ObjectMapperUtil.toObject(result, Map.class);
        return map;
    }

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
}
