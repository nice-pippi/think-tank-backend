package com.thinktank.post.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉07⽇ 16:48
 * @Description: 类描述
 * @Version: 1.0
 */
@RestController
public class test {
    @SaCheckLogin
    @GetMapping("test")
    public String test(){
        return "true";
    }
}
