package com.thinktank.post.mq;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thinktank.common.utils.RabbitMQUtil;
import com.thinktank.generator.entity.PostClickRecords;
import com.thinktank.generator.entity.PostInfo;
import com.thinktank.generator.mapper.PostClickRecordsMapper;
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
    private PostClickRecordsMapper postClickRecordsMapper;

    /**
     * 处理点击帖子记录
     *
     * @param message 消息内容
     */
    @Transactional
    @RabbitListener(queues = AddPostClickRecordFanoutConfig.Queue_Name)
    public void addPostClickRecord(Message message) {
        // 获取消息对象
        PostClickRecords postClickRecords = RabbitMQUtil.getObject(message, PostClickRecords.class);

        // 根据帖子ID获取帖子信息
        PostInfo postInfo = postInfoMapper.selectById(postClickRecords.getPostId());

        // 若帖子信息为空，则输出错误日志并结束写入操作
        if (postInfo == null) {
            log.error("当前帖子不存在，停止写入帖子点击记录表操作");
            return;
        }

        // 构造查询条件
        LambdaQueryWrapper<PostClickRecords> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostClickRecords::getBlockId, postClickRecords.getBlockId());
        queryWrapper.eq(PostClickRecords::getPostId, postClickRecords.getPostId());
        queryWrapper.eq(PostClickRecords::getUserId, postClickRecords.getUserId());

        // 查询点击记录
        PostClickRecords clickRecords = postClickRecordsMapper.selectOne(queryWrapper);

        // 若点击记录为空，则插入点击记录；否则更新点击次数
        if (clickRecords == null) {
            // 设置帖子标题
            postClickRecords.setTitle(postInfo.getTitle());
            postClickRecords.setTag(postInfo.getTag());
            postClickRecordsMapper.insert(postClickRecords);
        } else {
            clickRecords.setClickCount(clickRecords.getClickCount() + 1);
            clickRecords.setTag(postInfo.getTag());
            postClickRecordsMapper.updateById(clickRecords);
        }
    }

}
