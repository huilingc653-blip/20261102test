package com.bytter.core.cache;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCache;
import com.bytter.core.cache.hibernate5.impl.RedisCache;
import org.springframework.data.redis.core.RedisOperations;
import java.util.concurrent.Callable;
/**
 * Ehcache和Redis二级缓存实现 Created by zhoul on 2017/12/20.
 */
public class EhcacheRedisCache implements Cache {
    /**
     * Redis订阅、广播频道
     */
    private String redisChannel;
    private EhCacheCache ehcache;
    private RedisCache redisCache;
    private long defaultRedisExpiration = 0;
    /**
     * 自定义Redis缓存的前缀
     */
    private String redisCacheFullPrefix;
    public EhcacheRedisCache(EhCacheCache ehcache) {
        this.ehcache = ehcache;
    }
    @Override
    public String getName() {
        return ehcache.getName();
    }
    @Override
    public Object getNativeCache() {
        return this;
    }
    /**
     * 发送广播
     *
     * @param operator
     *            操作符
     * @param key
     *            缓存的KEY
     */
    private void convertAndSend(byte operator, Object key) {
        RedisOperations redisOperations = redisCache.getRedisOperations();
        redisOperations.convertAndSend(getRedisChannel(), new EhcacheRedisCommand(operator, redisCache.getName(), key));
    }
    /**
     * 当L1缓存不存在时，则到L2获取缓存数据。此时更新L2缓存数据的时间
     *
     * @param key
     *            缓存KEY值
     * @param value
     *            更新的VALUE
     */
    private void updateLocalEhcacheAndRedisExpiration(Object key, Object value) {
        if (value == null) {
            return;
        }
        ehcache.put(key, value);
        redisCache.refreshExpiration(key, defaultRedisExpiration);
    }
    @Override
    public ValueWrapper get(Object key) {
        ValueWrapper valueWrapper = ehcache.get(key);
        if (valueWrapper == null && redisCache != null) {
            valueWrapper = redisCache.get(key);
            if (valueWrapper != null) {
                updateLocalEhcacheAndRedisExpiration(key, valueWrapper.get());
            }
        }
        return valueWrapper;
    }
    @Override
    public <T> T get(Object key, Class<T> type) {
        T t = ehcache.get(key, type);
        if (t == null && redisCache != null) {
            t = redisCache.get(key, type);
            updateLocalEhcacheAndRedisExpiration(key, t);
        }
        return t;
    }
    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        T t = ehcache.get(key, valueLoader);
        if (t == null && redisCache != null) {
            t = redisCache.get(key, valueLoader);
            updateLocalEhcacheAndRedisExpiration(key, t);
        }
        return t;
    }
    @Override
    public void put(Object key, Object value) {
        ehcache.put(key, value);
        if (redisCache != null) {
            redisCache.put(key, value);
        }
    }
    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        ValueWrapper valueWrapper;
        if (redisCache == null) {
            valueWrapper = ehcache.putIfAbsent(key, value);
        } else {
            ehcache.putIfAbsent(key, value);
            valueWrapper = redisCache.putIfAbsent(key, value);
        }
        return valueWrapper;
    }
    @Override
    public void evict(Object key) {
        ehcache.evict(key);
        if (redisCache != null) {
            redisCache.evict(key);
            // 发送广播
            convertAndSend(EhcacheRedisCommand.OPERATOR_DELETE_KEY, key);
        }
    }
    @Override
    public void clear() {
        ehcache.clear();
        if (redisCache != null) {
            redisCache.clear();
            // 发送广播
            convertAndSend(EhcacheRedisCommand.OPERATOR_CLEAR_KEY, null);
        }
    }
    public String getRedisChannel() {
        return redisChannel;
    }
    public void setRedisChannel(String redisChannel) {
        this.redisChannel = redisChannel;
    }
    public EhCacheCache getEhcache() {
        return ehcache;
    }
    public void setEhcache(EhCacheCache ehcache) {
        this.ehcache = ehcache;
    }
    public RedisCache getRedisCache() {
        return redisCache;
    }
    public void setRedisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
    }
    public String getRedisCacheFullPrefix() {
        return redisCacheFullPrefix;
    }
    public void setRedisCacheFullPrefix(String redisCacheFullPrefix) {
        this.redisCacheFullPrefix = redisCacheFullPrefix;
    }
    public long getDefaultRedisExpiration() {
        return defaultRedisExpiration;
    }
    public void setDefaultRedisExpiration(long defaultRedisExpiration) {
        this.defaultRedisExpiration = defaultRedisExpiration;
    }
}
