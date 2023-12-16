package com.thinktank.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thinktank.generator.entity.PostComments;
import com.thinktank.generator.vo.PostCommentsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author pippi
 * @since 2023-10-27
 */
public interface PostCommentsMapper extends BaseMapper<PostComments> {
    /**
     * 帖子评论分页
     *
     * @param page   页码
     * @param postId 帖子ID
     * @return 分页结果
     */
    IPage<PostCommentsVo> getPage(Page<PostCommentsVo> page, @Param("postId") Long postId);

    /**
     * 获取指定评论的所有子评论列表
     *
     * @param commentId 评论ID
     * @return 指定评论的所有子评论列表
     */
    List<PostCommentsVo> getAllChildrenComment(Long commentId);

    /**
     * 根据帖子id获取该帖子前五条评论
     *
     * @param postId 帖子ID
     * @return 根据帖子id获取该帖子前五条评论
     */
    List<PostCommentsVo> getPostCommentsVoByFive(Long postId);

    /**
     * 获取指定登录用户收到的评论列表
     *
     * @param page    页码
     * @param loginId 登录用户ID
     * @return 分页结果
     */
    IPage<PostCommentsVo> receivedCommentsPage(Page<PostComments> page, @Param("loginId") Long loginId);

}
