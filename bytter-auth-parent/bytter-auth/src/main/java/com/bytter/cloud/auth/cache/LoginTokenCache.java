package com.bytter.cloud.auth.cache;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
/**
 * 登录的token
 *
 * @author zhoul 2021-03-05
 * @since 1.0.0
 */
public class LoginTokenCache {
    @CreateCache(name = AuthCacheKey.LOGIN_TOKEN, cacheType = CacheType.REMOTE, expire = 600)
    private Cache<String, String> loginTokenCache;
    public void put(String key, String value) {
        loginTokenCache.put(key, value);
    }
    public void remove(String key) {
        loginTokenCache.remove(key);
    }
    public String get(String key) {
        return loginTokenCache.get(key);
    }
}
