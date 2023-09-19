package com.thinktank.validatecode.controller;

import com.thinktank.common.utils.R;
import com.thinktank.validatecode.service.ValidateCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉10⽇ 17:05
 * @Description: 生成、校验验证码
 * @Version: 1.0
 */
@Api(tags = "验证码接口")
@RestController
public class ValidateCodeController {
    @Autowired
    private ValidateCodeService codeService;

    @ApiOperation("生成验证码")
    @PostMapping("generate")
    public R<String> generate(@RequestBody Map<String, String> map) {
        codeService.generateCode((map.get("email")));
        return R.success("已发送验证码到邮箱！");
    }

    @ApiOperation("校验验证码")
    @GetMapping("validate")
    public R<String> validate(@RequestParam("email") String email, @RequestParam("validateCode") String validateCode) {
        codeService.validateCode(email, validateCode);
        return R.success("校验成功！");
    }
}
