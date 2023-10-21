package com.thinktank.post.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.generator.dto.PostInfoDto;
import com.thinktank.generator.entity.BlockInfo;
import com.thinktank.generator.entity.MessagePrivate;
import com.thinktank.generator.entity.PostComments;
import com.thinktank.generator.entity.PostInfo;
import com.thinktank.generator.mapper.*;
import com.thinktank.post.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private MessagePrivateMapper messagePrivateMapper;

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

        // todo:写入帖子信息到es
    }
}
