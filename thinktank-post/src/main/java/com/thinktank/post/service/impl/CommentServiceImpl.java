package com.thinktank.post.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thinktank.generator.mapper.PostCommentsMapper;
import com.thinktank.generator.vo.PostCommentsVo;
import com.thinktank.post.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉23⽇ 18:32
 * @Description: 评论业务接口
 * @Version: 1.0
 */
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private PostCommentsMapper postCommentsMapper;

    // 递归获取当前评论下所有子评论
    private List<PostCommentsVo> getAllChildrenComment(Long id) {
        List<PostCommentsVo> childrenComment = postCommentsMapper.getAllChildrenComment(id);
        if (childrenComment.size() > 0) {
            for (PostCommentsVo postCommentsVo : childrenComment) {
                postCommentsVo.setReplies(postCommentsMapper.getAllChildrenComment(postCommentsVo.getId()));
            }
        }
        return childrenComment;
    }

    @Override
    public IPage<PostCommentsVo> page(Long postId, Integer currentPage) {
        Page<PostCommentsVo> page = new Page<>(currentPage, 15);

        IPage<PostCommentsVo> commentsVoIPage = postCommentsMapper.getPage(postId, page);
        List<PostCommentsVo> collect = commentsVoIPage.getRecords().stream().map(item -> {
            // 查询当前评论的所有子评论
            List<PostCommentsVo> allChildrenComment = getAllChildrenComment(item.getId());
            item.setReplies(allChildrenComment);
            return item;
        }).collect(Collectors.toList());

        commentsVoIPage.setRecords(collect);
        return commentsVoIPage;
    }


}
