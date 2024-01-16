package com.thinktank.post.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thinktank.common.utils.R;
import com.thinktank.generator.dto.PostInfoDto;
import com.thinktank.generator.entity.PostClickRecords;
import com.thinktank.generator.entity.PostInfo;
import com.thinktank.generator.vo.PostHotVo;
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
     * @param postInfoDto 发布帖子所需信息
     */
    void publish(PostInfoDto postInfoDto);

    /**
     * 删除帖子
     *
     * @param postId 帖子ID
     */
    void delete(Long postId);

    /**
     * 据基于用户的协同过滤算法推荐帖子
     *
     * @return 推荐帖子列表
     */
    List<PostInfoVo> getRecommendedPostsByCollaborativeFiltering();

    /**
     * 首页大厅获取最新帖子
     *
     * @return 最新帖子列表
     */
    List<PostInfoVo> getLatestPosts();

    /**
     * 当前板块下帖子分页查询
     *
     * @param postInfoDto 查询帖子信息
     * @return 当前板块下帖子分页
     */
    R<List<PostInfoVo>> page(PostInfoDto postInfoDto);

    /**
     * 根据帖子id获取帖子标题
     *
     * @param postId 帖子ID
     * @return 帖子标题
     */
    PostInfo getTitleAndTag(Long postId);

    /**
     * 根据用户id获取已发布的帖子
     *
     * @param id          用户ID
     * @param currentPage 当前页面
     * @return 已发布的帖子列表
     */
    R<List<PostInfoVo>> getPageByPublishedPosts(Long id, Integer currentPage);

    /**
     * 收藏帖子
     *
     * @param postId 帖子ID
     */
    void addFavoritePost(Long postId);

    /**
     * 验证当前登录用户是否收藏了当前帖子
     *
     * @param postId 帖子ID
     * @return 收藏状态，true表示已收藏，false表示未收藏
     */
    Boolean isFavorite(Long postId);

    /**
     * 移除收藏的帖子
     *
     * @param postId 帖子ID
     */
    void removeFavoritePost(Long postId);


    /**
     * 分页查询用户收藏的帖子
     *
     * @param userId      用户ID
     * @param currentPage 当前页码
     * @return 用户收藏的帖子分页查询结果
     */
    IPage<PostInfo> getFavoritePage(Long userId, Integer currentPage);

    /**
     * 获取热门帖子top5
     *
     * @return 热门帖子列表
     */
    List<PostHotVo> getHotPostByTop5();

    /**
     * 获取热门帖子top30
     *
     * @return 热门帖子列表
     */
    List<PostHotVo> getHotPostByTop30();

    /**
     * 新增点击帖子记录
     *
     * @param postClickRecords 点击帖子记录信息
     */
    void addPostClickRecord(PostClickRecords postClickRecords);
}
