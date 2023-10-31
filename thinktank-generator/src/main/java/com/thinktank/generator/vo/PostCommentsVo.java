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
    @ApiModelProperty("本板块名称")
    private String blockName;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("回复数量")
    private String repliesCount;

    @ApiModelProperty("子级评论内容")
    private List<PostCommentsVo> replies;
}
