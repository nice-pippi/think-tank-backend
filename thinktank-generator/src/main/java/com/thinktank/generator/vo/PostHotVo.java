package com.thinktank.generator.vo;

import com.thinktank.generator.entity.PostInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 弘
 * @CreateTime: 2024年01⽉03⽇ 16:30
 * @Description: 类描述
 * @Version: 1.0
 */
@Data
public class PostHotVo extends PostInfo {
    @ApiModelProperty("平均评分(保留2位小数)")
    private Double avgScore;
}
