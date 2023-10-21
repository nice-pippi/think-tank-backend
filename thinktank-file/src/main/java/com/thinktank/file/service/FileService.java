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
     * @param file
     * @return
     */
    String uploadUserAvatar(MultipartFile file);

    /**
     * 上传板块头像
     *
     * @param file
     * @param id
     * @return
     */
    String uploadBlockAvatar(MultipartFile file, Long id);


    /**
     * 上传帖子图片
     *
     * @param file
     * @return
     */
    String uploadPostImg(MultipartFile file);
}
