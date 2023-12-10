package com.thinktank.generator.vo;

import com.thinktank.generator.entity.MessageChatRoom;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉09⽇ 21:44
 * @Description: 聊天室VO
 * @Version: 1.0
 */
@Data
public class MessageChatRoomVo extends MessageChatRoom {
    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("头像")
    private String avatar;
}
