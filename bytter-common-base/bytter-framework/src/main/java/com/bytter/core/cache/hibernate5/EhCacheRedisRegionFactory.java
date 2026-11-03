/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.bytter.core.cache.hibernate5;
import com.bytter.core.cache.hibernate5.internal.util.HibernateEhcacheUtils;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
/**
 * A non-singleton EhCacheRegionFactory implementation. 根据开关选取本地还是redis缓存
 * 
 * @author Chris Dennis
 * @author Greg Luck
 * @author Emmanuel Bernard
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 */
public class EhCacheRedisRegionFactory extends AbstractEhcacheRedisRegionFactory {
    /**
     * Creates a non-singleton EhCacheRegionFactory
     */
    @SuppressWarnings("UnusedDeclaration")
    public EhCacheRedisRegionFactory() {
    }
    private HibernateEhcacheRedisCacheFactory ehcacheRedisCacheFactory;
    /**
     * Creates a non-singleton EhCacheRegionFactory
     *
     * @param prop
     *            Not used
     */
    @SuppressWarnings("UnusedDeclaration")
    public EhCacheRedisRegionFactory(Properties prop) {
        super();
    }
    @Override
    public void start(SessionFactoryOptions settings, Properties properties) throws CacheException {
        this.settings = settings;
        if (manager != null) {
            return;
        }
        try {
            String configurationResourceName = null;
            if (properties != null) {
                configurationResourceName = (String) properties.get(NET_SF_EHCACHE_CONFIGURATION_RESOURCE_NAME);
            }
            if (configurationResourceName == null || configurationResourceName.length() == 0) {
                final Configuration configuration = ConfigurationFactory.parseConfiguration();
                manager = new CacheManager(configuration);
            } else {
                final URL url = loadResource(configurationResourceName);
                final Configuration configuration = HibernateEhcacheUtils.loadAndCorrectConfiguration(url);
                manager = new CacheManager(configuration);
            }
            mbeanRegistrationHelper.registerMBean(manager, properties);
            initRedis(properties);
        } catch (net.sf.ehcache.CacheException e) {
            if (e.getMessage().startsWith("Cannot parseConfiguration CacheManager. Attempt to create a new instance of "
                    + "CacheManager using the diskStorePath")) {
                throw new CacheException("Attempt to restart an already started EhCacheRegionFactory. "
                        + "Use sessionFactory.close() between repeated calls to buildSessionFactory. "
                        + "Consider using SingletonEhCacheRegionFactory. Error from ehcache was: " + e.getMessage());
            } else {
                throw new CacheException(e);
            }
        }
    }
    private void initRedis(Properties properties) {
        if ("true".equalsIgnoreCase(properties.getProperty("bytter.redis.switch", "true"))) {
            URL redisPropertiesURL = loadResource(properties.getProperty("bytter.redis.configurationResourceName"));
            Properties redisProperties = new Properties();
            try {
                redisProperties.load(redisPropertiesURL.openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            boolean usePrefix = "true"
                    .equalsIgnoreCase(redisProperties.getProperty(IEhcacheRedisSettings.USE_PREFIX, "true"));
            String prefix = redisProperties.getProperty(IEhcacheRedisSettings.CACHE_PREFIX);
            if (usePrefix) {
                IEhcacheRedisSettings.HIBERNATE_FULL_KEY = prefix + ":Hibernate:{0}:";
            } else {
                IEhcacheRedisSettings.HIBERNATE_FULL_KEY = "Hibernate:{0}:";
            }
            ehcacheRedisCacheFactory = new HibernateEhcacheRedisCacheFactory(manager, redisProperties);
            redisOperations = ehcacheRedisCacheFactory.getRedisOperations();
            redisChannel = redisProperties.getProperty(IEhcacheRedisSettings.CHANNEL_HIBERNATE);
        }
    }
    @Override
    public void stop() {
        try {
            if (manager != null) {
                mbeanRegistrationHelper.unregisterMBean();
                manager.shutdown();
                manager = null;
            }
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException(e);
        }
        if (ehcacheRedisCacheFactory != null) {
            ehcacheRedisCacheFactory.destroy();
        }
    }
}
