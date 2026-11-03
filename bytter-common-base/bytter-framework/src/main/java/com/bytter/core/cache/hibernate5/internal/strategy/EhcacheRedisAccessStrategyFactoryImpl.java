/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.bytter.core.cache.hibernate5.internal.strategy;
import com.bytter.core.cache.hibernate5.internal.regions.EhcacheRedisCollectionRegion;
import com.bytter.core.cache.hibernate5.internal.regions.EhcacheRedisEntityRegion;
import com.bytter.core.cache.hibernate5.internal.regions.EhcacheRedisNaturalIdRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
/**
 * Class implementing {@link EhcacheRedisAccessStrategyFactory}
 *
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 */
public class EhcacheRedisAccessStrategyFactoryImpl implements EhcacheRedisAccessStrategyFactory {
    @Override
    public EntityRegionAccessStrategy createEntityRegionAccessStrategy(EhcacheRedisEntityRegion entityRegion,
            AccessType accessType) {
        switch (accessType) {
        case READ_ONLY:
            if (entityRegion.getCacheDataDescription().isMutable()) {
                // LOG.readOnlyCacheConfiguredForMutableEntity( entityRegion.getName() );
            }
            return new ReadOnlyEhcacheRedisEntityRegionAccessStrategy(entityRegion, entityRegion.getSettings());
        case READ_WRITE:
            return new ReadWriteEhcacheRedisEntityRegionAccessStrategy(entityRegion, entityRegion.getSettings());
        case NONSTRICT_READ_WRITE:
            return new NonStrictReadWriteEhcacheRedisEntityRegionAccessStrategy(entityRegion,
                    entityRegion.getSettings());
        case TRANSACTIONAL:
            return new TransactionalEhcacheRedisEntityRegionAccessStrategy(entityRegion, entityRegion.getEhcache(),
                    entityRegion.getRedisCache(), entityRegion.getSettings());
        default:
            throw new IllegalArgumentException("unrecognized access strategy type [" + accessType + "]");
        }
    }
    @Override
    public CollectionRegionAccessStrategy createCollectionRegionAccessStrategy(
            EhcacheRedisCollectionRegion collectionRegion, AccessType accessType) {
        switch (accessType) {
        case READ_ONLY:
            if (collectionRegion.getCacheDataDescription().isMutable()) {
                // LOG.readOnlyCacheConfiguredForMutableEntity( collectionRegion.getName() );
            }
            return new ReadOnlyEhcacheRedisCollectionRegionAccessStrategy(collectionRegion,
                    collectionRegion.getSettings());
        case READ_WRITE:
            return new ReadWriteEhcacheRedisCollectionRegionAccessStrategy(collectionRegion,
                    collectionRegion.getSettings());
        case NONSTRICT_READ_WRITE:
            return new NonStrictReadWriteEhcacheRedisCollectionRegionAccessStrategy(collectionRegion,
                    collectionRegion.getSettings());
        case TRANSACTIONAL:
            return new TransactionalEhcacheRedisCollectionRegionAccessStrategy(collectionRegion,
                    collectionRegion.getEhcache(), collectionRegion.getRedisCache(), collectionRegion.getSettings());
        default:
            throw new IllegalArgumentException("unrecognized access strategy type [" + accessType + "]");
        }
    }
    @Override
    public NaturalIdRegionAccessStrategy createNaturalIdRegionAccessStrategy(
            EhcacheRedisNaturalIdRegion naturalIdRegion, AccessType accessType) {
        switch (accessType) {
        case READ_ONLY:
            if (naturalIdRegion.getCacheDataDescription().isMutable()) {
                // LOG.readOnlyCacheConfiguredForMutableEntity( naturalIdRegion.getName() );
            }
            return new ReadOnlyEhcacheRedisNaturalIdRegionAccessStrategy(naturalIdRegion,
                    naturalIdRegion.getSettings());
        case READ_WRITE:
            return new ReadWriteEhcacheRedisNaturalIdRegionAccessStrategy(naturalIdRegion,
                    naturalIdRegion.getSettings());
        case NONSTRICT_READ_WRITE:
            return new NonStrictReadWriteEhcacheRedisNaturalIdRegionAccessStrategy(naturalIdRegion,
                    naturalIdRegion.getSettings());
        case TRANSACTIONAL:
            return new TransactionalEhcacheRedisNaturalIdRegionAccessStrategy(naturalIdRegion,
                    naturalIdRegion.getEhcache(), naturalIdRegion.getRedisCache(), naturalIdRegion.getSettings());
        default:
            throw new IllegalArgumentException("unrecognized access strategy type [" + accessType + "]");
        }
    }
}
