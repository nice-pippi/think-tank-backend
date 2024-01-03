package com.thinktank.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thinktank.generator.entity.PostScore;
import com.thinktank.generator.vo.PostHotVo;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author pippi
 * @since 2023-10-21
 */
public interface PostScoreMapper extends BaseMapper<PostScore> {
    /**
     * 获取热门帖子top5
     *
     * @return
     */
    List<PostHotVo> getHotPostByTop5();

    /**
     * 获取热门帖子top30
     *
     * @return
     */
    List<PostHotVo> getHotPostByTop30();
}
