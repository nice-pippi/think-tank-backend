package com.thinktank.block.controller;

import com.thinktank.block.service.FollowService;
import com.thinktank.common.utils.R;
import com.thinktank.generator.entity.BlockInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉20⽇ 14:07
 * @Description: 关注板块接口
 * @Version: 1.0
 */
@Api(tags = "关注板块接口")
@RestController
@RequestMapping("follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    @ApiOperation("关注板块")
    @PostMapping("{id}")
    public R<String> followBlock(@PathVariable("id") Long id) {
        followService.followBlock(id);
        return R.success("关注成功！");
    }

    @ApiOperation("根据用户id获取所有已关注板块")
    @GetMapping("/getAllFollow/{id}")
    public R<List<BlockInfo>> getAllFollow(@PathVariable("id") Long id) {
        return R.success(followService.getAllFollow(id));
    }
}
