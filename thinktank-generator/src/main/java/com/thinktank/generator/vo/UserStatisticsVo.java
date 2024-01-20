package com.thinktank.generator.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 弘
 * @CreateTime: 2024年01⽉16⽇ 16:45
 * @Description: 类描述
 * @Version: 1.0
 */
@Data
public class UserStatisticsVo {
    @ApiModelProperty("用户数量")
    private Integer userCount;

    @ApiModelProperty("新用户数量")
    private Long newUserCount;

    @ApiModelProperty("在线人数")
    private Integer onlineCount;

    @ApiModelProperty("男女比例情况")
    private String sexRatio;
}
