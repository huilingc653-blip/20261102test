package com.bytter.core.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    public void put(String key, Object value) {
        redisTemplate.opsForValue().set(key, value, 120 * 1000, TimeUnit.SECONDS);
    }

    public void putForLongTime(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 向Redis中存储一个键值对，并设置过期时间
     *
     * @param key   存储的键名（不能为null）
     * @param value 存储的值（会自动进行序列化）
     * @param ex    过期时间，单位：秒
     */
    public void put(String key, Object value, long ex) {
        redisTemplate.opsForValue().set(key, value, ex, TimeUnit.SECONDS);
    }

    /**
     * 向Redis中的Hash结构存储一个键值对
     *
     * @param key     Redis中Hash的键（相当于数据库中的表名）
     * @param itemKey Hash中的字段名（相当于数据库中的主键/字段）
     * @param value   要存储的值（可以是任意序列化的Java对象）
     * @param ex      过期时间
     */
    public void hashPut(String key, String itemKey, Object value, long ex) {
        redisTemplate.opsForHash().put(key, itemKey, value);
        if (ex > 0) {
            redisTemplate.expire(key, ex, TimeUnit.SECONDS);
        }
    }

    public void remove(String key) {
        redisTemplate.delete(key);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Object hashGet(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    public void increase(String key) {
        Integer integer = (Integer) get(key);
        put(key, ++integer);
    }

    /**
     * 锁定用户
     *
     * @param userId     用户ID
     * @param expiration 过期时间 秒
     */
    public void lockUser(String userId, long expiration) {
        redisTemplate.opsForValue().set(userId, String.valueOf(expiration), expiration * 60, TimeUnit.SECONDS);
    }

    /**
     * 用户是否锁定
     *
     * @param userId 用户ID
     * @return true是 false否
     */
    public boolean isLockedUser(String userId) {
        return StringUtils.isNotBlank((String) get(userId));
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
