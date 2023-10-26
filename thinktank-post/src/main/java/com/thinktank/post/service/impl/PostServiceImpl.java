package com.thinktank.post.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.common.utils.ObjectMapperUtil;
import com.thinktank.generator.dto.PostInfoDto;
import com.thinktank.generator.entity.*;
import com.thinktank.generator.mapper.*;
import com.thinktank.post.config.AddPostDocFanoutConfig;
import com.thinktank.post.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉20⽇ 19:34
 * @Description: 帖子业务接口实现类
 * @Version: 1.0
 */
@Slf4j
@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private BlockInfoMapper blockInfoMapper;

    @Autowired
    private PostInfoMapper postInfoMapper;

    @Autowired
    private PostCommentsMapper postCommentsMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private MessagePrivateMapper messagePrivateMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 获取板块信息
    private BlockInfo getBlockExists(Long id) {
        BlockInfo blockInfo = blockInfoMapper.selectById(id);
        if (blockInfo == null) {
            log.error("板块'{}'不存在", id);
            throw new ThinkTankException("当前板块不存在！");
        }
        return blockInfo;
    }

    @Transactional
    @Override
    public void publish(PostInfoDto postInfoDto) {
        // 验证当前板块是否存在
        getBlockExists(postInfoDto.getBlockId());

        // 获取登录用户id
        long loginId = StpUtil.getLoginIdAsLong();

        // 验证当前用户是否被所在板块禁言
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, loginId);
        queryWrapper.eq(SysUserRole::getBlockId, postInfoDto.getBlockId());
        queryWrapper.eq(SysUserRole::getRoleId, 104L);
        if (sysUserRoleMapper.selectCount(queryWrapper) > 0) {
            log.error("用户'{}'在'{}'被禁言，无法发布帖子", loginId, postInfoDto.getBlockId());
            throw new ThinkTankException("您在当前板块已被禁言，无法发布帖子！");
        }

        // 写入记录到帖子信息表
        PostInfo postInfo = new PostInfo();
        BeanUtils.copyProperties(postInfoDto, postInfo);
        postInfo.setUserId(loginId);
        postInfoMapper.insert(postInfo);

        // 写入记录到帖子评论表
        PostComments postComments = new PostComments();
        BeanUtils.copyProperties(postInfoDto, postComments);
        postComments.setPostId(postInfo.getId());
        postComments.setUserId(loginId);
        postComments.setTopicFlag(1);
        postCommentsMapper.insert(postComments);

        // 验证是否要提问
        Integer count = postInfoDto.getCount();
        if (count > 0) {
            // 随机抽取用户
            List<Long> randomList = sysUserMapper.selectRandomList(count);
            if (randomList.size() < count) {
                throw new ThinkTankException("发布失败，当前系统注册用户人数未超过" + count + "人。");
            }

            // 遍历随机用户，将提问信息记录到用户私信表中
            String content = postInfoDto.getContent();
            for (Long acceptUserId : randomList) {
                MessagePrivate messagePrivate = new MessagePrivate();
                messagePrivate.setSendUserId(loginId);
                messagePrivate.setAcceptUserId(acceptUserId);
                messagePrivate.setContent(content);
                messagePrivateMapper.insert(messagePrivate);
            }
        }

        // 提交待处理帖子id到mq队列，由队列异步处理写入es文档操作
        // 1.全局唯一的消息ID，需要封装到CorrelationData中
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        // 2.添加callback
        correlationData.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error("消息发送异常, ID:{}, 原因{}", correlationData.getId(), throwable.getMessage());
            }

            @Override
            public void onSuccess(CorrelationData.Confirm confirm) {
                if (confirm.isAck()) {
                    // 3.1.ack，消息成功
                    log.debug("消息发送成功, ID:{}", correlationData.getId());
                } else {
                    // 3.2.nack，消息失败
                    log.error("消息发送失败, ID:{}, 原因{}", correlationData.getId(), confirm.getReason());
                }
            }
        });
        // 3.发送消息
        String json = ObjectMapperUtil.toJSON(postInfo);
        Message message = MessageBuilder.withBody(json.getBytes(StandardCharsets.UTF_8)).build(); // 消息内容
        rabbitTemplate.convertAndSend(AddPostDocFanoutConfig.FANOUT_EXCHANGE, "", message, correlationData);
    }

    // 细粒度验证用户身份
    private Boolean validateRole(Long postId) {

        // 验证是否超级管理员身份
        if (StpUtil.hasRole("super-admin")) {
            return true;
        }

        // 获取当前登录用户id
        long loginId = StpUtil.getLoginIdAsLong();

        // 验证该帖子是否由用户自己发布的
        PostInfo postInfo = postInfoMapper.selectById(postId);
        if (postInfo == null) {
            log.error("帖子id:'{}'不存在", postId);
            throw new ThinkTankException("该帖子id不存在！");
        }

        if (postInfo.getUserId().equals(loginId)) {
            return true;
        }

        // 验证是否该板块板主
        if (StpUtil.hasRoleOr("big-master", "small-master")) {
            LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysUserRole::getUserId, loginId);
            queryWrapper.eq(SysUserRole::getBlockId, postInfo.getBlockId());
            SysUserRole sysUserRole = sysUserRoleMapper.selectOne(queryWrapper);
            if (sysUserRole == null) {
                return false;
            }

            // 验证是否被禁言用户，若不是则代表该用户有当前板块的板主或小版主角色
            return !sysUserRole.getRoleId().equals(104L);
        }
        return false;
    }

    @Transactional
    @Override
    public void delete(Long postId) {
        if (!validateRole(postId)) {
            throw new ThinkTankException("您无权对他人的帖子进行操作！");
        }

        // 删除该帖子下所有评论
        LambdaQueryWrapper<PostComments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostComments::getPostId, postId);
        postCommentsMapper.delete(queryWrapper);

        // 删除该帖子
        postInfoMapper.deleteById(postId);
    }
}
