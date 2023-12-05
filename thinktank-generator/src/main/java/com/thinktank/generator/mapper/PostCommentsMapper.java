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
    IPage<PostCommentsVo> getPage(Page<PostCommentsVo> page, @Param("postId") Long postId);

    List<PostCommentsVo> getAllChildrenComment(Long commentId);

    List<PostCommentsVo> getPostCommentsVo(Long postId);

    IPage<PostCommentsVo> receivedCommentsPage(Page<PostComments> page, @Param("loginId") Long loginId);
}
