package com.thinktank.search.service.impl;

import com.thinktank.generator.entity.BlockInfo;
import com.thinktank.generator.mapper.BlockInfoMapper;
import com.thinktank.search.doc.BlockInfoDoc;
import com.thinktank.search.service.BlockInfoDocService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉18⽇ 19:01
 * @Description: 板块信息文档管理业务接口实现类
 * @Version: 1.0
 */
@Service
public class BlockInfoDocServiceImpl implements BlockInfoDocService {
    @Autowired
    private BlockInfoMapper blockInfoMapper;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public BlockInfoDoc addBlockInfoDoc(Long id) {
        // 获取板块信息
        BlockInfo blockInfo = blockInfoMapper.selectById(id);

        // 将板块信息拷贝到板块信息文档
        BlockInfoDoc blockInfoDoc = new BlockInfoDoc();
        BeanUtils.copyProperties(blockInfo, blockInfoDoc);

        // 保存到es文档
        blockInfoDoc = elasticsearchRestTemplate.save(blockInfoDoc);
        return blockInfoDoc;
    }
}
