package com.thinktank.generator.dto;

import com.thinktank.common.validationgroups.InsertValidation;
import com.thinktank.generator.entity.PostInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉21⽇ 14:56
 * @Description: 发布帖子dto
 * @Version: 1.0
 */
@Data
public class PostInfoDto extends PostInfo {
    @NotEmpty(groups = {InsertValidation.class}, message = "帖子内容不能为空")
    @ApiModelProperty("帖子内容")
    private String content;

    @NotNull(groups = {InsertValidation.class}, message = "提问人数不能为空")
    @ApiModelProperty("提问人数")
    private Integer count;
}
