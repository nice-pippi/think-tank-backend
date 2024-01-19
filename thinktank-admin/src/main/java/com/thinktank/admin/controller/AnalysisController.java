package com.thinktank.admin.controller;

import com.thinktank.admin.service.AnalysisService;
import com.thinktank.admin.vo.UserStatisticsVo;
import com.thinktank.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 弘
 * @CreateTime: 2024年01⽉16⽇ 16:33
 * @Description: 数据分析
 * @Version: 1.0
 */
@Api(tags = "数据分析")
@RestController
@RequestMapping("analysis")
public class AnalysisController {
    @Autowired
    private AnalysisService analysisService;

    @ApiOperation("用户统计分析")
    @RequestMapping("getUserStatistic")
    public R<UserStatisticsVo> getUserStatistics() {
        return R.success(analysisService.getUserStatistics());
    }
}
