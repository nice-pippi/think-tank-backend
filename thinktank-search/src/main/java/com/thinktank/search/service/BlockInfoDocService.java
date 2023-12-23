package com.thinktank.search.service;

import com.thinktank.generator.entity.BlockInfo;
import com.thinktank.search.doc.BlockInfoDoc;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉18⽇ 19:00
 * @Description: 板块信息文档管理业务接口
 * @Version: 1.0
 */
public interface BlockInfoDocService {
    /**
     * 添加板块信息文档
     *
     * @param blockInfo - 版块信息
     * @return 添加成功的板块信息文档
     */
    BlockInfoDoc addBlockInfoDoc(BlockInfo blockInfo);

    /**
     * 删除板块信息文档
     *
     * @param id - 板块信息文档ID
     */
    void deleteBlockInfoDoc(Long id);
}
