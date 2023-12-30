package com.thinktank.generator.dto;

import com.thinktank.generator.entity.SysUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉26⽇ 14:20
 * @Description: 板主信息dto
 * @Version: 1.0
 */
@Data
public class MasterInfoDto extends SysUser {
    @ApiModelProperty("板块大分类ID")
    private Long bigTypeId;

    @ApiModelProperty("板块小分类ID")
    private Long smallTypeId;

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("当前页")
    private Integer currentPage;

    @ApiModelProperty("当前页显示数量")
    private Integer size;
}
