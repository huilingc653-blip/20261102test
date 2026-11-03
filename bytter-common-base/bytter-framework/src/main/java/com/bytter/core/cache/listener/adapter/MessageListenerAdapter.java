package com.bytter.core.cache.listener.adapter;
/**
 * Created by zhoul on 2017/12/22.
 */
public class MessageListenerAdapter extends org.springframework.data.redis.listener.adapter.MessageListenerAdapter {
    private boolean redisSwitch;
    public MessageListenerAdapter(Object delegate) {
        super(delegate);
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
