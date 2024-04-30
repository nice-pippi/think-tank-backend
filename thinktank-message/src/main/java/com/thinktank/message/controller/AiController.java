package com.thinktank.message.controller;

import com.thinktank.common.utils.R;
import com.thinktank.message.service.AiChatService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: 弘
 * @CreateTime: 2024年4⽉30⽇ 9:11
 * @Description: Ai对话接口
 * @Version: 1.0
 */
@Api(tags = "Ai对话接口")
@RestController
@RequestMapping("ai")
public class AiController {
    @Autowired
    private AiChatService aiChatService;

    @PostMapping
    public R<String> assistant(@RequestBody Map<String, String> map) {
        String message = map.get("content");
        return R.success(aiChatService.chat(message));
    }
}
