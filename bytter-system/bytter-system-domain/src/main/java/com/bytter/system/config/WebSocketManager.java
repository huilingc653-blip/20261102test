package com.bytter.system.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.bytter.core.tenant.SmartThreadPoolManager;
import com.bytter.core.utils.IpUtil;
import com.bytter.core.utils.ServletUtils;
import com.bytter.system.api.dto.SocketMsg;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.SocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.websocket.Session;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * socket管理器。
 *
 * @author bytter
 * @since 2021/11/23
 */
@Slf4j
@Component
public class WebSocketManager {

    // Redis key 常量
    public static final String USER_NODE_MAP = "ws:user:node";   // Hash: userId -> nodeId
    public static final String NODE_SESSIONS_MAP = "ws:node:sessions"; // Hash: nodeId -> sessionIdList (可选)

    // Pub/Sub 频道，用于跨节点消息转发
    public static final String CROSS_NODE_CHANNEL = "ws:cross:channel";

    @Value("${server.port}")
    private int serverPort;

    @Autowired
    private StringRedisTemplate redisTemplate;
    // 本地存储：当前节点上连接的客户端
    public static Map<String, List<SocketClient>> LOCAL_CLIENT_MAP = new ConcurrentHashMap<>();

    // 当前节点的唯一标识（可以使用 IP:Port 或应用启动时生成的 UUID）
    private String nodeId;

    /**
     * 跨节点消息订阅线程池（TTL 包装，上下文感知）。
     */
    private static final ExecutorService WS_SUBSCRIBER = SmartThreadPoolManager.wrap(Executors.newSingleThreadExecutor());

    @PostConstruct
    public void init() {
        String clientIp = IpUtil.getLocalIp();
        this.nodeId = clientIp + ":" + serverPort;
        log.info("WebSocketManager 初始化，节点ID: {}", nodeId);
        // 订阅跨节点频道，监听其他节点发来的消息指令
        WS_SUBSCRIBER.execute(this::subscribeCrossNodeMessages);
    }

    // 订阅并处理跨节点消息
    private void subscribeCrossNodeMessages() {
        try {
            redisTemplate.execute((RedisConnection connection) -> {
                connection.subscribe((message, pattern) -> {
                    String channel = new String(message.getChannel());
                    String body = new String(message.getBody());
                    log.info("收到跨节点消息，channel: {}, body: {}", channel, body);
                    CrossNodeMessage crossMsg = JSONUtil.toBean(body, CrossNodeMessage.class);
                    // 只处理目标节点为本节点的消息
                    if (nodeId.equals(crossMsg.getTargetNodeId())) {
                        sendToLocalUser(crossMsg.getTargetUserId(), crossMsg.getMessage());
                    }
                }, CROSS_NODE_CHANNEL.getBytes());
                return null;
            }, true);
        } catch (Exception e) {
            log.error("Redis 订阅异常", e);
        }
    }

    /**
     * 添加一个连接。
     *
     * @param session websocket 会话。
     * @param uid     用户 ID。
     * @return 是否添加成功。
     */
    public boolean add(Session session, String uid) {
        // 1. 存入本地 Map
        List<SocketClient> clients = LOCAL_CLIENT_MAP.getOrDefault(uid, new ArrayList<>());
        clients.add(SocketClient.builder().session(session).uid(uid).build());
        LOCAL_CLIENT_MAP.put(uid, clients);

        // 2. 更新 Redis：记录用户当前所在的节点
        redisTemplate.opsForHash().put(USER_NODE_MAP, uid, nodeId);

        // 可选：同时记录节点的 Session 列表（便于统计或清理）
        redisTemplate.opsForSet().add(NODE_SESSIONS_MAP + ":" + nodeId, session.getId());
        log.info("用户 {} 上线，节点: {}", uid, nodeId);
        return true;
    }

