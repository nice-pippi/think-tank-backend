package com.thinktank.generator.vo;

import com.thinktank.generator.entity.BlockApplicationBlock;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉27⽇ 17:22
 * @Description: 申请创建板块分页查询结果
 * @Version: 1.0
 */
@Data
public class BlockApplicationBlockVo extends BlockApplicationBlock {
    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("大分类")
    private String BigTypeName;

    @ApiModelProperty("小分类")
    private String smallTypeName;
}
