package com.thinktank.gateway.handler;

import cn.dev33.satoken.stp.StpInterface;
import com.thinktank.generator.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉07⽇ 16:56
 * @Description: 自定义权限验证接口扩展
 * @Version: 1.0
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {

        ArrayList<String> list = new ArrayList<>();
        list.add("ad");
        // 返回此 loginId 拥有的权限列表
        return list;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        ArrayList<String> list = new ArrayList<>();
        list.add("ad");
        return list;
    }

}
