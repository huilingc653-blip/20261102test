package com.bytter.core.cache.hibernate5.internal.util;
import com.bytter.core.cache.EhcacheRedisCommand;
import com.bytter.core.cache.hibernate5.impl.RedisCache;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisOperations;
/**
 * Created by zhoul on 2017/12/28.
 */
public class EhcacheRedisUtil {
    public static Object putIfEhcacheNotExsit(Cache.ValueWrapper valueWrapper, Object key, Ehcache ehcache,
            RedisCache redisCache) {
        Object value = valueWrapper.get();
        Element newElement = new Element(key, value);
        ehcache.put(newElement);
        redisCache.refreshExpiration(key, newElement.getTimeToLive());
        return value;
    }
    public static void sendEvict(RedisCache redisCache, Object key) {
        RedisOperations redisOperations = redisCache.getRedisOperations();
        redisOperations.convertAndSend(redisCache.getChannel(),
                new EhcacheRedisCommand(EhcacheRedisCommand.OPERATOR_DELETE_KEY, redisCache.getName(), key));
    }
    public static void sendClear(RedisCache redisCache) {
        RedisOperations redisOperations = redisCache.getRedisOperations();
        redisOperations.convertAndSend(redisCache.getChannel(),
                new EhcacheRedisCommand(EhcacheRedisCommand.OPERATOR_CLEAR_KEY, redisCache.getName(), null));
    }
}
