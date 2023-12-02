package com.thinktank.search.service;

import com.thinktank.common.utils.R;
import com.thinktank.search.doc.BlockInfoDoc;
import com.thinktank.search.doc.PostInfoDoc;
import com.thinktank.search.dto.BlockInfoDocDto;
import com.thinktank.search.dto.PostInfoDocDto;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉18⽇ 20:11
 * @Description: 搜索业务接口
 * @Version: 1.0
 */
public interface SearchService {
    /**
     * 搜索板块
     *
     * @param blockInfoDocDto 板块信息数据传输对象
     * @return 板块文档信息列表
     */
    R<List<BlockInfoDoc>> searchBlock(BlockInfoDocDto blockInfoDocDto);

    /**
     * 搜索帖子
     *
     * @param postInfoDocDto 帖子信息数据传输对象
     * @return 帖子文档信息列表
     */
    R<List<PostInfoDoc>> searchPost(PostInfoDocDto postInfoDocDto);
}
