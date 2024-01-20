package com.thinktank.generator.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 弘
 * @CreateTime: 2024年01⽉19⽇ 16:17
 * @Description: 类描述
 * @Version: 1.0
 */
@Data
public class UserLoginCountBySevenDayVo {
    @ApiModelProperty("日期")
    private String loginDate;

    @ApiModelProperty("用户数量")
    private Integer userCount;
}
