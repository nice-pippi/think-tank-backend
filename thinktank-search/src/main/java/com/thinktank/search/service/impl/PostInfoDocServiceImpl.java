package com.thinktank.search.service.impl;

import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.common.utils.ObjectMapperUtil;
import com.thinktank.generator.entity.PostComments;
import com.thinktank.generator.entity.PostInfo;
import com.thinktank.generator.mapper.PostCommentsMapper;
import com.thinktank.generator.vo.PostCommentsVo;
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
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @RabbitListener(queues = "fanout.queue.postdoc")
    @Override
    public void addPostInfoDoc(Message message) {
        // 获取消息
        byte[] bytes = message.getBody();
        String json = new String(bytes);
        PostInfo postInfo = ObjectMapperUtil.toObject(json, PostInfo.class);

        // 根据帖子id获取该帖子前五条评论
        List<PostCommentsVo> postComments = postCommentsMapper.getPostCommentsVoByFive(postInfo.getId());

        // 获取主题帖
        PostCommentsVo postCommentsVo = postComments.stream().filter(item -> item.getTopicFlag() == 1).findFirst().orElse(null);

        if (postCommentsVo == null) {
            throw new ThinkTankException("该帖子不存在主题帖！");
        }

        // 去掉帖子内容中的HTML标签以及制表符
        String content = postCommentsVo.getContent().replaceAll("<.*?>", "").replaceAll("\\t", "");

        // 收集所有帖子评论中的图片URL
        Pattern pattern = Pattern.compile("<img\\s+src=\"([^\"]+)\"");
        List<String> imageUrlList = new ArrayList<>();
        for (PostComments comment : postComments) {
            Matcher matcher = pattern.matcher(comment.getContent());
            while (matcher.find()) {
                imageUrlList.add(matcher.group(1));
            }
        }

        PostInfoDoc postInfoDoc = new PostInfoDoc();
        BeanUtils.copyProperties(postInfo, postInfoDoc);
        postInfoDoc.setContent(content);
        postInfoDoc.setBlockName(postCommentsVo.getBlockName());
        postInfoDoc.setUsername(postCommentsVo.getUsername());
        postInfoDoc.setImages(imageUrlList);

        // 保存到es文档
        elasticsearchRestTemplate.save(postInfoDoc);
    }

    @Override
    public void deletePostInfoDoc(Long id) {
        elasticsearchRestTemplate.delete(id.toString(), PostInfoDoc.class);
    }
}
