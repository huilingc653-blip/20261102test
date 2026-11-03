package com.bytter.core.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 缓存注解配置。
 *
 * @author bytter
 * @date 2018/11/27
 */
@EnableCaching
@Configuration
public class CacheConfig {
    @Value("${redis.switch}")
    private boolean redisSwitch;
    @Value("${redis.usePrefix}")
    private boolean redisUsePrefix;
    @Value("${redis.cachePrefix}")
    private String redisCachePrefix;
    @Value("${redis.channel.system}")
    private String redisChannelSystem;

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setConfigLocation(
                new PathMatchingResourcePatternResolver().getResource("classpath:ehcache/ehcache-system.xml"));
        factoryBean.setShared(true);
        factoryBean.setCacheManagerName("springEhcacheManager");
        return factoryBean;
    }

    /**
     * todo 初始化与原项目不一致
     *
     * @return
     */
    @Bean("cacheManager")
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        EhcacheRedisCacheManager cacheManager = new EhcacheRedisCacheManager(ehCacheManagerFactoryBean().getObject(),
                redisTemplate);
        cacheManager.setTransactionAware(true);
        cacheManager.setUseRedis(redisSwitch);
        cacheManager.setRedisUsePrefix(redisUsePrefix);
        cacheManager.setRedisCachePrefix(redisCachePrefix);
        cacheManager.setRedisChannel(redisChannelSystem);
        return cacheManager;
    }
}
