package com.thinktank.file.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉16⽇ 12:02
 * @Description: 文件管理接口接口
 * @Version: 1.0
 */
public interface FileService {

    String uploadUserAvatar(MultipartFile file);
}
