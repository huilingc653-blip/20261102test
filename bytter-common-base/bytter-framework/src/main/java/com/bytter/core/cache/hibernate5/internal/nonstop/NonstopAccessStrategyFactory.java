/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.bytter.core.cache.hibernate5.internal.nonstop;
import com.bytter.core.cache.hibernate5.internal.regions.EhcacheRedisCollectionRegion;
import com.bytter.core.cache.hibernate5.internal.regions.EhcacheRedisEntityRegion;
import com.bytter.core.cache.hibernate5.internal.regions.EhcacheRedisNaturalIdRegion;
import com.bytter.core.cache.hibernate5.internal.strategy.EhcacheRedisAccessStrategyFactory;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
/**
 * Implementation of {@link EhcacheRedisAccessStrategyFactory} that takes care of Nonstop cache exceptions using
 * {@link HibernateNonstopCacheExceptionHandler}
 *
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 */
public class NonstopAccessStrategyFactory implements EhcacheRedisAccessStrategyFactory {
    private final EhcacheRedisAccessStrategyFactory actualFactory;
    /**
     * Constructor accepting the actual factory
     *
     * @param actualFactory
     *            The wrapped RegionAccessStrategy factory
     */
    public NonstopAccessStrategyFactory(EhcacheRedisAccessStrategyFactory actualFactory) {
        this.actualFactory = actualFactory;
    }
    @Override
    public EntityRegionAccessStrategy createEntityRegionAccessStrategy(EhcacheRedisEntityRegion entityRegion,
            AccessType accessType) {
        return new NonstopAwareEntityRegionAccessStrategy(
                actualFactory.createEntityRegionAccessStrategy(entityRegion, accessType),
                HibernateNonstopCacheExceptionHandler.getInstance());
    }
    @Override
    public NaturalIdRegionAccessStrategy createNaturalIdRegionAccessStrategy(
            EhcacheRedisNaturalIdRegion naturalIdRegion, AccessType accessType) {
        return new NonstopAwareNaturalIdRegionAccessStrategy(
                actualFactory.createNaturalIdRegionAccessStrategy(naturalIdRegion, accessType),
                HibernateNonstopCacheExceptionHandler.getInstance());
    }
    @Override
    public CollectionRegionAccessStrategy createCollectionRegionAccessStrategy(
            EhcacheRedisCollectionRegion collectionRegion, AccessType accessType) {
        return new NonstopAwareCollectionRegionAccessStrategy(
                actualFactory.createCollectionRegionAccessStrategy(collectionRegion, accessType),
                HibernateNonstopCacheExceptionHandler.getInstance());
    }
}
