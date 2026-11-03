/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.bytter.core.cache.hibernate5.internal.strategy;
import com.bytter.core.cache.hibernate5.internal.regions.EhcacheRedisCollectionRegion;
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
 * Ehcache specific read-only collection region access strategy
 *
 * @author Chris Dennis
 * @author Alex Snaps
 */
public class ReadOnlyEhcacheRedisCollectionRegionAccessStrategy extends
        AbstractEhcacheRedisAccessStrategy<EhcacheRedisCollectionRegion> implements CollectionRegionAccessStrategy {
    /**
     * Create a read-only access strategy accessing the given collection region.
     *
     * @param region
     *            The wrapped region
     * @param settings
     *            The Hibernate settings
     */
    public ReadOnlyEhcacheRedisCollectionRegionAccessStrategy(EhcacheRedisCollectionRegion region,
            SessionFactoryOptions settings) {
        super(region, settings);
    }
    @Override
    public CollectionRegion getRegion() {
        return region();
    }
    @Override
    public Object get(SessionImplementor session, Object key, long txTimestamp) throws CacheException {
        return region().get(key);
    }
    @Override
    public boolean putFromLoad(SessionImplementor session, Object key, Object value, long txTimestamp, Object version,
            boolean minimalPutOverride) throws CacheException {
        if (minimalPutOverride && region().contains(key)) {
            return false;
        } else {
            region().put(key, value);
            return true;
        }
    }
    @Override
    public SoftLock lockItem(SessionImplementor session, Object key, Object version)
            throws UnsupportedOperationException {
        return null;
    }
    /**
     * {@inheritDoc}
     * <p>
     * A no-op since this cache is read-only
     */
    @Override
    public void unlockItem(SessionImplementor session, Object key, SoftLock lock) throws CacheException {
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
