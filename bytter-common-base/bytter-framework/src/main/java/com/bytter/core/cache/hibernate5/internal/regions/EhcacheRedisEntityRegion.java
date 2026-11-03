package com.bytter.core.cache.hibernate5.internal.regions;
import com.bytter.core.cache.hibernate5.impl.RedisCache;
import com.bytter.core.cache.hibernate5.internal.strategy.EhcacheRedisAccessStrategyFactory;
import net.sf.ehcache.Ehcache;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import java.util.Properties;
/**
 * An entity region specific wrapper around an Ehcache instance.
 * <p>
 * This implementation returns Ehcache specific access strategy instances for all the non-transactional access types.
 * Transactional access is not supported.
 *
 * @author Chris Dennis
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 * @author zhoul
 */
public class EhcacheRedisEntityRegion extends EhcacheRedisTransactionalDataRegion implements EntityRegion {
    /**
     * Constructs an EhcacheCollectionRegion around the given underlying cache.
     *
     * @param accessStrategyFactory
     *            The factory for building needed EntityRegionAccessStrategy instance
     * @param underlyingCache
     *            The ehcache cache instance
     * @param redisCache
     *            {@link RedisCache}
     * @param settings
     *            The Hibernate settings
     * @param metadata
     *            Metadata about the data to be cached in this region
     * @param properties
     *            Any additional[ properties
     */
    public EhcacheRedisEntityRegion(EhcacheRedisAccessStrategyFactory accessStrategyFactory, Ehcache underlyingCache,
            RedisCache redisCache, SessionFactoryOptions settings, CacheDataDescription metadata,
            Properties properties) {
        super(accessStrategyFactory, underlyingCache, redisCache, settings, metadata, properties);
    }
    @Override
    public EntityRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
        return getAccessStrategyFactory().createEntityRegionAccessStrategy(this, accessType);
    }
}
