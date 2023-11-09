package com.thinktank.generator.vo;

import com.thinktank.generator.entity.PostReports;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 弘
 * @CreateTime: 2023年11⽉08⽇ 16:34
 * @Description: 帖子举报记录vo
 * @Version: 1.0
 */
@Data
public class PostReportsVo extends PostReports {
    @ApiModelProperty("帖子标题")
    private String title;

    @ApiModelProperty("举报用户名称")
    private String username;
}
