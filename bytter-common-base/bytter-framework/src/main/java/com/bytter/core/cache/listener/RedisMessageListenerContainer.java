package com.bytter.core.cache.listener;
/**
 * Created by zhoul on 2017/12/22.
 */
public class RedisMessageListenerContainer
        extends org.springframework.data.redis.listener.RedisMessageListenerContainer {
    private boolean redisSwitch;
    @Override
    public void start() {
        if (redisSwitch) {
            super.start();
        }
    }
    @Override
    public void afterPropertiesSet() {
        if (redisSwitch) {
            super.afterPropertiesSet();
        }
    }
    public void setRedisSwitch(boolean redisSwitch) {
        this.redisSwitch = redisSwitch;
    }
}
