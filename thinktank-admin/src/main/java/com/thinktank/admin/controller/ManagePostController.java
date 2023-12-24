package com.thinktank.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thinktank.admin.service.ManagePostService;
import com.thinktank.common.utils.R;
import com.thinktank.generator.dto.PostInfoDto;
import com.thinktank.generator.vo.PostInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉23⽇ 15:50
 * @Description: 帖子管理
 * @Version: 1.0
 */
@Api(tags = "帖子管理")
@RestController
@RequestMapping("postManage")
public class ManagePostController {
    @Autowired
    private ManagePostService managePostService;

    @ApiOperation("分页查询帖子")
    @RequestMapping("/page")
    public R<IPage<PostInfoVo>> page(@RequestBody PostInfoDto postInfoDto) {
        return R.success(managePostService.page(postInfoDto));
    }

    @ApiOperation("删除帖子")
    @DeleteMapping("{id}")
    public R<String> delete(@PathVariable("id") Long id) {
        managePostService.delete(id);
        return R.success("删除成功");
    }
}
