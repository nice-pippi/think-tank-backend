package com.thinktank.gateway.handler;

import cn.dev33.satoken.stp.StpInterface;
import com.thinktank.common.utils.ObjectMapperUtil;
import com.thinktank.generator.mapper.SysRoleMenuMapper;
import com.thinktank.generator.mapper.SysUserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉07⽇ 16:56
 * @Description: 自定义权限验证接口扩展
 * @Version: 1.0
 */
@Slf4j
@Component
public class StpInterfaceImpl implements StpInterface {
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取权限列表
     *
     * @param loginId
     * @param loginType
     * @return
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 从缓存中查询权限集合
        HashOperations ops = redisTemplate.opsForHash();
        Object o = ops.get(loginId, "permissionList");

        // 若缓存中存在则直接返回
        if (o != null) {
            List<String> list = ObjectMapperUtil.toObject(o.toString(), List.class);
            log.info("id={},permissionList={}", loginId, list);
            return list;
        }

        List<String> permissionList = sysRoleMenuMapper.getPermissionList(loginId);
        log.info("id={},permissionList={}", loginId, permissionList);

        // 写入redis缓存
        ops.put(loginId, "permissionList", ObjectMapperUtil.toJSON(permissionList));
        return permissionList;
    }

    /**
     * 获取角色列表
     *
     * @param loginId
     * @param loginType
     * @return
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 从缓存中查询权限集合
        HashOperations ops = redisTemplate.opsForHash();
        Object o = ops.get(loginId, "roleList");

        // 若缓存中存在则直接返回
        if (o != null) {
            List<String> list = ObjectMapperUtil.toObject(o.toString(), List.class);
            log.info("id={},roleList={}", loginId, list);
            return list;
        }

        // 从缓存中查询角色集合
        List<String> roleList = sysUserRoleMapper.getRoleList(loginId);
        log.info("id={},roleList={}", loginId, roleList);

        // 写入redis缓存
        ops.put(loginId, "roleList", ObjectMapperUtil.toJSON(roleList));
        return roleList;
    }
}
