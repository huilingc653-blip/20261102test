package com.bytter.core.cache.listener;
import com.bytter.core.cache.EhcacheRedisCommand;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.ehcache.EhCacheCache;
import java.io.Serializable;
/**
 * Redis二级缓存订阅通知Ehcache操作对应的缓存
 */
public abstract class AbstractCacheMessageDelegateListener implements IMessageDelegate {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private String channel;
    public AbstractCacheMessageDelegateListener() {
    }
    public AbstractCacheMessageDelegateListener(String channel) {
        this.channel = channel;
    }
    @Override
    public void handleMessage(Serializable message, String channel) {
        if (!channel.equals(getChannel()) && !(message instanceof EhcacheRedisCommand)) {
            return;
        }
        EhcacheRedisCommand ehcacheRedisCommand = (EhcacheRedisCommand) message;
        if (ehcacheRedisCommand.isLocalCommand()) {
            return;
        }
        Ehcache ehcache = getEhcacheCacheManager().getEhcache(ehcacheRedisCommand.getRegion());
        EhCacheCache ehCacheCache = new EhCacheCache(ehcache);
        switch (ehcacheRedisCommand.getOperator()) {
        // 删除缓存
        case EhcacheRedisCommand.OPERATOR_DELETE_KEY:
            ehCacheCache.evict(ehcacheRedisCommand.getKey());
            if (logger.isDebugEnabled()) {
                logger.debug("Channel:" + getChannel() + ",Operator:evict ehcache, Source:"
                        + ehcacheRedisCommand.getSrc() + " Region:" + ehcacheRedisCommand.getRegion() + ",Key:"
                        + ehcacheRedisCommand.getKey());
            }
            break;
        // 清除指定区域缓存
        case EhcacheRedisCommand.OPERATOR_CLEAR_KEY:
            ehCacheCache.clear();
            if (logger.isDebugEnabled()) {
                logger.debug("Channel:" + getChannel() + ",Operator:clear ehcache, Source:"
                        + ehcacheRedisCommand.getSrc() + " Region:" + ehcacheRedisCommand.getRegion());
            }
            break;
        }
    }
    public String getChannel() {
        return channel;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public abstract CacheManager getEhcacheCacheManager();
}
