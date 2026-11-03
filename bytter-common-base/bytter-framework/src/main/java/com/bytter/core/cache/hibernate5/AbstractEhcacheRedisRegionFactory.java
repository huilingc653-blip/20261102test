package com.bytter.core.cache.hibernate5;
import com.bytter.core.cache.hibernate5.impl.ProviderMBeanRegistrationHelper;
import com.bytter.core.cache.hibernate5.impl.RedisCache;
import com.bytter.core.cache.hibernate5.internal.nonstop.NonstopAccessStrategyFactory;
import com.bytter.core.cache.hibernate5.internal.regions.*;
import com.bytter.core.cache.hibernate5.internal.strategy.EhcacheRedisAccessStrategyFactory;
import com.bytter.core.cache.hibernate5.internal.strategy.EhcacheRedisAccessStrategyFactoryImpl;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.config.CacheConfiguration;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.*;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.service.spi.InjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisOperations;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Properties;
/**
 * Abstract implementation of an Ehcache specific RegionFactory.
 *
 * @author Chris Dennis
 * @author Greg Luck
 * @author Emmanuel Bernard
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 */
abstract class AbstractEhcacheRedisRegionFactory implements RegionFactory {
    /**
     * The Hibernate system property specifying the location of the ehcache configuration file name.
     * <p>
     * If not set, ehcache.xml will be looked for in the root of the classpath.
     * <p>
     * If set to say ehcache-1.xml, ehcache-1.xml will be looked for in the root of the classpath.
     */
    public static final String NET_SF_EHCACHE_CONFIGURATION_RESOURCE_NAME = "net.sf.ehcache.configurationResourceName";
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * MBean registration helper class instance for Ehcache Hibernate MBeans.
     */
    protected final ProviderMBeanRegistrationHelper mbeanRegistrationHelper = new ProviderMBeanRegistrationHelper();
    /**
     * Ehcache CacheManager that supplied Ehcache instances for this Hibernate RegionFactory.
     */
    protected volatile CacheManager manager;
    protected volatile RedisOperations redisOperations;
    protected String redisChannel;
    /**
     * Settings object for the Hibernate persistence unit.
     */
    protected SessionFactoryOptions settings;
    /**
     * {@link EhcacheRedisAccessStrategyFactory} for creating various access strategies
     */
    protected final EhcacheRedisAccessStrategyFactory accessStrategyFactory = new NonstopAccessStrategyFactory(
            new EhcacheRedisAccessStrategyFactoryImpl());
    /**
     * {@inheritDoc}
     * <p>
     * In Ehcache we default to minimal puts since this should have minimal to no affect on unclustered users, and has
     * great benefit for clustered users.
     *
     * @return true, optimize for minimal puts
     */
    @Override
    public boolean isMinimalPutsEnabledByDefault() {
        return true;
    }
    @Override
    public long nextTimestamp() {
        return net.sf.ehcache.util.Timestamper.next();
    }
    @Override
    public EntityRegion buildEntityRegion(String regionName, Properties properties, CacheDataDescription metadata)
            throws CacheException {
        return new EhcacheRedisEntityRegion(accessStrategyFactory, getCache(regionName), getRedisCache(regionName),
                settings, metadata, properties);
    }
    @Override
    public NaturalIdRegion buildNaturalIdRegion(String regionName, Properties properties, CacheDataDescription metadata)
            throws CacheException {
        return new EhcacheRedisNaturalIdRegion(accessStrategyFactory, getCache(regionName), getRedisCache(regionName),
                settings, metadata, properties);
    }
    @Override
    public CollectionRegion buildCollectionRegion(String regionName, Properties properties,
            CacheDataDescription metadata) throws CacheException {
        return new EhcacheRedisCollectionRegion(accessStrategyFactory, getCache(regionName), getRedisCache(regionName),
                settings, metadata, properties);
    }
    @Override
    public QueryResultsRegion buildQueryResultsRegion(String regionName, Properties properties) throws CacheException {
        return new EhcacheRedisQueryResultsRegion(accessStrategyFactory, getCache(regionName),
                getRedisCache(regionName), properties);
    }
    @InjectService
    @SuppressWarnings("UnusedDeclaration")
    public void setClassLoaderService(ClassLoaderService classLoaderService) {
        this.classLoaderService = classLoaderService;
    }
    private ClassLoaderService classLoaderService;
    @Override
    public TimestampsRegion buildTimestampsRegion(String regionName, Properties properties) throws CacheException {
        return new EhcacheRedisTimestampsRegion(accessStrategyFactory, getCache(regionName), getRedisCache(regionName),
                properties);
    }
    private Ehcache getCache(String name) throws CacheException {
        try {
            Ehcache cache = manager.getEhcache(name);
            if (cache == null) {
                // LOG.unableToFindEhCacheConfiguration(name);
                manager.addCache(name);
                cache = manager.getEhcache(name);
                logger.debug("started EHCache region: " + name);
            }
            return cache;
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException(e);
        }
    }
    private RedisCache getRedisCache(String name) {
        if (redisOperations == null) {
            return null;
        }
        Cache cache = manager.getCache(name);
        CacheConfiguration cacheConfiguration = cache.getCacheConfiguration();
        RedisCache redisCache = new RedisCache(name,
                MessageFormat.format(IEhcacheRedisSettings.HIBERNATE_FULL_KEY, name).getBytes(), redisOperations,
                cacheConfiguration.getTimeToLiveSeconds());
        redisCache.setChannel(redisChannel);
        return redisCache;
    }
    /**
     * Load a resource from the classpath.
     */
    protected URL loadResource(String configurationResourceName) {
        URL url = null;
        if (classLoaderService != null) {
            url = classLoaderService.locateResource(configurationResourceName);
        }
        if (url == null) {
            final ClassLoader standardClassloader = Thread.currentThread().getContextClassLoader();
            if (standardClassloader != null) {
                url = standardClassloader.getResource(configurationResourceName);
            }
            if (url == null) {
                url = AbstractEhcacheRedisRegionFactory.class.getResource(configurationResourceName);
            }
            if (url == null) {
                try {
                    url = new URL(configurationResourceName);
                } catch (MalformedURLException e) {
                    // ignore
                }
            }
        }
        if (logger.isDebugEnabled()) {
            // LOG.debugf(
            // "Creating EhCacheRegionFactory from a specified resource: %s. Resolved to URL: %s",
            // configurationResourceName,
            // url
            // );
        }
        if (url == null) {
            // LOG.unableToLoadConfiguration(configurationResourceName);
        }
        return url;
    }
    /**
     * Default access-type used when the configured using JPA 2.0 config. JPA 2.0 allows <code>@Cacheable(true)</code>
     * to be attached to an entity without any access type or usage qualification.
     * <p>
     * We are conservative here in specifying {@link AccessType#READ_WRITE} so as to follow the mantra of "do no harm".
     * <p>
     * This is a Hibernate 3.5 method.
     */
    public AccessType getDefaultAccessType() {
        return AccessType.READ_WRITE;
    }
}
