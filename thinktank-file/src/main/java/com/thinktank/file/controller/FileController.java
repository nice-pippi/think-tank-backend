package com.thinktank.file.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.thinktank.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉15⽇ 15:48
 * @Description: 文件上传
 * @Version: 1.0
 */
@Api("文件上传接口")
@RestController
public class FileController {
    @SaCheckLogin
    @ApiOperation(("上传头像"))
    @PostMapping("avatar")
    public R<String> avatar(MultipartFile file) {
        System.out.println(file);
        return R.success(null);
    }

    @SaCheckLogin
    @ApiOperation(("上传帖子图片"))
    @PostMapping("postImg")
    public R<String> postImg(MultipartFile file) {
        System.out.println(file);
        return R.success(null);
    }
}
