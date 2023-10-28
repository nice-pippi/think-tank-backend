package com.thinktank.post.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thinktank.generator.entity.PostComments;
import com.thinktank.generator.vo.PostCommentsVo;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉23⽇ 18:32
 * @Description: 评论业务接口
 * @Version: 1.0
 */
public interface CommentService {
    IPage<PostCommentsVo> page(Long postId, Integer currentPage);

    void replyPost(PostComments postComments);
}
