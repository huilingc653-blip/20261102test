package com.bytter.core.cache.hibernate5.internal.strategy;
import com.bytter.core.cache.hibernate5.impl.RedisCache;
import com.bytter.core.cache.hibernate5.internal.regions.EhcacheRedisEntityRegion;
import net.sf.ehcache.Ehcache;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.internal.DefaultCacheKeysFactory;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;
/**
 * JTA EntityRegionAccessStrategy.
 *
 * @author Chris Dennis
 * @author Ludovic Orban
 * @author Alex Snaps
 */
public class TransactionalEhcacheRedisEntityRegionAccessStrategy
        extends AbstractTransactionalEhcacheRedisAccessStrategy<EhcacheRedisEntityRegion>
        implements EntityRegionAccessStrategy {
    /**
     * Construct a new entity region access strategy.
     *
     * @param region
     *            the Hibernate region.
     * @param ehcache
     *            the cache.
     * @param settings
     *            the Hibernate settings.
     */
    public TransactionalEhcacheRedisEntityRegionAccessStrategy(EhcacheRedisEntityRegion region, Ehcache ehcache,
            RedisCache redisCache, SessionFactoryOptions settings) {
        super(region, ehcache, redisCache, settings);
    }
    @Override
    public boolean afterInsert(SessionImplementor session, Object key, Object value, Object version) {
        return false;
    }
    @Override
    public boolean afterUpdate(SessionImplementor session, Object key, Object value, Object currentVersion,
            Object previousVersion, SoftLock lock) {
        return false;
    }
    @Override
    public EntityRegion getRegion() {
        return region();
    }
    @Override
    public boolean insert(SessionImplementor session, Object key, Object value, Object version) throws CacheException {
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
    public boolean update(SessionImplementor session, Object key, Object value, Object currentVersion,
            Object previousVersion) throws CacheException {
        put(key, value);
        return true;
    }
    @Override
    public Object generateCacheKey(Object id, EntityPersister persister, SessionFactoryImplementor factory,
            String tenantIdentifier) {
        return DefaultCacheKeysFactory.staticCreateEntityKey(id, persister, factory, tenantIdentifier);
    }
    @Override
    public Object getCacheKeyId(Object cacheKey) {
        return DefaultCacheKeysFactory.staticGetEntityId(cacheKey);
    }
}
