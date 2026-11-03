package com.bytter.core.cache.redis.core;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jsonRedisSerializer.setObjectMapper(mapper);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
        LettucePoolingClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                .clientName("bt-redis").build();
        // 单机部署
        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration();
        standaloneConfiguration.setHostName(redisProperties.getHost());
        standaloneConfiguration.setPassword(redisProperties.getPassword());
        standaloneConfiguration.setPort(redisProperties.getPort());
        standaloneConfiguration.setDatabase(redisProperties.getDatabase());
        // Cluster部署
        // RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        // redisClusterConfiguration.setMaxRedirects(redisProperties.getCluster().getMaxRedirects());
        // redisClusterConfiguration.setPassword(redisProperties.getPassword());
        // 哨兵部署
        // RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
        return new LettuceConnectionFactory(standaloneConfiguration, clientConfiguration);
    }
    /*
     * @Bean public CacheManager cacheManager(RedisConnectionFactory factory) { RedisSerializer<String> redisSerializer
     * = new StringRedisSerializer(); Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new
     * Jackson2JsonRedisSerializer(Object.class); //解决查询缓存转换异常的问题 ObjectMapper om = new ObjectMapper();
     * om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
     * om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL); jackson2JsonRedisSerializer.setObjectMapper(om);
     * 
     * // 配置序列化（解决乱码的问题） RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
     * .entryTtl(Duration.ofDays(1))
     * .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
     * .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
     * .disableCachingNullValues();
     * 
     * RedisCacheManager cacheManager = RedisCacheManager.builder(factory) .cacheDefaults(config) .build(); return
     * cacheManager; }
     */
}
