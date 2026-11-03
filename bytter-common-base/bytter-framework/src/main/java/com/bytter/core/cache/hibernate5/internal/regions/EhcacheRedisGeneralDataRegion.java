package com.bytter.core.cache.hibernate5.internal.regions;
import com.bytter.core.cache.hibernate5.impl.RedisCache;
import com.bytter.core.cache.hibernate5.internal.nonstop.HibernateNonstopCacheExceptionHandler;
import com.bytter.core.cache.hibernate5.internal.strategy.EhcacheRedisAccessStrategyFactory;
import com.bytter.core.cache.hibernate5.internal.util.EhcacheRedisUtil;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.nonstop.NonStopCacheException;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.GeneralDataRegion;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.cache.Cache;
import java.util.Properties;
/**
 * An Ehcache specific GeneralDataRegion.
 * <p>
 * GeneralDataRegion instances are used for both the timestamps and query caches.
 *
 * @author Chris Dennis
 * @author Greg Luck
 * @author Emmanuel Bernard
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 */
abstract class EhcacheRedisGeneralDataRegion extends EhcacheRedisDataRegion implements GeneralDataRegion {
    /**
     * Constructs an EhcacheGeneralDataRegion around the given underlying cache.
     *
     * @param accessStrategyFactory
     *            The factory for building needed RegionAccessStrategy instance
     * @param underlyingCache
     *            The ehcache cache instance
     * @param properties
     *            Any additional[ properties
     */
    public EhcacheRedisGeneralDataRegion(EhcacheRedisAccessStrategyFactory accessStrategyFactory,
            Ehcache underlyingCache, RedisCache redisCache, Properties properties) {
        super(accessStrategyFactory, underlyingCache, redisCache, properties);
    }
    @Override
    public Object get(SessionImplementor session, Object key) throws CacheException {
        try {
            // LOG.debugf( "key: %s", key );
            if (key == null) {
                return null;
            } else {
                final Element element = getCache().get(key);
                if (element == null) {
                    // LOG.debugf( "Element for key %s is null", key );
                    if (isUseRedis()) {
                        Cache.ValueWrapper valueWrapper = getRedisCache().get(key);
                        if (valueWrapper != null) {
                            return EhcacheRedisUtil.putIfEhcacheNotExsit(valueWrapper, key, getEhcache(),
                                    getRedisCache());
                        }
                    }
                    return null;
                } else {
                    return element.getObjectValue();
                }
            }
        } catch (net.sf.ehcache.CacheException e) {
            if (e instanceof NonStopCacheException) {
                HibernateNonstopCacheExceptionHandler.getInstance()
                        .handleNonstopCacheException((NonStopCacheException) e);
                return null;
            } else {
                throw new CacheException(e);
            }
        }
    }
    @Override
    public void put(SessionImplementor session, Object key, Object value) throws CacheException {
        // LOG.debugf( "key: %s value: %s", key, value );
        try {
            final Element element = new Element(key, value);
            getCache().put(element);
            if (isUseRedis()) {
                getRedisCache().put(key, value);
            }
        } catch (IllegalArgumentException e) {
            throw new CacheException(e);
        } catch (IllegalStateException e) {
            throw new CacheException(e);
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
    public void evict(Object key) throws CacheException {
        try {
            getCache().remove(key);
            if (isUseRedis()) {
                getRedisCache().evict(key);
                EhcacheRedisUtil.sendEvict(getRedisCache(), key);
            }
        } catch (ClassCastException e) {
            throw new CacheException(e);
        } catch (IllegalStateException e) {
            throw new CacheException(e);
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
    public void evictAll() throws CacheException {
        try {
            getCache().removeAll();
            if (isUseRedis()) {
                getRedisCache().clear();
                EhcacheRedisUtil.sendClear(getRedisCache());
            }
        } catch (IllegalStateException e) {
            throw new CacheException(e);
        } catch (net.sf.ehcache.CacheException e) {
            if (e instanceof NonStopCacheException) {
                HibernateNonstopCacheExceptionHandler.getInstance()
                        .handleNonstopCacheException((NonStopCacheException) e);
            } else {
                throw new CacheException(e);
            }
        }
    }
}
