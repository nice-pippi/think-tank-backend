package com.thinktank.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thinktank.generator.entity.MessagePrivate;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author pippi
 * @since 2023-09-11
 */
public interface MessagePrivateMapper extends BaseMapper<MessagePrivate> {
    @Insert("insert into message_private (send_user_id, accept_user_id, content)  values (#{sendUserId},#{acceptUserId},#{content})")
    void insertByCondition(@Param("sendUserId") Long sendUserId,
                           @Param("acceptUserId") Long acceptUserId,
                           @Param("content") String content);
}
