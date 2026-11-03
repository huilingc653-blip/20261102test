package com.bytter.core.autoconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
//    @Value("${app.redis.queue}")
    private String queueKey = "oprator-log";
    
    
    private String deadQueueKey = "dead=oprator-log";
    /**
     * 发送消息到队列
     * @param message 消息内容
     */
    public void sendMessage(String message) {
    	
        // 使用LPUSH将消息添加到队列头部
        redisTemplate.opsForList().leftPush(queueKey, message);
        System.out.println("发送消息: " + message);
    }
    
    /**
     * 发送消息到死信队列
     * @param message 消息内容
     */
    public void sendToDeadLetterQueue(String message) {
        redisTemplate.opsForList().leftPush(deadQueueKey, message);
        System.out.println("发送到死信队列: " + message);
    }
}