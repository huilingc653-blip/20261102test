package com.bytter.cloud.auth.cache;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.bytter.cloud.auth.persistence.ApiResourceDto;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * 登录即可拥有的资源
 *
 * @author liuwf 2021-08-04
 * @since 1.0.0
 */
@Service
public class UserLoginResourceCache {
    @CreateCache(name = AuthCacheKey.USER_LOGIN_RESOURCE, cacheType = CacheType.REMOTE, expire = 14 * 60 * 60)
    private Cache<String, List<ApiResourceDto>> userLoginResourceCache;
    private static final String KEY = "public";
    public void put(List<ApiResourceDto> value) {
        userLoginResourceCache.put(KEY, value);
    }
    public void remove() {
        userLoginResourceCache.remove(KEY);
    }
    public List<ApiResourceDto> get() {
        return userLoginResourceCache.get(KEY);
    }
}
