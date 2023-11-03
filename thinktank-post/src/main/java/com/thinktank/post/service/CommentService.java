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
     * @param postId
     * @param currentPage
     * @return
     */
    IPage<PostCommentsVo> page(Long postId, Integer currentPage);

    /**
     * 对帖子发表评论
     *
     * @param postComments
     */
    void replyPost(PostComments postComments);

    /**
     * 回复评论
     *
     * @param postComments
     * @return
     */
    PostComments replyComment(PostComments postComments);

    /**
     * 评论点赞
     *
     * @param postCommentLikes
     */
    void addLikeComment(PostCommentLikes postCommentLikes);

    /**
     * 取消评论点赞
     *
     * @param postCommentLikes
     */
    void removeLikeComment(PostCommentLikes postCommentLikes);
}
