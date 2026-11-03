package com.bytter.system.config;

import cn.hutool.json.JSONUtil;
import com.bytter.core.utils.JsonUtils;
import com.bytter.system.api.dto.SocketMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;

/**
 * WebSocket服务端
 *
 * @author zhaohc
 * @since 2026/3/31 18:18
 */
@Slf4j
@Component
@ServerEndpoint(value = "/ws/{uid}", configurator = SpringWebSocketConfigurator.class)
public class WebSocketServer {

    @Autowired
    private WebSocketManager webSocketManager;

    /**
     * 与客户端建立socket链接
     *
     * @param session 发送消息session
     * @param uid     用户ID
     */
    @OnOpen
    public void open(Session session, @PathParam("uid") String uid) {
        webSocketManager.add(session, uid);
        log.info("用户ID {} 已建立socket链接 sessionId={}", uid, session.getId());
    }

    @OnClose
    public void close(Session session, @PathParam("uid") String uid) {
        log.info("用户ID {} 关联sessionId {} 关闭链接", uid, session.getId());
        webSocketManager.remove(uid, session);
    }

    /**
     * 收到客户端消息
     */
    @OnMessage
    public void message(String message, Session session) {

    }

    @OnError
    public void onError(Session session, Throwable error) {

    }

    /**
     * 广播发送消息
     *
     * @param socketMsg 发送消息
     * @return 是否发送
     */
    public boolean broadcastMessage(SocketMsg socketMsg, String... excludeUid) {
        List<WebSocketManager.SocketClient> socketClients;
        if (excludeUid == null || excludeUid.length == 0) {
            socketClients = webSocketManager.listAllOnline();
        } else {
            socketClients = webSocketManager.listOnline(Arrays.asList(excludeUid));
        }
        return sendText(socketMsg, socketClients);
    }

    /**
     * 批量发送消息。
     *
     * @param socketMsg     发送消息。
     * @param socketClients 接收消息的socket客户端。
     * @return 是否发送成功。
     */
    private boolean sendText(SocketMsg socketMsg, List<WebSocketManager.SocketClient> socketClients) {
        if (socketClients.isEmpty()) {
            return Boolean.FALSE;
        }
        // Future<Void> voidFuture = null;
        boolean result = false;
        for (WebSocketManager.SocketClient socketClient : socketClients) {
            // voidFuture = socketClient.getSession().getAsyncRemote()
            //         .sendText(JsonUtils.toJSONString(socketMsg));
            result = webSocketManager.sendToUser(socketClient.getUid(), socketMsg);
        }
        return result;
    }

    /**
     * 指定一群人发送消息
     */
    public Boolean sendMessage(SocketMsg socketMsg, String... userIds) {
        if (Objects.isNull(userIds) || userIds.length == 0) {
            return Boolean.FALSE;
        }
        List<WebSocketManager.SocketClient> socketClients = webSocketManager.getOnline(userIds);
        return sendText(socketMsg, socketClients);
    }
}
