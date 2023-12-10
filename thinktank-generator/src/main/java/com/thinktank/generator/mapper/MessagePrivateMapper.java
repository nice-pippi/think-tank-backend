package com.thinktank.generator.mapper;

import com.thinktank.generator.entity.MessagePrivate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thinktank.generator.vo.MessageChatRoomVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pippi
 * @since 2023-12-09
 */
public interface MessagePrivateMapper extends BaseMapper<MessagePrivate> {

    List<MessageChatRoomVo> getPrivateMessageList(long loginId);
}
