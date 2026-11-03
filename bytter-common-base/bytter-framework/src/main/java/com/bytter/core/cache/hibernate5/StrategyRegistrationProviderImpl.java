/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.bytter.core.cache.hibernate5;
import org.hibernate.boot.registry.selector.SimpleStrategyRegistrationImpl;
import org.hibernate.boot.registry.selector.StrategyRegistration;
import org.hibernate.boot.registry.selector.StrategyRegistrationProvider;
import org.hibernate.cache.spi.RegionFactory;
import java.util.ArrayList;
import java.util.List;
/**
 * Makes the 2 contained region factory implementations available to the Hibernate
 * {@link org.hibernate.boot.registry.selector.spi.StrategySelector} service.
 *
 * @author Steve Ebersole
 */
public class StrategyRegistrationProviderImpl implements StrategyRegistrationProvider {
    @Override
    @SuppressWarnings("unchecked")
    public Iterable<StrategyRegistration> getStrategyRegistrations() {
        final List<StrategyRegistration> strategyRegistrations = new ArrayList<StrategyRegistration>();
        strategyRegistrations
                .add(new SimpleStrategyRegistrationImpl(RegionFactory.class, EhCacheRedisRegionFactory.class, "ehcache",
                        EhCacheRedisRegionFactory.class.getName(), EhCacheRedisRegionFactory.class.getSimpleName(),
                        // legacy impl class name
                        "org.hibernate.cache.EhCacheRegionFactory"));
        strategyRegistrations
                .add(new SimpleStrategyRegistrationImpl(RegionFactory.class, SingletonEhCacheRedisRegionFactory.class,
                        "ehcache-singleton", SingletonEhCacheRedisRegionFactory.class.getName(),
                        SingletonEhCacheRedisRegionFactory.class.getSimpleName(),
                        // legacy impl class name
                        "org.hibernate.cache.SingletonEhCacheRegionFactory"));
        return strategyRegistrations;
    }
}