    /**
     * 删除一个连接。
     *
     * @param uid     用户 ID。
     * @param session websocket 会话。
     * @return 是否删除成功。
     */
    public List<SocketClient> remove(String uid, Session session) {
        // 1. 从本地 Map 移除
        List<SocketClient> clients = LOCAL_CLIENT_MAP.get(uid);
        if (clients != null) {
            clients.removeIf(socketClient -> socketClient.getSession().equals(session));
            if (clients.isEmpty()) {
                LOCAL_CLIENT_MAP.remove(uid);
                // 2. 如果该用户没有其他本地连接，删除 Redis 中的节点映射
                redisTemplate.opsForHash().delete(USER_NODE_MAP, uid);
                log.info("用户 {} 完全离线，节点: {}", uid, nodeId);
            } else {
                LOCAL_CLIENT_MAP.put(uid, clients);
            }
        }

        // 3. 从节点的 Session 集合中移除
        redisTemplate.opsForSet().remove(NODE_SESSIONS_MAP + ":" + nodeId, session.getId());

        return clients;
    }

    /**
     * 向指定用户发送消息（自动跨节点）。
     *
     * @param userId  用户 ID
     * @param message 消息内容
     * @return 是否发送成功（仅代表指令已处理，不代表对方已收到）
     */
    public boolean sendToUser(String userId, SocketMsg message) {
        // 1. 先从 Redis 查询用户所在的节点
        String targetNodeId = (String) redisTemplate.opsForHash().get(USER_NODE_MAP, userId);
        if (targetNodeId == null) {
            log.warn("用户 {} 不在线", userId);
            return false;
        }

        // 2. 如果是当前节点，直接通过本地 Session 发送
        if (targetNodeId.equals(this.nodeId)) {
            log.info("用户 {} 在本节点，发送消息：{}", userId, message);
            return sendToLocalUser(userId, message);
        } else {
            // 3. 发送跨节点消息到目标节点
            CrossNodeMessage crossMsg = new CrossNodeMessage(userId, targetNodeId, message);
            String msgJson = JSONUtil.toJsonStr(crossMsg);
            log.info("跨节点消息已发送，目标用户: {}, 目标节点: {}, 消息内容: {}", userId, targetNodeId, msgJson);
            redisTemplate.convertAndSend(CROSS_NODE_CHANNEL, msgJson);
            return true;
        }
    }

    /**
     * 向指定用户发送消息（仅限当前节点）。
     *
     * @param userId 用户 ID。
     * @param message 消息内容。
     * @return 是否发送成功。
     */
    private boolean sendToLocalUser(String userId, SocketMsg message) {
        List<SocketClient> clients = LOCAL_CLIENT_MAP.get(userId);
        if (CollectionUtil.isEmpty(clients)) {
            return false;
        }
        for (SocketClient client : clients) {
            Session session = client.getSession();
            if (session.isOpen()) {
                String jsonStr = JSONUtil.toJsonStr(message);
                session.getAsyncRemote().sendText(jsonStr);
                log.info("用户 {} 发送消息：{}", userId, jsonStr);
            } else {
                // 如果会话已关闭，清理本地和 Redis
                remove(userId, session);
            }
        }
        return true;
    }

    /**
     * 获取指定用户列表的连接（仅限当前节点）。
     *
     * @param uids 用户 ID 列表
     * @return 用户列表的连接
     */
    public List<SocketClient> get(String... uids) {
        if (uids == null || uids.length == 0) {
            return Lists.newArrayList();
        }
        List<String> idList = Lists.newArrayList(uids);
        return LOCAL_CLIENT_MAP.entrySet().stream()
                .filter(entry -> idList.contains(entry.getKey()))
                .map(Map.Entry::getValue).flatMap(Collection::stream).collect(Collectors.toList());
    }

