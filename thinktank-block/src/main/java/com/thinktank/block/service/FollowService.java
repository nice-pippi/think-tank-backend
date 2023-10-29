package com.thinktank.block.service;

import com.thinktank.generator.entity.BlockInfo;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉20⽇ 14:17
 * @Description: 关注板块业务接口
 * @Version: 1.0
 */
public interface FollowService {
    /**
     * 关注板块
     */
    void followBlock(Long id);

    /**
     * 根据用户id获取所有已关注板块
     *
     * @return
     */
    List<BlockInfo> getAllFollow(Long id);
}
