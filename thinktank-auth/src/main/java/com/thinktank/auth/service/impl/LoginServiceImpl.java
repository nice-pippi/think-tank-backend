package com.thinktank.auth.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thinktank.auth.service.AddUserService;
import com.thinktank.auth.service.LoginService;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.common.utils.ObjectMapperUtil;
import com.thinktank.common.utils.R;
import com.thinktank.generator.entity.SysUser;
import com.thinktank.generator.mapper.SysRoleMenuMapper;
import com.thinktank.generator.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
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

        // 会话登录
        StpUtil.login(sysUser.getId().toString());

        // 根据获取用户所有权限
        List<String> permissionList = getPermissionList(sysUser.getId());

        // 返回token以及权限码给用户
        String base = String.format("?token=%s&permissions=%s", StpUtil.getTokenValue(), permissionList);
        return base;
    }

    @Override
    public R<String> passwordLogin(SysUser sysUser) {
        // 查询该邮箱是否已存在
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getEmail, sysUser.getEmail());
        SysUser user = validateUser(sysUser, wrapper);

        // 根据获取用户所有权限
        List<String> permissionList = getPermissionList(user.getId());

        // 返回用户token以及权限码
        return R.success(StpUtil.getTokenValue()).add("permissions", permissionList);
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

    // 校验用户是否存在
    private SysUser validateUser(SysUser sysUser, LambdaQueryWrapper<SysUser> wrapper) {
        SysUser user = sysUserMapper.selectOne(wrapper);

        if (user == null) {
            throw new ThinkTankException("账号有误！");
        }

        // 将查询到的密码与用户提交的密码做匹配
        boolean result = BCrypt.checkpw(sysUser.getPassword(), user.getPassword());

        // 如果为false，抛出异常提示用户账号或密码错误
        if (!result) {
            throw new ThinkTankException("账号或密码错误！");
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
