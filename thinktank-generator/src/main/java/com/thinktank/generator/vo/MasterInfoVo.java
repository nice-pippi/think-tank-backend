package com.thinktank.generator.vo;

import com.thinktank.generator.entity.SysUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉26⽇ 14:17
 * @Description: 板主信息vo
 * @Version: 1.0
 */
@Data
public class MasterInfoVo extends SysUser {
    @ApiModelProperty("板块ID")
    private String blockId;

    @ApiModelProperty("板块名称")
    private String blockName;

    @ApiModelProperty("板块大分类名称")
    private String bigTypeName;

    @ApiModelProperty("板块小分类名称")
    private String smallTypeName;

    @ApiModelProperty("角色ID")
    private String roleId;
}
