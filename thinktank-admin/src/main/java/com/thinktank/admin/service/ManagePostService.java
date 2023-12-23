package com.thinktank.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thinktank.generator.dto.PostInfoDto;
import com.thinktank.generator.vo.PostInfoVo;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉23⽇ 15:51
 * @Description: 帖子管理业务接口
 * @Version: 1.0
 */
public interface ManagePostService {
    /**
     * 分页查询帖子信息
     *
     * @param postInfoDto 帖子信息查询条件
     * @return 分页查询板块
     */
    IPage<PostInfoVo> page(PostInfoDto postInfoDto);
}
