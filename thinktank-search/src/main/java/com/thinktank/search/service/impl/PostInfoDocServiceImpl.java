package com.thinktank.search.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thinktank.common.utils.ObjectMapperUtil;
import com.thinktank.generator.entity.BlockInfo;
import com.thinktank.generator.entity.PostComments;
import com.thinktank.generator.entity.PostInfo;
import com.thinktank.generator.entity.SysUser;
import com.thinktank.generator.mapper.BlockInfoMapper;
import com.thinktank.generator.mapper.PostCommentsMapper;
import com.thinktank.generator.mapper.SysUserMapper;
import com.thinktank.search.doc.PostInfoDoc;
import com.thinktank.search.service.PostInfoDocService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉21⽇ 19:22
 * @Description: 板块信息文档管理业务接口实现类
 * @Version: 1.0
 */
@Component
public class PostInfoDocServiceImpl implements PostInfoDocService {
    @Autowired
    private PostCommentsMapper postCommentsMapper;

    @Autowired
    private BlockInfoMapper blockInfoMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @RabbitListener(queues = "fanout.queue.postdoc")
    @Override
    public void addPostInfoDoc(Message message) {
        // 获取消息
        byte[] bytes = message.getBody();
        String json = new String(bytes);
        PostInfo postInfo = ObjectMapperUtil.toObject(json, PostInfo.class);

        // 查询帖子前5条发言
        LambdaQueryWrapper<PostComments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostComments::getPostId, postInfo.getId());
        queryWrapper.eq(PostComments::getParentId,0);
        queryWrapper.last("limit 5");
        List<PostComments> postComments = postCommentsMapper.selectList(queryWrapper);

        // 帖子内容
        String content = postComments.stream()
                .filter(item -> item.getTopicFlag() == 1)
                .map(PostComments::getContent)
                .findFirst()
                .orElse("");

        // 去掉帖子内容中的HTML标签
        content = content.replaceAll("<.*?>", "");
        // 去掉制表符
        content = content.replaceAll("\\t", "");

        // 收集所有帖子评论中的图片URL
        Pattern pattern = Pattern.compile("<img\\s+src=\"([^\"]+)\"");
        List<String> imageUrlList = new ArrayList<>();
        for (PostComments comment : postComments) {
            Matcher matcher = pattern.matcher(comment.getContent());
            while (matcher.find()) {
                imageUrlList.add(matcher.group(1));
            }
        }

        // 查询板块名称
        BlockInfo blockInfo = blockInfoMapper.selectById(postInfo.getBlockId());

        // 根据帖子id查询发布帖子用户的名称
        SysUser sysUser = sysUserMapper.selectById(postInfo.getUserId());

        PostInfoDoc postInfoDoc = new PostInfoDoc();
        BeanUtils.copyProperties(postInfo, postInfoDoc);
        postInfoDoc.setContent(content);
        postInfoDoc.setBlockId(blockInfo.getId());
        postInfoDoc.setBlockName(blockInfo.getBlockName());
        postInfoDoc.setUsername(sysUser.getUsername());
        postInfoDoc.setUserId(sysUser.getId());
        postInfoDoc.setImages(imageUrlList);

        // 保存到es文档
        elasticsearchRestTemplate.save(postInfoDoc);
    }
}
