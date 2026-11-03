package com.bytter.core.cache.hibernate5.internal.strategy;
import com.bytter.core.cache.hibernate5.impl.RedisCache;
import com.bytter.core.cache.hibernate5.internal.regions.EhcacheRedisCollectionRegion;
import net.sf.ehcache.Ehcache;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.internal.DefaultCacheKeysFactory;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.collection.CollectionPersister;
/**
 * JTA CollectionRegionAccessStrategy.
 *
 * @author Chris Dennis
 * @author Ludovic Orban
 * @author Alex Snaps
 */
public class TransactionalEhcacheRedisCollectionRegionAccessStrategy
        extends AbstractTransactionalEhcacheRedisAccessStrategy<EhcacheRedisCollectionRegion>
        implements CollectionRegionAccessStrategy {
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
    public TransactionalEhcacheRedisCollectionRegionAccessStrategy(EhcacheRedisCollectionRegion region, Ehcache ehcache,
            RedisCache redisCache, SessionFactoryOptions settings) {
        super(region, ehcache, redisCache, settings);
    }
    @Override
    public CollectionRegion getRegion() {
        return region();
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
    public Object generateCacheKey(Object id, CollectionPersister persister, SessionFactoryImplementor factory,
            String tenantIdentifier) {
        return DefaultCacheKeysFactory.staticCreateCollectionKey(id, persister, factory, tenantIdentifier);
    }
    @Override
    public Object getCacheKeyId(Object cacheKey) {
        return DefaultCacheKeysFactory.staticGetCollectionId(cacheKey);
    }
}
