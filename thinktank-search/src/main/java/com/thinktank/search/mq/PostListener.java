package com.thinktank.search.mq;

import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.common.utils.RabbitMQUtil;
import com.thinktank.generator.entity.PostComments;
import com.thinktank.generator.entity.PostInfo;
import com.thinktank.generator.mapper.PostCommentsMapper;
import com.thinktank.generator.vo.PostCommentsVo;
import com.thinktank.search.doc.PostInfoDoc;
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
 * @CreateTime: 2024年01⽉14⽇ 15:28
 * @Description: 帖子模块消费者
 * @Version: 1.0
 */
@Component
public class PostListener {
    @Autowired
    private PostCommentsMapper postCommentsMapper;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 监听添加帖子文档消息
     *
     * @param message 消息体
     */
    @RabbitListener(queues = "fanout.queue.postdoc")
    public void addPostInfoDoc(Message message) {
        // 获取消息
        PostInfo postInfo = RabbitMQUtil.getObject(message, PostInfo.class);

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

    /**
     * 监听删除帖子文档消息
     *
     * @param postId 帖子id
     */
    @RabbitListener(queues = "fanout.queue.post.delete")
    public void deletePostInfoDoc(String postId) {
        elasticsearchRestTemplate.delete(postId, PostInfoDoc.class);
    }
}
