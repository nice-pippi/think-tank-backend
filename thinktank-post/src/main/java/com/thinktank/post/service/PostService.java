package com.thinktank.post.service;

import com.thinktank.generator.dto.PostInfoDto;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉20⽇ 19:33
 * @Description: 帖子业务接口
 * @Version: 1.0
 */
public interface PostService {
    void publish(PostInfoDto postInfoDto);
}
