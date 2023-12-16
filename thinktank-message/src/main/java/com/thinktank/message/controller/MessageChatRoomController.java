package com.thinktank.message.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.thinktank.common.utils.R;
import com.thinktank.generator.entity.MessageChatRoom;
import com.thinktank.generator.vo.MessageChatRoomVo;
import com.thinktank.message.service.MessageChatRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉11⽇ 18:41
 * @Description: 聊天室接口
 * @Version: 1.0
 */
@Api(tags = "聊天室接口")
@RestController
@RequestMapping("chatRoom")
public class MessageChatRoomController {
    @Autowired
    private MessageChatRoomService messageChatRoomService;

    @ApiOperation("查询所有聊天室")
    @GetMapping("getAllChatRoom")
    public R<List<MessageChatRoomVo>> getAllChatRoom() {
        return R.success(messageChatRoomService.getAllChatRoom());
    }

    @ApiOperation("新建聊天室")
    @PostMapping
    public R<Boolean> addChatRoom(@RequestBody MessageChatRoom messageChatRoom) {
        messageChatRoomService.addChatRoom(messageChatRoom);
        return R.success(true);
    }
}
