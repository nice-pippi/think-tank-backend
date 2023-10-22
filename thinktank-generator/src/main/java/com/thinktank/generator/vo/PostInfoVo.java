package com.thinktank.generator.vo;

import com.thinktank.generator.entity.PostInfo;
import lombok.Data;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉21⽇ 19:16
 * @Description: 帖子信息vo
 * @Version: 1.0
 */
@Data
public class PostInfoVo extends PostInfo {
    // 板块名称
    private String blockName;

    // 帖子内容
    private String content;

    // 帖子图片（最多只取5张）
    private List<String> images;

    // 用户名
    private String username;
}
