package com.thinktank.post.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.core.type.TypeReference;
import com.thinktank.common.utils.ObjectMapperUtil;
import com.thinktank.generator.entity.PostCommentLikes;
import com.thinktank.generator.mapper.PostCommentLikesMapper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: 弘
 * @CreateTime: 2023年11⽉04⽇ 15:26
 * @Description: 处理评论点赞任务
 * @Version: 1.0
 */
@Slf4j
@Component
public class CommentLikeJob {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private PostCommentLikesMapper postCommentLikesMapper;

    /**
     * 每天0点将redis中用户点赞记录更新到数据库
     */
    @Transactional
    @XxlJob("writeCommentLikesToDB")
    public void writeCommentLikesToDB() {
        log.info("更新点赞记录帖子任务开始执行");
        String namespace = "post:comment:like";
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
        // 获取指定命名空间下的所有Hash键值对
        Map<String, Object> entries = ops.entries(namespace);

        for (Map.Entry<String, Object> entry : entries.entrySet()) {
            // 评论id
            String commentId = entry.getKey();

            // 该评论id下所有点赞的用户id
            List<Long> userList = ObjectMapperUtil.toObjectByTypeReference(entry.getValue().toString(), new TypeReference<List<Long>>() {
            });

            // 取出该评论的所有点赞用户id
            LambdaQueryWrapper<PostCommentLikes> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PostCommentLikes::getCommentId, commentId);
            queryWrapper.select(PostCommentLikes::getUserId);
            List<Long> collect = postCommentLikesMapper.selectList(queryWrapper)
                    .stream().map(PostCommentLikes::getUserId).collect(Collectors.toList());

            // 求差集，从缓存中取出数据库中没有的用户id
            List<Long> difference = userList.stream().filter(item -> !collect.contains(item)).collect(Collectors.toList());

            // 如果difference列表的大小大于0，则使用批量插入操作将difference中的用户ID插入到数据库中
            if (difference.size() > 0) {
                // 使用批量插入操作插入差异列表中的用户id
                postCommentLikesMapper.insertBatch(IdWorker.getId(), commentId, difference);
            }

            // 求差集，从数据库中取出缓存中没有的用户id，这里的差集代表要删除的用户点赞记录
            List<Long> difference2 = collect.stream().filter(item -> !userList.contains(item)).collect(Collectors.toList());

            // 如果difference2列表的大小大于0，则删除这些用户的点赞记录
            if (difference2.size() > 0) {
                LambdaQueryWrapper<PostCommentLikes> queryWrapper2 = new LambdaQueryWrapper<>();
                queryWrapper2.eq(PostCommentLikes::getCommentId, commentId);
                queryWrapper2.in(PostCommentLikes::getUserId, difference2);
                postCommentLikesMapper.delete(queryWrapper2);
            }
        }
        log.info("更新点赞记录帖子任务执行结束");
    }
}
