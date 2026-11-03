package com.bytter.core.cache.hibernate5.internal.regions;
import com.bytter.core.cache.hibernate5.impl.RedisCache;
import com.bytter.core.cache.hibernate5.internal.nonstop.HibernateNonstopCacheExceptionHandler;
import com.bytter.core.cache.hibernate5.internal.strategy.EhcacheRedisAccessStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.constructs.nonstop.NonStopCacheException;
import net.sf.ehcache.util.Timestamper;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.Region;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
/**
 * An Ehcache specific data region implementation.
 * <p>
 * This class is the ultimate superclass for all Ehcache Hibernate cache regions.
 *
 * @author Chris Dennis
 * @author Greg Luck
 * @author Emmanuel Bernard
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 */
@Slf4j
public abstract class EhcacheRedisDataRegion implements Region {
    private static final String CACHE_LOCK_TIMEOUT_PROPERTY = "net.sf.ehcache.hibernate.cache_lock_timeout";
    private static final int DEFAULT_CACHE_LOCK_TIMEOUT = 60000;
    private final Ehcache cache;
    private final RedisCache redisCache;
    private final EhcacheRedisAccessStrategyFactory accessStrategyFactory;
    private final int cacheLockTimeout;
    /**
     * Create a Hibernate data region backed by the given Ehcache instance.
     */
    EhcacheRedisDataRegion(EhcacheRedisAccessStrategyFactory accessStrategyFactory, Ehcache cache,
            RedisCache redisCache, Properties properties) {
        this.accessStrategyFactory = accessStrategyFactory;
        this.cache = cache;
        this.redisCache = redisCache;
        final String timeout = properties.getProperty(CACHE_LOCK_TIMEOUT_PROPERTY,
                Integer.toString(DEFAULT_CACHE_LOCK_TIMEOUT));
        this.cacheLockTimeout = Timestamper.ONE_MS * Integer.decode(timeout);
    }
    /**
     * Ehcache instance backing this Hibernate data region.
     *
     * @return Ehcache
     */
    protected Ehcache getCache() {
        return cache;
    }
    /**
     * The {@link EhcacheRedisAccessStrategyFactory} used for creating various access strategies
     *
     * @return EhcacheRedisAccessStrategyFactory
     */
    protected EhcacheRedisAccessStrategyFactory getAccessStrategyFactory() {
        return accessStrategyFactory;
    }
    /**
     * Return the Ehcache instance backing this Hibernate data region.
     *
     * @return The underlying ehcache cache
     */
    public Ehcache getEhcache() {
        return getCache();
    }
    public RedisCache getRedisCache() {
        return redisCache;
    }
    public boolean isUseRedis() {
        return redisCache != null;
    }
    @Override
    public String getName() {
        return getCache().getName();
    }
    @Override
    public void destroy() throws CacheException {
        try {
            getCache().getCacheManager().removeCache(getCache().getName());
        } catch (IllegalStateException e) {
            // When Spring and Hibernate are both involved this will happen in normal shutdown operation.
            // Do not throw an exception, simply log this one.
            log.debug("This can happen if multiple frameworks both try to shutdown ehcache", e);
        } catch (net.sf.ehcache.CacheException e) {
            if (e instanceof NonStopCacheException) {
                HibernateNonstopCacheExceptionHandler.getInstance()
                        .handleNonstopCacheException((NonStopCacheException) e);
            } else {
                throw new CacheException(e);
            }
        }
    }
    @Override
    public long getSizeInMemory() {
        try {
            return getCache().calculateInMemorySize();
        } catch (Throwable t) {
            if (t instanceof NonStopCacheException) {
                HibernateNonstopCacheExceptionHandler.getInstance()
                        .handleNonstopCacheException((NonStopCacheException) t);
            }
            return -1;
        }
    }
    @Override
    public long getElementCountInMemory() {
        try {
            return getCache().getMemoryStoreSize();
        } catch (net.sf.ehcache.CacheException ce) {
            if (ce instanceof NonStopCacheException) {
                HibernateNonstopCacheExceptionHandler.getInstance()
                        .handleNonstopCacheException((NonStopCacheException) ce);
                return -1;
            } else {
                throw new CacheException(ce);
            }
        }
    }
    @Override
    public long getElementCountOnDisk() {
        try {
            return getCache().getDiskStoreSize();
        } catch (net.sf.ehcache.CacheException ce) {
            if (ce instanceof NonStopCacheException) {
                HibernateNonstopCacheExceptionHandler.getInstance()
                        .handleNonstopCacheException((NonStopCacheException) ce);
                return -1;
            } else {
                throw new CacheException(ce);
            }
        }
    }
    @Override
    public Map toMap() {
        try {
            final Map<Object, Object> result = new HashMap<Object, Object>();
            for (Object key : getCache().getKeys()) {
                result.put(key, getCache().get(key).getObjectValue());
            }
            return result;
        } catch (Exception e) {
            if (e instanceof NonStopCacheException) {
                HibernateNonstopCacheExceptionHandler.getInstance()
                        .handleNonstopCacheException((NonStopCacheException) e);
                return Collections.emptyMap();
            } else {
                throw new CacheException(e);
            }
        }
    }
    @Override
    public long nextTimestamp() {
        return Timestamper.next();
    }
    @Override
    public int getTimeout() {
        return cacheLockTimeout;
    }
    @Override
    public boolean contains(Object key) {
        return getCache().isKeyInCache(key);
    }
}
