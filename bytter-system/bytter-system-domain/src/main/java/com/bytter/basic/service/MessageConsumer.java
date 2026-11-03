package com.bytter.basic.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.bytter.core.autoconfig.MessageProducer;
import com.bytter.core.base.entity.BtLogDTO;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.tenant.SmartThreadPoolManager;
import com.bytter.core.utils.JsonUtils;
import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class MessageConsumer implements MessageListener {
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Autowired
    private MessageProducer messageProducer;
    
    @Autowired
    private LogAopService logAopService;
    
    private String queueKey = "oprator-log";
    
//    @Value("${app.redis.dead-letter-queue}") 暂时没用到
    private String deadLetterQueueKey = "";
    
    private static final int MAX_RETRY = 3;

    /**
     * 操作日志消费线程池（TTL 包装，上下文感知）。
     */
    private static final ExecutorService MESSAGE_EXECUTOR = SmartThreadPoolManager.wrap(Executors.newSingleThreadExecutor());

    /**
     * 死信队列消费线程池（独立单线程，避免与主队列消费者共享同一线程导致饿死）。
     */
    private static final ExecutorService DEAD_LETTER_EXECUTOR = SmartThreadPoolManager.wrap(Executors.newSingleThreadExecutor());
    
    /**
     * 初始化消息监听
     */
    @PostConstruct
    public void init() {
        // 启动阻塞式消费者
        startBlockingConsumer();
        // 启动死信队列消费者
        //startDeadLetterConsumer();
    }
    
    /**
     * 启动主队列消费者（阻塞式）
     */
    private void startBlockingConsumer() {
        MESSAGE_EXECUTOR.execute(() -> {
            while (true) {
                try {
                    // 阻塞式获取消息（右出），设置超时时间30秒
                    String message = redisTemplate.opsForList().rightPop(queueKey, 30, TimeUnit.SECONDS);
                    
                    if (message != null) {
                        log.info("收到操作消息: {}", message);
                        processMessage(message);
                    }
                } catch (Exception e) {
                    log.error("操作消费异常: {}", e.getMessage());
                    try {
                        Thread.sleep(5000); // 异常后暂停5秒
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
    }
    
    /**
     * 启动死信队列消费者
     */
    private void startDeadLetterConsumer() {
        DEAD_LETTER_EXECUTOR.execute(() -> {
            while (true) {
                try {
                    String message = redisTemplate.opsForList().rightPop(deadLetterQueueKey, 60, TimeUnit.SECONDS);
                    if (message != null) {
                        log.info("处理死信消息: {}", message);
                        // 这里可以添加死信消息的特殊处理逻辑
                    }
                } catch (Exception e) {
                    log.error("死信队列消费异常: {}", e.getMessage());
                }
            }
        });
    }
    
    /**
     * 处理消息
     * @param message 消息内容
     */
    private void processMessage(String message) {
        try {
        	BtLogDTO btLogTDO = JsonUtils.parseObject(message, BtLogDTO.class);
            // 从消息体还原租户上下文：消费线程为常驻复用线程，TTL 快照固化于启动时（为空），
            // 必须逐消息显式安装租户，否则下游落库线程（logPoolExecutor）拿到的租户为空。
            String tenantId = btLogTDO.getTenantId();
            if (tenantId == null || tenantId.trim().isEmpty()) {
                throw new IllegalArgumentException("操作日志消息缺少租户标识，拒绝处理");
            }
            UserContextHelper.helper().setTTLTenant(tenantId);
            // 模拟业务处理
            log.info("处理消息: {}", message);
            logAopService.asynSaveLog(btLogTDO);
            // 业务处理成功
            log.info("消息处理成功: {}", message);
        } catch (Exception e) {
            log.error("消息处理失败: {}，错误: {}", message, e.getMessage());
            // 失败处理逻辑（重试或进入死信队列）
            handleFailedMessage(message);
        } finally {
            // 清理租户上下文，避免常驻线程串扰下一条消息
            UserContextHelper.helper().clearTTLTenant();
        }
    }
    
    /**
     * 处理失败消息
     * @param message 消息内容
     */
    private void handleFailedMessage(String message) {
        // 从消息中解析重试次数
        int retryCount = 0;
        if (message.startsWith("retry:")) {
            String[] parts = message.split(":", 3);
            retryCount = Integer.parseInt(parts[1]);
            message = parts[2];
        }
        
        if (retryCount < MAX_RETRY) {
            // 重试
            String retryMessage = "retry:" + (retryCount + 1) + ":" + message;
            redisTemplate.opsForList().leftPush(queueKey, retryMessage);
            log.info("消息重试 ({}/{}): {}", retryCount + 1, MAX_RETRY, message);
        } else {
            // 进入死信队列
            messageProducer.sendToDeadLetterQueue(message);
            log.info("消息进入死信队列: {}", message);
        }
    }

	@Override
	public void onMessage(Message message, byte[] pattern) {
		// TODO Auto-generated method stub
		
	}
}