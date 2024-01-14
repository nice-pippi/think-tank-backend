package com.thinktank.post.mq;

import com.thinktank.common.utils.RabbitMQUtil;
import com.thinktank.generator.entity.PostClickRecord;
import com.thinktank.generator.entity.PostInfo;
import com.thinktank.generator.mapper.PostClickRecordMapper;
import com.thinktank.generator.mapper.PostInfoMapper;
import com.thinktank.post.config.AddPostClickRecordFanoutConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: 弘
 * @CreateTime: 2024年01⽉14⽇ 16:44
 * @Description: 帖子点赞记录消费者
 * @Version: 1.0
 */
@Slf4j
@Component
public class PostClickRecordListener {
    @Autowired
    private PostInfoMapper postInfoMapper;

    @Autowired
    private PostClickRecordMapper postClickRecordMapper;

    @Transactional
    @RabbitListener(queues = AddPostClickRecordFanoutConfig.Queue_Name)
    public void addPostClickRecord(Message message) {
        PostClickRecord postClickRecord = RabbitMQUtil.getObject(message, PostClickRecord.class);

        PostInfo postInfo = postInfoMapper.selectById(postClickRecord.getPostId());

        if (postInfo == null) {
            log.error("当前帖子不存在，停止写入帖子点击记录表操作");
            return;
        }

        postClickRecord.setTitle(postInfo.getTitle());
        if (postClickRecordMapper.insert(postClickRecord) == 0) {
            log.error("当前帖子点击记录写入失败，帖子id:{}", postClickRecord.getPostId());
        }
    }
}
