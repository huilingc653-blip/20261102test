package com.bytter.cloud.auth.cache;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.bytter.cloud.auth.persistence.ApiResourceDto;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * 用户拥有的资源
 *
 * @author liuwf 2021-08-04
 * @since 1.0.0
 */
@Service
public class UserResourceCache {
    @CreateCache(name = AuthCacheKey.USER_RESOURCE, cacheType = CacheType.REMOTE, expire = 12 * 60 * 60)
    private Cache<String, List<ApiResourceDto>> userResourceCache;
    public void put(String key, List<ApiResourceDto> value) {
        userResourceCache.put(key, value);
    }
    public void remove(String key) {
        userResourceCache.remove(key);
    }
    public List<ApiResourceDto>  get(String key) {
        return userResourceCache.get(key);
    }
}
