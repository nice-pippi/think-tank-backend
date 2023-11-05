package com.thinktank.post.service;

import com.thinktank.generator.entity.PostScore;

/**
 * @Author: 弘
 * @CreateTime: 2023年11⽉05⽇ 19:45
 * @Description: 评分业务接口
 * @Version: 1.0
 */
public interface ScoreService {
    /**
     * 帖子评分
     *
     * @param postScore
     */
    Integer score(PostScore postScore);

    /**
     * 获取当前用户对帖子的评分
     *
     * @param postId
     * @return
     */
    Integer getScore(Long postId);
}
