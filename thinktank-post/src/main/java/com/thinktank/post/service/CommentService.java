package com.thinktank.post.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thinktank.generator.entity.PostCommentLikes;
import com.thinktank.generator.entity.PostComments;
import com.thinktank.generator.vo.PostCommentsVo;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉23⽇ 18:32
 * @Description: 评论业务接口
 * @Version: 1.0
 */
public interface CommentService {

    /**
     * 帖子评论分页
     *
     * @param postId      帖子ID
     * @param currentPage 当前页码
     * @return 分页结果
     */
    IPage<PostCommentsVo> page(Long postId, Integer currentPage);

    /**
     * 对帖子发表评论
     *
     * @param postComments 评论内容
     */
    void replyPost(PostComments postComments);

    /**
     * 回复评论
     *
     * @param postComments 回复的评论
     * @return 回复后的评论
     */
    PostComments replyComment(PostComments postComments);

    /**
     * 评论点赞
     *
     * @param postCommentLikes 点赞评论信息
     */
    void addLikeComment(PostCommentLikes postCommentLikes);

    /**
     * 取消评论点赞
     *
     * @param postCommentLikes 点赞评论信息
     */
    void removeLikeComment(PostCommentLikes postCommentLikes);
}
