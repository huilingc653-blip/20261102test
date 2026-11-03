package com.bytter.cloud.auth.cache;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.concurrent.TimeUnit;
/**
 * 登录的锁定用户的缓存
 *
 * @author huangw 2021-08-18
 * @since 1.0.0
 */
public class LoginLockCache {
    @CreateCache(name = AuthCacheKey.LOGIN_LOCK, cacheType = CacheType.REMOTE, expire = 1800)
    private Cache<String, Integer> loginLockCache;
    /**
     * 锁定时间
     */
    @CreateCache(name = AuthCacheKey.LOGIN_LOCK_TIME, cacheType = CacheType.REMOTE)
    private Cache<String, String> loginLockTime;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    public void put(String key, Integer value) {
        loginLockCache.put(key, value);
    }
    public void remove(String key) {
        loginLockCache.remove(key);
    }
    public Integer get(String key) {
        return loginLockCache.get(key);
    }
    public void increase(String key) {
        Integer integer = get(key);
        put(key, ++integer);
    }
    /**
     * 锁定用户
     * @param userId 用户ID
     * @param expiration 过期时间 秒
     */
    public void lockUser(String userId, long expiration) {
        loginLockTime.put(userId, String.valueOf(expiration), expiration * 60, TimeUnit.SECONDS);
    }
    /**
     * 用户是否锁定
     * @param userId 用户ID
     * @return true是 false否
     */
    public boolean isLockedUser(String userId) {
        return StringUtils.isNotBlank(loginLockTime.get(userId));
    }
    /**
     * 剩余锁定时间
     *
     * @param userId 用户ID
     * @return 剩余锁定时间
     */
    public Long remainingLockUserTime(String userId) {
        return redisTemplate.opsForValue().getOperations().getExpire(AuthCacheKey.LOGIN_LOCK_TIME + userId, TimeUnit.MINUTES);
    }
}
