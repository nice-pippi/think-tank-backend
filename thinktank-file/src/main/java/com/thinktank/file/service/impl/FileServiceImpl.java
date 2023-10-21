package com.thinktank.file.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.thinktank.api.clients.UserClient;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.common.utils.R;
import com.thinktank.file.service.FileService;
import com.thinktank.generator.dto.SysUserDto;
import com.thinktank.generator.entity.SysUser;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉16⽇ 12:03
 * @Description: 文件管理接口接口实现类
 * @Version: 1.0
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private UserClient userClient;

    @Autowired
    private MinioClient minioClient;

    @Override
    public String uploadUserAvatar(MultipartFile file) {
        // 根据用户id，远程调用auth服务的用户查询接口
        long id = StpUtil.getLoginIdAsLong();
        R<SysUser> result = userClient.getUserInfo(id);

        // 如果状态不为200，则抛出异常
        if (result.getStatus() != 200) {
            throw new ThinkTankException(result.getMsg());
        }

        // 用户信息
        SysUser sysUser = result.getData();

        // 桶
        String bucket = "user-avatar";

        // 文件存储路径
        String object = String.format("/%s/%s", sysUser.getId(), file.getOriginalFilename());

        // 将图片文件以流的形式上传至minio
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(object)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());
        } catch (Exception e) {
            log.error("用户头像上传minio失败：用户id:{}", sysUser.getId());
            throw new ThinkTankException("上传minio失败");
        }

        // 将该图片地址替换用户信息原头像地址
        String avatarPath = "/" + bucket + object;
        sysUser.setAvatar(avatarPath);

        // 远程调用auth服务的用户更改接口
        SysUserDto sysUserDto = new SysUserDto();
        BeanUtils.copyProperties(sysUser, sysUserDto);
        R<SysUser> updateResult = userClient.updateUser(sysUserDto);

        if (updateResult.getStatus() != 200) {
            log.error("头像更改失败，用户id:{}", sysUserDto.getId());
            throw new ThinkTankException(updateResult.getMsg());
        }

        // 返回用户头像地址
        return updateResult.getData().getAvatar();
    }

    @Override
    public String uploadBlockAvatar(MultipartFile file, Long id) {
        // 桶
        String bucket = "block-avatar";

        // 文件存储路径
        String object = String.format("/%s/%s", id, file.getOriginalFilename());

        // 将图片文件以流的形式上传至minio
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(object)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());
        } catch (Exception e) {
            log.error("板块头像上传minio失败，板块id:{}，文件:{}", id, file.getOriginalFilename());
            throw new ThinkTankException("上传minio失败");
        }

        // 返回板块头像地址
        String avatarPath = "/" + bucket + object;
        return avatarPath;
    }

    @Override
    public String uploadPostImg(MultipartFile file) {
        // 桶
        String bucket = "post-image";

        // 文件存储路径
        String object = String.format("/%s", UUID.randomUUID());

        // 将图片文件以流的形式上传至minio
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(object)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());
        } catch (Exception e) {
            log.error("帖子上传minio失败，文件:{}", file.getOriginalFilename());
            throw new ThinkTankException("上传minio失败");
        }

        // 返回板块头像地址
        String avatarPath = "/" + bucket + object;
        return avatarPath;
    }
}
