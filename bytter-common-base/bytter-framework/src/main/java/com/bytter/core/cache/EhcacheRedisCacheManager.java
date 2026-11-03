package com.bytter.core.cache;
import com.bytter.core.cache.hibernate5.impl.RedisCache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Status;
import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.cache.ehcache.EhCacheManagerUtils;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.StringUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
/**
 * 二级缓存管理类 L1:Ehcache，L2:Redis Created by zhoul on 2017/12/20.
 */
public class EhcacheRedisCacheManager extends AbstractTransactionSupportingCacheManager
        implements IEhcacheRedisCacheManager {
    private Map<String, Object> redisProperties = new HashMap<>();
    /**
     * 是否使用Redis作为二级缓存
     */
    private boolean useRedis;
    /**
     * 是否使用Redis缓存的前缀
     */
    private boolean redisUsePrefix;
    /**
     * 自定义Redis缓存的前缀
     */
    private String redisCachePrefix;
    /**
     * 定义Redis的订阅、广播频道
     */
    private String redisChannel;
    /**
     * Ehcache缓存管理器
     */
    private CacheManager ehcacheManager;
    /**
     * Redis缓存管理器
     */
    private RedisOperations redisOperations;
    public EhcacheRedisCacheManager() {
    }
    public EhcacheRedisCacheManager(CacheManager ehcacheManager) {
        this.ehcacheManager = ehcacheManager;
    }
    public EhcacheRedisCacheManager(CacheManager ehcacheManager, RedisOperations redisOperations) {
        this.ehcacheManager = ehcacheManager;
        this.redisOperations = redisOperations;
    }
    public void setEhcacheManager(CacheManager ehcacheManager) {
        this.ehcacheManager = ehcacheManager;
    }
    public CacheManager getEhcacheManager() {
        return this.ehcacheManager;
    }
    @Override
    public void afterPropertiesSet() {
        if (getEhcacheManager() == null) {
            setEhcacheManager(EhCacheManagerUtils.buildCacheManager());
        }
        super.afterPropertiesSet();
    }
    @SuppressWarnings("unchecked")
    @Override
    protected Collection<Cache> loadCaches() {
        Status status = getEhcacheManager().getStatus();
        if (!Status.STATUS_ALIVE.equals(status)) {
            throw new IllegalStateException(
                    "An 'alive' EhCache CacheManager is required - current cache is " + status.toString());
        }
        String[] names = getEhcacheManager().getCacheNames();
        Collection<Cache> caches = new LinkedHashSet<>(names.length);
        for (String cacheName : names) {
            EhcacheRedisCache ehcacheRedisCache = setEhcacheRedisCacheProperties(cacheName);
            caches.add(ehcacheRedisCache);
        }
        return caches;
    }
    private EhcacheRedisCache setEhcacheRedisCacheProperties(String cacheName) {
        net.sf.ehcache.Cache cache = getEhcacheManager().getCache(cacheName);
        EhcacheRedisCache ehcacheRedisCache = new EhcacheRedisCache(new EhCacheCache(cache));
        if (getUseRedis()) {
            CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
            String redisCachePrefix = makeRedisCachePrefix(cacheName);
            RedisCache redisCache = new RedisCache(cacheName, redisCachePrefix.getBytes(), getRedisOperations(),
                    cacheConfiguration.getTimeToLiveSeconds());
            ehcacheRedisCache.setDefaultRedisExpiration(cacheConfiguration.getTimeToLiveSeconds());
            // 设置Redis前缀
            ehcacheRedisCache.setRedisCacheFullPrefix(redisCachePrefix);
            // 设置Redis缓存处理类
            ehcacheRedisCache.setRedisCache(redisCache);
            // 设置Redis的广播频道
            ehcacheRedisCache.setRedisChannel(getRedisChannel());
        }
        return ehcacheRedisCache;
    }
    private String makeRedisCachePrefix(String cacheName) {
        if (getRedisUsePrefix() && !StringUtils.isEmpty(getRedisCachePrefix())) {
            cacheName = getRedisCachePrefix() + ":" + cacheName;
        }
        return cacheName + ":";
    }
    @Override
    protected Cache getMissingCache(String name) {
        ehcacheManager.addCache(name);
        return setEhcacheRedisCacheProperties(name);
    }
    public boolean getUseRedis() {
        return useRedis;
    }
    public void setUseRedis(boolean useRedis) {
        this.useRedis = useRedis;
    }
    public RedisOperations getRedisOperations() {
        return redisOperations;
    }
    public void setRedisOperations(RedisOperations redisOperations) {
        this.redisOperations = redisOperations;
    }
    public Map<String, Object> getRedisProperties() {
        return redisProperties;
    }
    public void setRedisProperties(Map<String, Object> redisProperties) {
        this.redisProperties = redisProperties;
    }
    public boolean getRedisUsePrefix() {
        return redisUsePrefix;
    }
    public void setRedisUsePrefix(boolean redisUsePrefix) {
        this.redisUsePrefix = redisUsePrefix;
    }
    public String getRedisCachePrefix() {
        return redisCachePrefix;
    }
    public void setRedisCachePrefix(String redisCachePrefix) {
        this.redisCachePrefix = redisCachePrefix;
    }
    public String getRedisChannel() {
        return redisChannel;
    }
    public void setRedisChannel(String redisChannel) {
        this.redisChannel = redisChannel;
    }
    @Override
    public CacheManager getEhcacheCacheManager() {
        return getEhcacheManager();
    }
    @Override
    public RedisOperations getRedisCacheManager() {
        return getRedisOperations();
    }
}
