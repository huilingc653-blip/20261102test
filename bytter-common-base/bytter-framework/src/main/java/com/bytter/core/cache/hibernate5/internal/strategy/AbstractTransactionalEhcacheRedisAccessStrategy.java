package com.bytter.core.cache.hibernate5.internal.strategy;
import com.bytter.core.cache.hibernate5.impl.RedisCache;
import com.bytter.core.cache.hibernate5.internal.regions.EhcacheRedisTransactionalDataRegion;
import com.bytter.core.cache.hibernate5.internal.util.EhcacheRedisUtil;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.cache.Cache;
/**
 * Created by zhoul on 2017/12/27.
 */
public class AbstractTransactionalEhcacheRedisAccessStrategy<T extends EhcacheRedisTransactionalDataRegion>
        extends AbstractEhcacheRedisAccessStrategy<T> {
    protected final Ehcache ehcache;
    protected final RedisCache redisCache;
    /**
     * Create an access strategy wrapping the given region.
     *
     * @param region
     *            The wrapped region. Accessible to subclasses via {@link #region()}
     * @param settings
     *            The Hibernate settings. Accessible to subclasses via {@link #settings()}
     */
    AbstractTransactionalEhcacheRedisAccessStrategy(T region, Ehcache ehcache, RedisCache redisCache,
            SessionFactoryOptions settings) {
        super(region, settings);
        this.ehcache = ehcache;
        this.redisCache = redisCache;
    }
    protected void put(Object key, Object value) {
        try {
            ehcache.put(new Element(key, value));
            if (redisCache != null) {
                redisCache.put(key, value);
            }
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException(e);
        }
    }
    @Override
    public boolean putFromLoad(SessionImplementor session, Object key, Object value, long txTimestamp, Object version,
            boolean minimalPutOverride) throws CacheException {
        if (minimalPutOverride && ehcache.get(key) != null) {
            return false;
        }
        put(key, value);
        return true;
    }
    public Object get(SessionImplementor session, Object key, long txTimestamp) throws CacheException {
        try {
            final Element element = ehcache.get(key);
            if (element != null) {
                return element.getObjectValue();
            }
            if (redisCache != null) {
                Cache.ValueWrapper valueWrapper = redisCache.get(key);
                if (valueWrapper != null) {
                    return EhcacheRedisUtil.putIfEhcacheNotExsit(valueWrapper, key, ehcache, redisCache);
                }
                return null;
            }
            return null;
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException(e);
        }
    }
    @Override
    public void remove(SessionImplementor session, Object key) throws CacheException {
        try {
            ehcache.remove(key);
            if (redisCache != null) {
                redisCache.evict(key);
                EhcacheRedisUtil.sendEvict(redisCache, key);
            }
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException(e);
        }
    }
}
