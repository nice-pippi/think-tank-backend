package com.thinktank.post.job;

import com.thinktank.common.utils.ObjectMapperUtil;
import com.thinktank.generator.mapper.PostScoreMapper;
import com.thinktank.generator.vo.PostHotVo;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2024年01⽉03⽇ 14:21
 * @Description: 热门帖子更新任务
 * @Version: 1.0
 */
@Slf4j
@Component
public class HotPostJob {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private PostScoreMapper postScoreMapper;

    /**
     * 每小时更新一次热门帖子
     */
    @XxlJob("updateHotPost")
    public void updateHotPost() {
        log.info("热门帖子更新任务开始执行");
        String namespace = "post:hot";

        // 清空热门帖子缓存
        redisTemplate.delete(namespace);

        // 按照一定规则统计热门帖子
        List<PostHotVo> list = postScoreMapper.getHotPostByTop30();

        // 取top5写入缓存，用于首页显示
        redisTemplate.opsForValue().set(namespace + ":top5", ObjectMapperUtil.toJSON(list.subList(0, 5)));

        // 全部数据写入缓存
        redisTemplate.opsForValue().set(namespace + ":top30", ObjectMapperUtil.toJSON(list));
        log.info("热门帖子更新任务执行完毕");
    }
}