    /**
     * 获取指定用户列表的在线连接（自动跨节点）。
     *
     * @param uids 用户 ID 列表
     * @return 用户列表的在线连接
     */
    public List<SocketClient> getOnline(String... uids) {
        if (uids == null || uids.length == 0) {
            return Lists.newArrayList();
        }
        Set<String> onlineUserIds = getAllOnlineUserIds();
        List<String> idList = Lists.newArrayList(uids);
        List<String> userIds = onlineUserIds.stream().filter(idList::contains).collect(Collectors.toList());
        return userIds.stream().map(userId -> SocketClient.builder().uid(userId).build()).collect(Collectors.toList());
    }

    /**
     * 获取所有连接（仅限当前节点）。
     *
     * @return 所有连接。
     */
    public List<SocketClient> listAll() {
        return LOCAL_CLIENT_MAP.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    /**
     * 获取所有在线连接（自动跨节点）。
     *
     * @return 所有在线连接。
     */
    public List<SocketClient> listAllOnline() {
        Set<String> onlineUserIds = getAllOnlineUserIds();
        return onlineUserIds.stream().map(userId -> SocketClient.builder().uid(userId).build()).collect(Collectors.toList());
    }

    /**
     * 获取除了指定用户列表以外的所有连接（仅限当前节点）。
     *
     * @param excludeUids 需要排除的用户列表
     * @return 所有连接
     */
    public List<SocketClient> list(List<String> excludeUids) {
        if (CollectionUtil.isEmpty(excludeUids)) {
            return listAll();
        }
        return LOCAL_CLIENT_MAP.entrySet().stream()
                .filter(entry -> !excludeUids.contains(entry.getKey()))
                .map(Map.Entry::getValue).flatMap(Collection::stream).collect(Collectors.toList());
    }

    /**
     * 获取除了指定用户列表以外的所有在线连接（自动跨节点）。
     *
     * @param excludeUids 需要排除的用户列表
     * @return 所有在线连接
     */
    public List<SocketClient> listOnline(List<String> excludeUids) {
        if (CollectionUtil.isEmpty(excludeUids)) {
            return listAllOnline();
        }
        Set<String> onlineUserIds = getAllOnlineUserIds();
        return onlineUserIds.stream().filter(id -> !excludeUids.contains(id))
                .map(userId -> SocketClient.builder().uid(userId).build()).collect(Collectors.toList());
    }

    /**
     * 获取所有在线用户的ID（全集群）
     */
    public Set<String> getAllOnlineUserIds() {
        Set<Object> keys = redisTemplate.opsForHash().keys(USER_NODE_MAP);
        return keys.stream().map(Object::toString).collect(Collectors.toSet());
    }

    /**
     * 获取指定用户所在的节点ID
     */
    public String getUserNode(String userId) {
        return (String) redisTemplate.opsForHash().get(USER_NODE_MAP, userId);
    }

    @Data
    @Builder
    public static class SocketClient implements Serializable {
        // 用户 ID
        private String uid;
        // websocket 会话
        private Session session;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CrossNodeMessage implements Serializable {
        // 目标用户 ID
        private String targetUserId;
        // 目标节点 ID
        private String targetNodeId;
        // 消息内容
        private String msg;
        // 消息类型
        private SocketMsg.Type type;

        public CrossNodeMessage(String targetUserId, String targetNodeId, SocketMsg message) {
            this.targetUserId = targetUserId;
            this.targetNodeId = targetNodeId;
            this.msg = message.getMsg();
            this.type = message.getType();
        }

        public SocketMsg getMessage() {
            return SocketMsg.build(type, msg);
        }
    }

    @PreDestroy
    public void destroy() {
        log.info("WebSocketManager 关闭，节点: {}", nodeId);
        // 清理当前节点在 Redis 中所有用户的映射
        // 遍历 LOCAL_CLIENT_MAP 的 key，删除 USER_NODE_MAP 中的记录
        for (String uid : LOCAL_CLIENT_MAP.keySet()) {
            redisTemplate.opsForHash().delete(USER_NODE_MAP, uid);
        }
    }

}
