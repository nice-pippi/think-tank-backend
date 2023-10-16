package com.thinktank.generator.vo;

import com.thinktank.generator.entity.SysUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉14⽇ 18:50
 * @Description: 板块板主信息
 * @Version: 1.0
 */
@Data
public class BlockMasterListVo {
    @ApiModelProperty("板主列表")
    private List<SysUser> masterList;

    @ApiModelProperty("小板主列表")
    private List<SysUser> smallMasterList;
}
