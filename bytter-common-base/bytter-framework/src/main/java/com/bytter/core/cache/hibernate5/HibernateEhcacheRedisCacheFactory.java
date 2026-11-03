package com.bytter.core.cache.hibernate5;
import com.bytter.core.cache.hibernate5.listener.HibernateCacheMessageDelegateListener;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import java.util.*;
/**
 * 用于扩展hiberntae二级缓存与redis适配
 *
 * @author zhoul
 * @since 2017-12-20
 * @since update by zhoul 2020-04-24 修改redis适配哨兵集群模式
 */
@Configurable
public class HibernateEhcacheRedisCacheFactory {
    private CacheManager cacheManager;
    private Properties redisProperties;
    @Autowired
    private RedisOperations redisOperations;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    private String channel;
    private RedisMessageListenerContainer redisMessageListenerContainer;
    private MessageListener messageListenerAdapter;
    private final RedisSerializer valueSerializer = new JdkSerializationRedisSerializer();
    public HibernateEhcacheRedisCacheFactory(CacheManager cacheManager, Properties redisProperties) {
        if (!"true".equalsIgnoreCase(redisProperties.getProperty(IEhcacheRedisSettings.SWITCH))) {
            return;
        }
        this.channel = redisProperties.getProperty(IEhcacheRedisSettings.CHANNEL_HIBERNATE);
        this.cacheManager = cacheManager;
        this.redisProperties = redisProperties;
        initMessageListenerAdapter();
        initMessageListener();
    }
    private void initMessageListenerAdapter() {
        HibernateCacheMessageDelegateListener hibernateCacheMessageDelegateListener = new HibernateCacheMessageDelegateListener(
                channel, cacheManager);
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(
                hibernateCacheMessageDelegateListener);
        messageListenerAdapter.setSerializer(valueSerializer);
        messageListenerAdapter.afterPropertiesSet();
        this.messageListenerAdapter = messageListenerAdapter;
    }
    private void initMessageListener() {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        Map<MessageListener, Collection<? extends Topic>> messageListeners = new HashMap<>();
        ArrayList<Topic> topics = new ArrayList<>();
        topics.add(new ChannelTopic(channel));
        messageListeners.put(getMessageListenerAdapter(), topics);
        redisMessageListenerContainer.setMessageListeners(messageListeners);
        redisMessageListenerContainer.afterPropertiesSet();
        redisMessageListenerContainer.start();
        this.redisMessageListenerContainer = redisMessageListenerContainer;
    }
    public RedisOperations getRedisOperations() {
        return redisOperations;
    }
    public MessageListener getMessageListenerAdapter() {
        return messageListenerAdapter;
    }
    public void destroy() {
        if (redisMessageListenerContainer != null) {
            try {
                redisMessageListenerContainer.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
