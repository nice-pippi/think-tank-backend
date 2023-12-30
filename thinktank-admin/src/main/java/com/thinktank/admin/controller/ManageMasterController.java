package com.thinktank.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thinktank.admin.service.ManageMasterService;
import com.thinktank.common.utils.R;
import com.thinktank.generator.dto.MasterInfoDto;
import com.thinktank.generator.vo.MasterInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉26⽇ 14:14
 * @Description: 板主管理
 * @Version: 1.0
 */
@Api(tags = "板主管理")
@RestController
@RequestMapping("masterManage")
public class ManageMasterController {
    @Autowired
    private ManageMasterService manageMasterService;

    @ApiOperation("分页查询板主")
    @RequestMapping("/page")
    public R<IPage<MasterInfoVo>> page(@RequestBody MasterInfoDto masterInfoDto) {
        return R.success(manageMasterService.page(masterInfoDto));
    }
}
