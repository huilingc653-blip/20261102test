package com.bytter.core.sequence.lock;
import java.util.HashMap;
import java.util.Map;
/**
 * @author zhoul
 */
public abstract class AbstractTransactionThreadLock {
    private static final ThreadLocal<Map<Object, Object>> RESOURCE = new ThreadLocal<>();
    protected Object getResource(Object key) {
        Map<Object, Object> map = RESOURCE.get();
        if (map == null) {
            return null;
        }
        return map.get(key);
    }
    protected void bindResource(Object key, Object value) {
        Map<Object, Object> map = RESOURCE.get();
        if (map == null) {
            map = new HashMap<>(16);
            RESOURCE.set(map);
        }
        map.put(key, value);
    }
    protected void unbindResource(Object key) {
        Map<Object, Object> map = RESOURCE.get();
        if (map == null) {
            return;
        }
        map.remove(key);
        if (map.isEmpty()) {
            RESOURCE.remove();
        }
    }
}
