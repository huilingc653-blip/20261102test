package com.bytter.core.cache.hibernate5.internal.strategy;
import com.bytter.core.cache.hibernate5.impl.RedisCache;
import com.bytter.core.cache.hibernate5.internal.regions.EhcacheRedisNaturalIdRegion;
import net.sf.ehcache.Ehcache;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.internal.DefaultCacheKeysFactory;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;
/**
 * JTA NaturalIdRegionAccessStrategy.
 *
 * @author Chris Dennis
 * @author Ludovic Orban
 * @author Alex Snaps
 */
public class TransactionalEhcacheRedisNaturalIdRegionAccessStrategy
        extends AbstractTransactionalEhcacheRedisAccessStrategy<EhcacheRedisNaturalIdRegion>
        implements NaturalIdRegionAccessStrategy {
    /**
     * Construct a new collection region access strategy.
     *
     * @param region
     *            the Hibernate region.
     * @param ehcache
     *            the cache.
     * @param settings
     *            the Hibernate settings.
     */
    public TransactionalEhcacheRedisNaturalIdRegionAccessStrategy(EhcacheRedisNaturalIdRegion region, Ehcache ehcache,
            RedisCache redisCache, SessionFactoryOptions settings) {
        super(region, ehcache, redisCache, settings);
    }
    @Override
    public boolean afterInsert(SessionImplementor session, Object key, Object value) {
        return false;
    }
    @Override
    public boolean afterUpdate(SessionImplementor session, Object key, Object value, SoftLock lock) {
        return false;
    }
    @Override
    public NaturalIdRegion getRegion() {
        return region();
    }
    @Override
    public boolean insert(SessionImplementor session, Object key, Object value) throws CacheException {
        // OptimisticCache? versioning?
        put(key, value);
        return true;
    }
    @Override
    public SoftLock lockItem(SessionImplementor session, Object key, Object version) throws CacheException {
        return null;
    }
    @Override
    public void unlockItem(SessionImplementor session, Object key, SoftLock lock) throws CacheException {
        // no-op
    }
    @Override
    public boolean update(SessionImplementor session, Object key, Object value) throws CacheException {
        put(key, value);
        return true;
    }
    @Override
    public Object generateCacheKey(Object[] naturalIdValues, EntityPersister persister, SessionImplementor session) {
        return DefaultCacheKeysFactory.staticCreateNaturalIdKey(naturalIdValues, persister, session);
    }
    @Override
    public Object[] getNaturalIdValues(Object cacheKey) {
        return DefaultCacheKeysFactory.staticGetNaturalIdValues(cacheKey);
    }
}
