package com.bytter.core.cache.hibernate5.internal.regions;
import com.bytter.core.cache.hibernate5.impl.RedisCache;
import com.bytter.core.cache.hibernate5.internal.strategy.EhcacheRedisAccessStrategyFactory;
import net.sf.ehcache.Ehcache;
import org.hibernate.cache.spi.QueryResultsRegion;
import java.util.Properties;
/**
 * A query results region specific wrapper around an Ehcache instance.
 *
 * @author Chris Dennis
 * @author Alex Snaps
 */
public class EhcacheRedisQueryResultsRegion extends EhcacheRedisGeneralDataRegion implements QueryResultsRegion {
    /**
     * Constructs an EhcacheQueryResultsRegion around the given underlying cache.
     *
     * @param accessStrategyFactory
     *            The factory for building needed CollectionRegionAccessStrategy instance
     * @param underlyingCache
     *            The ehcache cache instance
     * @param redisCache
     *            redisCache
     * @param properties
     *            Any additional[ properties
     */
    public EhcacheRedisQueryResultsRegion(EhcacheRedisAccessStrategyFactory accessStrategyFactory,
            Ehcache underlyingCache, RedisCache redisCache, Properties properties) {
        super(accessStrategyFactory, underlyingCache, redisCache, properties);
    }
}
