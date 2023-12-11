package com.thinktank.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thinktank.generator.entity.MessageChatRoom;
import com.thinktank.generator.vo.MessageChatRoomVo;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author pippi
 * @since 2023-12-09
 */
public interface MessageChatRoomMapper extends BaseMapper<MessageChatRoom> {
    /**
     * 查询所有聊天室
     *
     * @param loginId
     * @return
     */
    List<MessageChatRoomVo> getAllChatRoom(long loginId);
}
