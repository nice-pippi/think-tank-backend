package com.thinktank.search.dto;

import com.thinktank.common.validationgroups.QueryValidation;
import com.thinktank.search.doc.PostInfoDoc;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉21⽇ 19:10
 * @Description: 帖子信息文档实体类dto
 * @Version: 1.0
 */
@Data
public class PostInfoDocDto extends PostInfoDoc {
    @NotNull(groups = QueryValidation.class, message = "当前页码不能为空")
    @ApiModelProperty("当前页")
    private Integer currentPage;

    @NotNull(groups = QueryValidation.class, message = "当前页显示数不能为空")
    @ApiModelProperty("当前页显示数量")
    private Integer size;
}
