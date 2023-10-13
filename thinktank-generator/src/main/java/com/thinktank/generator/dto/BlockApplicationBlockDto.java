package com.thinktank.generator.dto;

import com.thinktank.generator.entity.BlockApplicationBlock;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉27⽇ 17:27
 * @Description: 申请创建板块记录分页查询
 * @Version: 1.0
 */
@Data
public class BlockApplicationBlockDto extends BlockApplicationBlock {
    @ApiModelProperty("当前页")
    private Integer currentPage;

    @ApiModelProperty("当前页显示数量")
    private Integer size;
}
