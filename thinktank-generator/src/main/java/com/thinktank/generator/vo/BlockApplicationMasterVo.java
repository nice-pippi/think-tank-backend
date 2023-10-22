package com.thinktank.generator.vo;

import com.thinktank.generator.entity.BlockApplicationMaster;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉13⽇ 18:32
 * @Description: 申请板主分页查询结果vo
 * @Version: 1.0
 */
@Data
public class BlockApplicationMasterVo extends BlockApplicationMaster {
    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("大分类")
    private String BigTypeName;

    @ApiModelProperty("小分类")
    private String smallTypeName;

    @ApiModelProperty("板块名称")
    private String blockName;
}
