package com.bytter.core.cache;
import net.sf.ehcache.CacheManager;
import org.springframework.data.redis.core.RedisOperations;
/**
 * Created by zhoul on 2017/12/22.
 */
public interface IEhcacheRedisCacheManager extends org.springframework.cache.CacheManager {
    CacheManager getEhcacheCacheManager();
    RedisOperations getRedisCacheManager();
}
