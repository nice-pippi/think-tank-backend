package com.thinktank.generator.vo;

import com.thinktank.generator.entity.PostComments;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉23⽇ 15:41
 * @Description: 用户评论vo
 * @Version: 1.0
 */
@Data
public class PostCommentsVo extends PostComments {
    @ApiModelProperty("板块名称")
    private String blockName;

    @ApiModelProperty("帖子标题")
    private String title;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("点赞数量")
    private Integer likes;

    @ApiModelProperty("是否点赞标识")
    private Boolean likeFlag;

    @ApiModelProperty("子级评论内容")
    private List<PostCommentsVo> replies;
}
