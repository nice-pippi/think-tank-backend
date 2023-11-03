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

    /**
     * 首页大厅获取最新帖子
     *
     * @return
     */
    List<PostInfoVo> getLatestPosts();

    /**
     * 根据板块id获取当前板块下帖子分页
     *
     * @param postInfoDto
     * @return
     */
    R<List<PostInfoVo>> page(PostInfoDto postInfoDto);

    /**
     * 根据帖子id获取帖子标题
     *
     * @param postId
     * @return
     */
    String getTitle(Long postId);

    /**
     * 根据用户id获取已发布的帖子
     *
     * @param id
     * @param currentPage
     * @return
     */
    R<List<PostInfoVo>> getPageByPublishedPosts(Long id, Integer currentPage);
}
