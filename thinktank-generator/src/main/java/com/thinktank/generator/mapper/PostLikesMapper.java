package com.thinktank.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thinktank.generator.entity.PostInfo;
import com.thinktank.generator.entity.PostLikes;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author pippi
 * @since 2023-12-02
 */
public interface PostLikesMapper extends BaseMapper<PostLikes> {

    /**
     * 根据用户ID获取用户收藏页
     *
     * @param page   分页对象
     * @param userId 用户ID
     * @return 分页结果
     */
    IPage<PostInfo> getFavoritePage(Page<PostInfo> page, @Param("userId") Long userId);
}
