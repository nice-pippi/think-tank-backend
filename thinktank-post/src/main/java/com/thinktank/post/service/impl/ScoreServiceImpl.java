package com.thinktank.post.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.generator.entity.PostInfo;
import com.thinktank.generator.entity.PostScore;
import com.thinktank.generator.mapper.PostInfoMapper;
import com.thinktank.generator.mapper.PostScoreMapper;
import com.thinktank.post.service.ScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 弘
 * @CreateTime: 2023年11⽉05⽇ 19:46
 * @Description: 评分业务接口实现类
 * @Version: 1.0
 */
@Slf4j
@Service
public class ScoreServiceImpl implements ScoreService {
    @Autowired
    private PostInfoMapper postInfoMapper;

    @Autowired
    private PostScoreMapper postScoreMapper;

    @Override
    public Integer score(PostScore postScore) {
        PostInfo postInfo = postInfoMapper.selectById(postScore.getPostId());
        long loginId = StpUtil.getLoginIdAsLong();

        // 验证帖子是否存在
        if (postInfo == null) {
            log.error("帖子'{}'不存在，操作用户id:{}", postScore.getPostId(), loginId);
            throw new ThinkTankException("当前帖子不存在！");
        }

        // 验证板块id与帖子id是否匹配
        if (!postInfo.getBlockId().equals(postScore.getBlockId())) {
            log.error("板块id'{}'与帖子id'{}'不匹配，操作用户id:{}", postScore.getBlockId(), postScore.getPostId(), loginId);
            throw new ThinkTankException("操作非法！");
        }

        // 查询数据库当前帖子是否有该用户打分记录，若有则更新，若无则新增
        LambdaQueryWrapper<PostScore> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostScore::getPostId, postScore.getPostId());
        queryWrapper.eq(PostScore::getUserId, loginId);
        PostScore result = postScoreMapper.selectOne(queryWrapper);

        postScore.setUserId(loginId);
        if (result == null) {
            // 写入数据库
            postScoreMapper.insert(postScore);

        } else {
            // 更新记录
            postScore.setId(result.getId());
            postScoreMapper.updateById(postScore);
        }

        return postScore.getScore();
    }

    @Override
    public Integer getScore(Long postId) {
        long loginId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<PostScore> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostScore::getPostId, postId);
        queryWrapper.eq(PostScore::getUserId, loginId);
        PostScore postScore = postScoreMapper.selectOne(queryWrapper);

        if (postScore == null) {
            return 0;
        }
        return postScore.getScore();
    }
}
