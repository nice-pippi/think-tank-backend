package com.thinktank.search.service;

import org.springframework.amqp.core.Message;

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
     * @param message
     * @return
     */
    void addPostInfoDoc(Message message);

    /**
     * 删除帖子信息文档
     *
     * @param id 帖子ID
     */
    void deletePostInfoDoc(Long id);
}
