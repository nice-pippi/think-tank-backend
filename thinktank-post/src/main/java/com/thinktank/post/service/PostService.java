package com.thinktank.post.service;

import com.thinktank.common.utils.R;
import com.thinktank.generator.dto.PostInfoDto;
import com.thinktank.generator.vo.PostInfoVo;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉20⽇ 19:33
 * @Description: 帖子业务接口
 * @Version: 1.0
 */
public interface PostService {
    /**
     * 发布帖子
     *
     * @param postInfoDto
     */
    void publish(PostInfoDto postInfoDto);

    /**
     * 删除帖子
     *
     * @param postId
     */
    void delete(Long postId);

    List<PostInfoVo> getLatestPosts();

    R<List<PostInfoVo>> page(PostInfoDto postInfoDto);

    String getTitle(Long postId);

    R<List<PostInfoVo>> getPageByPublishedPosts(Long id, Integer currentPage);
}
