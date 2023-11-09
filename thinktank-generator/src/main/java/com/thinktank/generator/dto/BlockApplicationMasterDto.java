package com.thinktank.generator.dto;

import com.thinktank.common.validationgroups.QueryValidation;
import com.thinktank.generator.entity.BlockApplicationMaster;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉13⽇ 18:20
 * @Description: 申请板主记录分页查询
 * @Version: 1.0
 */
@Data
public class BlockApplicationMasterDto extends BlockApplicationMaster {
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

    @ApiModelProperty("大分类板块id")
    private Long bigTypeId;

    @ApiModelProperty("小分类板块id")
    private Long smallTypeId;
}
