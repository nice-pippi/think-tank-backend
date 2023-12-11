package com.thinktank.message.controller;

import com.thinktank.common.utils.R;
import com.thinktank.generator.entity.MessagePrivate;
import com.thinktank.message.service.MessagePrivateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉09⽇ 14:28
 * @Description: 消息接口
 * @Version: 1.0
 */

@Api(tags = "消息接口")
@RestController
@RequestMapping("private_message")
public class MessagePrivateController {
    @Autowired

    private MessagePrivateService messagePrivateService;

    @ApiOperation("根据聊天室id获取私聊消息")
    @GetMapping("/{chatRoomId}")
    public R<List<MessagePrivate>> getPrivateMessage(@PathVariable("chatRoomId") Long chatRoomId) {
        return R.success(messagePrivateService.getPrivateMessage(chatRoomId));
    }

}
