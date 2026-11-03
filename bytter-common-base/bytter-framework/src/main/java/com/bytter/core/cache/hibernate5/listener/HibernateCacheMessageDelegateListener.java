package com.bytter.core.cache.hibernate5.listener;
import com.bytter.core.cache.listener.AbstractCacheMessageDelegateListener;
import net.sf.ehcache.CacheManager;
/**
 * Redis二级缓存订阅通知Ehcache操作对应的缓存
 */
public class HibernateCacheMessageDelegateListener extends AbstractCacheMessageDelegateListener {
    private CacheManager ehcacheCacheManager;
    public HibernateCacheMessageDelegateListener(String channel, CacheManager ehcacheCacheManager) {
        super(channel);
        this.ehcacheCacheManager = ehcacheCacheManager;
    }
    @Override
    public CacheManager getEhcacheCacheManager() {
        return ehcacheCacheManager;
    }
}
