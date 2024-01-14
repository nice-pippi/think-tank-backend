package com.thinktank.search.service.impl;

import com.thinktank.search.doc.PostInfoDoc;
import com.thinktank.search.service.PostInfoDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉21⽇ 19:22
 * @Description: 板块信息文档管理业务接口实现类
 * @Version: 1.0
 */
@Component
public class PostInfoDocServiceImpl implements PostInfoDocService {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public void deletePostInfoDoc(Long id) {
        elasticsearchRestTemplate.delete(id.toString(), PostInfoDoc.class);
    }
}
