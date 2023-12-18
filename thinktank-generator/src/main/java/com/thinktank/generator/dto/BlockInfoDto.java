package com.thinktank.generator.dto;

import com.thinktank.common.validationgroups.QueryValidation;
import com.thinktank.generator.entity.BlockInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉18⽇ 18:11
 * @Description: 类描述
 * @Version: 1.0
 */
@Data
public class BlockInfoDto extends BlockInfo {
    @Min(groups = {QueryValidation.class}, value = 1, message = "当前页码最小不能低于1")
    @NotNull(groups = QueryValidation.class, message = "当前页码不能为空")
    @ApiModelProperty("当前页")
    private Integer currentPage;

    @Min(groups = {QueryValidation.class}, value = 1, message = "每页显示数量最小不能低于1")
    @NotNull(groups = QueryValidation.class, message = "当前页显示数不能为空")
    @ApiModelProperty("当前页显示数量")
    private Integer size;
}
