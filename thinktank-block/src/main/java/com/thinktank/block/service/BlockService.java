package com.thinktank.block.service;

import com.thinktank.block.dto.BlockClassifyDto;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉22⽇ 14:57
 * @Description: 板块接口
 * @Version: 1.0
 */
public interface BlockService {
    List<BlockClassifyDto> getBlockClassify();
}
