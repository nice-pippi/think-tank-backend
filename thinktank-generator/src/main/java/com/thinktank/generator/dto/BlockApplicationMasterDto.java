package com.thinktank.generator.dto;

import com.thinktank.generator.entity.BlockApplicationMaster;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉13⽇ 18:20
 * @Description: 申请板主记录分页查询
 * @Version: 1.0
 */
@Data
public class BlockApplicationMasterDto extends BlockApplicationMaster {
    @ApiModelProperty("当前页")
    private Integer currentPage;

    @ApiModelProperty("当前页显示数量")
    private Integer size;

    @ApiModelProperty("大分类板块id")
    private Long bigTypeId;

    @ApiModelProperty("小分类板块id")
    private Long smallTypeId;
}
