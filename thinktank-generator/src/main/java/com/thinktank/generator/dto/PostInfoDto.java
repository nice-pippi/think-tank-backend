package com.thinktank.generator.dto;

import com.thinktank.common.validationgroups.InsertValidation;
import com.thinktank.common.validationgroups.QueryValidation;
import com.thinktank.generator.entity.PostInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    @Min(groups = {QueryValidation.class}, value = 1, message = "当前页码最小不能低于1")
    @Max(groups = {QueryValidation.class}, value = 50, message = "当前页码最大不能高于50")
    @NotNull(groups = QueryValidation.class, message = "当前页码不能为空")
    @ApiModelProperty("当前页")
    private Integer currentPage;

    @Min(groups = {QueryValidation.class}, value = 1, message = "每页显示数量最小不能低于1")
    @Max(groups = {QueryValidation.class}, value = 20, message = "每页显示数量最大不能高于20")
    @NotNull(groups = QueryValidation.class, message = "当前页显示数不能为空")
    @ApiModelProperty("当前页显示数量")
    private Integer size;

    @NotEmpty(groups = {InsertValidation.class}, message = "帖子内容不能为空")
    @ApiModelProperty("帖子内容")
    private String content;

    @NotNull(groups = {InsertValidation.class}, message = "提问人数不能为空")
    @ApiModelProperty("提问人数")
    private Integer count;
}
