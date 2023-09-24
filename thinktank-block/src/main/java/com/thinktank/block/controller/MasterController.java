package com.thinktank.block.controller;

import com.thinktank.block.service.MasterService;
import com.thinktank.common.utils.R;
import com.thinktank.common.validationgroups.InsertValidation;
import com.thinktank.generator.entity.BlockApplicationMaster;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉23⽇ 16:52
 * @Description: 板主管理接口
 * @Version: 1.0
 */
@Api(tags = "板主管理接口")
@RestController
@RequestMapping("master")
public class MasterController {
    @Autowired
    private MasterService masterService;

    @ApiOperation("申请板主/小板主")
    @PostMapping("applicationMaster")
    public R<String> applicationMaster(@RequestBody @Validated({InsertValidation.class}) BlockApplicationMaster blockApplicationMaster) {
        return R.success(masterService.applicationMaster(blockApplicationMaster));
    }
}
