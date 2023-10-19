package com.thinktank.search.service;

import com.thinktank.common.utils.R;
import com.thinktank.search.doc.BlockInfoDoc;
import com.thinktank.search.dto.BlockInfoDocDto;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉18⽇ 20:11
 * @Description: 搜索业务接口
 * @Version: 1.0
 */
public interface SearchService {
    R<List<BlockInfoDoc>> searchBlock(BlockInfoDocDto blockInfoDocDto);
}
