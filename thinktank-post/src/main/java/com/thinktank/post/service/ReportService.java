package com.thinktank.post.service;

import com.thinktank.generator.entity.PostReports;

/**
 * @Author: 弘
 * @CreateTime: 2023年11⽉06⽇ 17:16
 * @Description: 举报业务接口
 * @Version: 1.0
 */
public interface ReportService {
    /**
     * 举报帖子/评论
     *
     * @param postReports 要举报的帖子/评论
     */
    void report(PostReports postReports);
}
