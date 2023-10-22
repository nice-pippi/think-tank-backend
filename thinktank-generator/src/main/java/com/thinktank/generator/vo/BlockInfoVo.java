package com.thinktank.generator.vo;

import com.thinktank.generator.entity.BlockInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉06⽇ 19:42
 * @Description: 板块信息vo
 * @Version: 1.0
 */
@Data
public class BlockInfoVo extends BlockInfo {
    @ApiModelProperty("大分类")
    private String bigTypeName;

    @ApiModelProperty("小分类")
    private String smallTypeName;
}
