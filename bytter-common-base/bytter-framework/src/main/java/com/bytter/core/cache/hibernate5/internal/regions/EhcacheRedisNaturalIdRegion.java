package com.bytter.core.cache.hibernate5.internal.regions;
import com.bytter.core.cache.hibernate5.impl.RedisCache;
import com.bytter.core.cache.hibernate5.internal.strategy.EhcacheRedisAccessStrategyFactory;
import net.sf.ehcache.Ehcache;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import java.util.Properties;
/**
 * A collection region specific wrapper around an Ehcache instance.
 * <p>
 * This implementation returns Ehcache specific access strategy instances for all the non-transactional access types.
 * Transactional access is not supported.
 *
 * @author Chris Dennis
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 */
public class EhcacheRedisNaturalIdRegion extends EhcacheRedisTransactionalDataRegion implements NaturalIdRegion {
    /**
     * Constructs an EhcacheNaturalIdRegion around the given underlying cache.
     *
     * @param accessStrategyFactory
     *            The factory for building needed NaturalIdRegionAccessStrategy instance
     * @param underlyingCache
     *            The ehcache cache instance
     * @param redisCache
     *            redisCache
     * @param settings
     *            The Hibernate settings
     * @param metadata
     *            Metadata about the data to be cached in this region
     * @param properties
     *            Any additional[ properties
     */
    public EhcacheRedisNaturalIdRegion(EhcacheRedisAccessStrategyFactory accessStrategyFactory, Ehcache underlyingCache,
            RedisCache redisCache, SessionFactoryOptions settings, CacheDataDescription metadata,
            Properties properties) {
        super(accessStrategyFactory, underlyingCache, redisCache, settings, metadata, properties);
    }
    @Override
    public NaturalIdRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
        return getAccessStrategyFactory().createNaturalIdRegionAccessStrategy(this, accessType);
    }
}
