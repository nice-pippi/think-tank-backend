package com.thinktank.file.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉16⽇ 12:02
 * @Description: 文件管理接口接口
 * @Version: 1.0
 */
public interface FileService {
    /**
     * 上传用户头像
     *
     * @param file 用户头像文件
     * @return 用户头像地址
     */
    String uploadUserAvatar(MultipartFile file);

    /**
     * 上传板块头像
     *
     * @param file 板块头像文件
     * @param id   板块ID
     * @return 板块头像地址
     */
    String uploadBlockAvatar(MultipartFile file, Long id);


    /**
     * 上传帖子图片
     *
     * @param file 帖子图片文件
     * @return 帖子图片地址
     */
    String uploadPostImg(MultipartFile file);
}
