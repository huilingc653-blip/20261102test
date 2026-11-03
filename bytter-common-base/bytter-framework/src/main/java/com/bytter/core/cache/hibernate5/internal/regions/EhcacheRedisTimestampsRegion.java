package com.bytter.core.cache.hibernate5.internal.regions;
import com.bytter.core.cache.hibernate5.impl.RedisCache;
import com.bytter.core.cache.hibernate5.internal.strategy.EhcacheRedisAccessStrategyFactory;
import net.sf.ehcache.Ehcache;
import org.hibernate.cache.spi.TimestampsRegion;
import java.util.Properties;
/**
 * A timestamps region specific wrapper around an Ehcache instance.
 *
 * @author Chris Dennis
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 * @author zhoul
 */
public class EhcacheRedisTimestampsRegion extends EhcacheRedisGeneralDataRegion implements TimestampsRegion {
    /**
     * Constructs an EhcacheTimestampsRegion around the given underlying cache.
     *
     * @param accessStrategyFactory
     *            The factory for building needed CollectionRegionAccessStrategy instance
     * @param underlyingCache
     *            The ehcache cache instance
     * @param properties
     *            Any additional[ properties
     * @param redisCache
     *            redisCache
     */
    public EhcacheRedisTimestampsRegion(EhcacheRedisAccessStrategyFactory accessStrategyFactory,
            Ehcache underlyingCache, RedisCache redisCache, Properties properties) {
        super(accessStrategyFactory, underlyingCache, redisCache, properties);
    }
}
