package com.thinktank.generator.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thinktank.generator.dto.PostInfoDto;
import com.thinktank.generator.entity.PostInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thinktank.generator.vo.PostInfoVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pippi
 * @since 2023-09-11
 */
public interface PostInfoMapper extends BaseMapper<PostInfo> {
    IPage<PostInfoVo> page(Page<PostInfo> page, @Param("postInfoDto") PostInfoDto postInfoDto);
}
