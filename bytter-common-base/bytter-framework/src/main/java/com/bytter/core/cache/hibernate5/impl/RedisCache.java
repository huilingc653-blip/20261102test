package com.bytter.core.cache.hibernate5.impl;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnection;

/**
 * Created by zhoul on 2017/12/28.
 */
public class RedisCache extends org.springframework.data.redis.cache.RedisCache {
    private String channel;
    private RedisOperations<?, ?> redisOperations;
    private RedisConnectionFactory connectionFactory;

    public RedisCache(String name, byte[] prefix, RedisOperations<?, ?> redisOperations, long expiration) {
        this(name, prefix, redisOperations, ((RedisTemplate) redisOperations).getConnectionFactory(), expiration);
    }

    public RedisCache(String name, byte[] prefix, RedisOperations<?, ?> redisOperations, RedisConnectionFactory connectionFactory, long expiration) {
        super(name, createRedisCacheWriter(connectionFactory), createRedisCacheConfiguration(name, prefix, expiration));
        this.redisOperations = redisOperations;
        this.connectionFactory = connectionFactory;
    }

    private static RedisCacheWriter createRedisCacheWriter(RedisConnectionFactory connectionFactory) {
        return RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
    }

    private static RedisCacheConfiguration createRedisCacheConfiguration(String name, byte[] prefix, long expiration) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .computePrefixWith(cacheName -> new String(prefix) + cacheName)
                .entryTtl(java.time.Duration.ofSeconds(expiration));
        return config;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public RedisOperations<?, ?> getRedisOperations() {
        return redisOperations;
    }

    /**
     * Refresh the entry using the same key conversion and serialization as RedisCache.
     * A zero TTL denotes an eternal entry and must never issue EXPIRE 0.
     */
    public void refreshExpiration(Object key, long expiration) {
        if (expiration <= 0) {
            return;
        }
        byte[] cacheKey = serializeCacheKey(createCacheKey(key));
        try (RedisConnection connection = connectionFactory.getConnection()) {
            connection.expire(cacheKey, expiration);
        }
    }

    public RedisConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }
}
