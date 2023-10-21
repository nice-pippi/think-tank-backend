package com.thinktank.file.controller;

import com.thinktank.common.utils.R;
import com.thinktank.file.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private FileService fileService;

    @ApiOperation(("用户上传头像"))
    @PostMapping("userAvatar")
    public R<String> userAvatar(MultipartFile file) {
        return R.success(fileService.uploadUserAvatar(file));
    }

    @ApiOperation(("上传帖子图片"))
    @PostMapping("postImg")
    public R<String> postImg(MultipartFile file) {
        return R.success(fileService.uploadPostImg(file));
    }

    @ApiOperation(("上传板块头像"))
    @PostMapping("blockAvatar")
    public R<String> blockAvatar(@RequestParam MultipartFile file, Long id) {
        return R.success(fileService.uploadBlockAvatar(file, id));
    }
}
