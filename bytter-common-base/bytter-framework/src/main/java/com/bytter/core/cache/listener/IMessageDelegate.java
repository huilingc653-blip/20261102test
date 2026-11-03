package com.bytter.core.cache.listener;
import java.io.Serializable;
public interface IMessageDelegate {
    void handleMessage(Serializable message, String channel);
}
