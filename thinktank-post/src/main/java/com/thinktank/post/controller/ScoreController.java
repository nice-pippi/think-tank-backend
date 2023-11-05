package com.thinktank.post.controller;

import com.thinktank.common.utils.R;
import com.thinktank.common.validationgroups.InsertValidation;
import com.thinktank.generator.entity.PostScore;
import com.thinktank.post.service.ScoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 弘
 * @CreateTime: 2023年11⽉05⽇ 19:44
 * @Description: 评分接口
 * @Version: 1.0
 */
@Api(tags = "评分接口")
@RestController
@RequestMapping("score")
public class ScoreController {
    @Autowired
    private ScoreService scoreService;

    @ApiOperation("帖子评分")
    @PostMapping
    public R<Integer> score(@RequestBody @Validated(InsertValidation.class) PostScore postScore) {
        return R.success(scoreService.score(postScore));
    }

    @ApiOperation("获取当前用户对帖子的评分")
    @GetMapping({"{postId}"})
    public R<Integer> getScore(@PathVariable("postId") Long postId) {
        return R.success(scoreService.getScore(postId));
    }
}
