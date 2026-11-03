package com.bytter.core.cache.hibernate5;
/**
 * Created by zhoul on 2017/12/27.
 */
public class IEhcacheRedisSettings {
    public static String HIBERNATE_FULL_KEY;
    public static final String SWITCH = "redis.switch";
    public static final String HOSTNAME = "redis.hostname";
    public static final String PASSWORD = "redis.password";
    public static final String PORT = "redis.port";
    public static final String DATABASE = "redis.database";
    public static final String USE_PREFIX = "redis.usePrefix";
    public static final String CACHE_PREFIX = "redis.cachePrefix";
    public static final String CHANNEL_SYSTEM = "redis.channel.system";
    public static final String CHANNEL_HIBERNATE = "redis.channel.hibernate";
    public static final String POOL_MAX_TOTAL = "redis.pool.maxTotal";
    public static final String POOL_MAX_WAIT_MILLIS = "redis.pool.maxWaitMillis";
    public static final String POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS = "redis.pool.minEvictableIdleTimeMillis";
    public static final String POOL_MIN_IDLE = "redis.pool.minIdle";
    public static final String POOL_MAX_IDLE = "redis.pool.maxIdle";
    public static final String POOL_NUM_TESTS_PER_EVICTION_RUN = "redis.pool.numTestsPerEvictionRun";
    public static final String POOL_LIFO = "redis.pool.lifo";
    public static final String POOL_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS = "redis.pool.softMinEvictableIdleTimeMillis";
    public static final String POOL_TEST_ON_BORROW = "redis.pool.testOnBorrow";
    public static final String POOL_TEST_ON_RETURN = "redis.pool.testOnReturn";
    public static final String POOL_TEST_WHILE_IDLE = "redis.pool.testWhileIdle";
    public static final String POOL_TIME_BETWEEN_EVICTION_RUNS_MILLS = "redis.pool.timeBetweenEvictionRunsMillis";
    public static final String POOL_BLOCK_WHEN_EXHAUSTED = "redis.pool.blockWhenExhausted";
}
