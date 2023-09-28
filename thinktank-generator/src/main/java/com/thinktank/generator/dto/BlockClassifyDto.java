package com.thinktank.generator.dto;

import com.thinktank.generator.entity.BlockBigType;
import com.thinktank.generator.entity.BlockSmallType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉22⽇ 14:50
 * @Description: 板块分类dto
 * @Version: 1.0
 */
@Data
public class BlockClassifyDto extends BlockBigType {
    @ApiModelProperty("子板块")
    List<BlockSmallType> subCategories;
}
