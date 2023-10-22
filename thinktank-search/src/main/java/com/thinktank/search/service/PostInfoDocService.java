package com.thinktank.search.service;

import com.thinktank.generator.vo.PostInfoVo;
import com.thinktank.search.doc.PostInfoDoc;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉21⽇ 19:22
 * @Description: 板块信息文档管理业务接口
 * @Version: 1.0
 */
public interface PostInfoDocService {
    /**
     * 添加帖子信息文档
     *
     * @param id
     * @return
     */
    PostInfoDoc addPostInfoDoc(Long id);
}
