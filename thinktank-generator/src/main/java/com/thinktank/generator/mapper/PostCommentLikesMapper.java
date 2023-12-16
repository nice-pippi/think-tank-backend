package com.thinktank.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thinktank.generator.entity.PostCommentLikes;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author pippi
 * @since 2023-11-04
 */
public interface PostCommentLikesMapper extends BaseMapper<PostCommentLikes> {

    /**
     * 插入多个评论点赞记录
     *
     * @param id        评论点赞ID
     * @param commentId 评论ID
     * @param userList  用户ID列表
     */
    void insertBatch(@Param("id") Long id, @Param("commentId") String commentId, @Param("userList") List<Long> userList);
}
