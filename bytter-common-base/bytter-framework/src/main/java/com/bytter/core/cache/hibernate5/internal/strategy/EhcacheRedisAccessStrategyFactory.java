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
 * Factory to create {@link org.hibernate.cache.spi.access.RegionAccessStrategy} instance
 *
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 */
public interface EhcacheRedisAccessStrategyFactory {
    /**
     * Create {@link EntityRegionAccessStrategy} for the input {@link EhcacheRedisEntityRegion} and {@link AccessType}
     *
     * @param entityRegion
     *            The entity region being wrapped
     * @param accessType
     *            The type of access to allow to the region
     *
     * @return the created {@link EntityRegionAccessStrategy}
     */
    public EntityRegionAccessStrategy createEntityRegionAccessStrategy(EhcacheRedisEntityRegion entityRegion,
            AccessType accessType);
    /**
     * Create {@link CollectionRegionAccessStrategy} for the input {@link EhcacheRedisCollectionRegion} and
     * {@link AccessType}
     *
     * @param collectionRegion
     *            The collection region being wrapped
     * @param accessType
     *            The type of access to allow to the region
     *
     * @return the created {@link CollectionRegionAccessStrategy}
     */
    public CollectionRegionAccessStrategy createCollectionRegionAccessStrategy(
            EhcacheRedisCollectionRegion collectionRegion, AccessType accessType);
    /**
     * Create {@link NaturalIdRegionAccessStrategy} for the input {@link EhcacheRedisNaturalIdRegion} and
     * {@link AccessType}
     *
     * @param naturalIdRegion
     *            The natural-id region being wrapped
     * @param accessType
     *            The type of access to allow to the region
     *
     * @return the created {@link NaturalIdRegionAccessStrategy}
     */
    public NaturalIdRegionAccessStrategy createNaturalIdRegionAccessStrategy(
            EhcacheRedisNaturalIdRegion naturalIdRegion, AccessType accessType);
}
