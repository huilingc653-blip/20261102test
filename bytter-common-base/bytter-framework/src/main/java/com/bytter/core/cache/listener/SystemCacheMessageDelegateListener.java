package com.bytter.core.cache.listener;
import com.bytter.core.cache.IEhcacheRedisCacheManager;
import net.sf.ehcache.CacheManager;
/**
 * Redis二级缓存订阅通知Ehcache操作对应的缓存
 */
public class SystemCacheMessageDelegateListener extends AbstractCacheMessageDelegateListener {
    private IEhcacheRedisCacheManager ehcacheRedisCacheManager;
    @Override
    public CacheManager getEhcacheCacheManager() {
        IEhcacheRedisCacheManager ehcacheRedisCacheManager = getEhcacheRedisCacheManager();
        return ehcacheRedisCacheManager.getEhcacheCacheManager();
    }
    public IEhcacheRedisCacheManager getEhcacheRedisCacheManager() {
        return ehcacheRedisCacheManager;
    }
    public void setEhcacheRedisCacheManager(IEhcacheRedisCacheManager ehcacheRedisCacheManager) {
        this.ehcacheRedisCacheManager = ehcacheRedisCacheManager;
    }
}
