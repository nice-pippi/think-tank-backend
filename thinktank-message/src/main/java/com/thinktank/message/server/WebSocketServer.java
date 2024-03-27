package com.thinktank.message.server;

import com.thinktank.common.utils.ObjectMapperUtil;
import com.thinktank.generator.entity.MessageChatRoom;
import com.thinktank.generator.entity.MessagePrivate;
import com.thinktank.generator.mapper.MessageChatRoomMapper;
import com.thinktank.generator.mapper.MessagePrivateMapper;
import com.thinktank.message.config.BasedSpringConfigurator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉05⽇ 18:00
 * @Description: 处理用户之间的私聊消息，包括接收、发送、处理存储等操作。
 * @Version: 1.0
 */

@Slf4j
@ServerEndpoint(value = "/private_message/{userId}", configurator = BasedSpringConfigurator.class)
@Component
public class WebSocketServer {
    /**
     * 存储每一个连接
     */
    private static final Map<Long, Session> onLineUsers = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    @Autowired
    private MessagePrivateMapper messagePrivateMapper;

    @Autowired
    private MessageChatRoomMapper messageChatRoomMapper;

    /**
     * 会话连接建立成功调用的方法
     *
     * @param session 会话对象
     * @param userId  用户ID
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        onLineUsers.put(userId, session);
        log.info("当前在线人数:" + onLineUsers.size());
    }


    /**
     * 接收到消息后处理消息的方法
     *
     * @param message 消息内容
     * @param userId  用户ID
     */
    @OnMessage
    public void onMessage(String message, @PathParam("userId") Long userId) {
        log.info("用户'{}'发送消息:{}", userId, message);
        MessagePrivate messagePrivate = ObjectMapperUtil.toObject(message, MessagePrivate.class);


        // 更新当前聊天室最新消息
        MessageChatRoom messageChatRoom = messageChatRoomMapper.selectById(messagePrivate.getChatRoomId());
        if (messageChatRoom == null) {
            sendMessage(messagePrivate.getAcceptUserId(), "当前聊天室不存在");
            return;
        }

        // 更新聊天室最新消息内容
        messageChatRoom.setLatestContent(messagePrivate.getContent());
        messageChatRoomMapper.updateById(messageChatRoom);

        // 消息写入数据库
        messagePrivateMapper.insert(messagePrivate);
        if (onLineUsers.containsKey(messagePrivate.getAcceptUserId())) {
            sendMessage(messagePrivate.getAcceptUserId(), ObjectMapperUtil.toJSON(messagePrivate));
        }
    }


    /**
     * 当连接关闭时被调用的方法
     *
     * @param userId 用户ID
     */
    @OnClose
    public void onClose(@PathParam("userId") Long userId) {
        onLineUsers.remove(userId);
        log.info("断开连接，当前在线人数:" + onLineUsers.size());
    }

    /**
     * 当发生错误时调用的方法
     *
     * @param error  错误信息
     * @param userId 用户ID
     */
    @OnError
    public void onError(Throwable error, @PathParam("userId") Long userId) {
        log.error("用户'{}'错误,原因:{}", userId, error.getMessage());
    }

    /**
     * 发送消息给指定用户
     *
     * @param acceptUserId 接收消息的用户ID
     * @param message      消息内容
     */
    private void sendMessage(Long acceptUserId, String message) {
        try {
            onLineUsers.get(acceptUserId).getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
