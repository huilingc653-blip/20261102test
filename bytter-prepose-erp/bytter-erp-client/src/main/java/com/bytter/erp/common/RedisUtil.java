package com.bytter.erp.common;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Description: Redis工具类 (基于Redisson实现)
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2026
 *
 * @author bytter
 * @date 2026/1/20
 */
@Getter
@Slf4j
@Component
@ConditionalOnProperty(name = "config.redis.enable", havingValue = "true", matchIfMissing = true)
public class RedisUtil {

    /**
     * -- GETTER --
     *  获取RedissonClient实例
     *
     * @return RedissonClient
     */
    @Autowired(required = false)
    private RedissonClient redissonClient;

    // =============================common============================

    /**
     * 指定缓存失效时间
     * @param key  键
     * @param time 时间(秒)
     * @return true成功 false 失败
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                RKeys keys = redissonClient.getKeys();
                keys.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.warn("RedisUtil.expire() error: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        try {
            RKeys keys = redissonClient.getKeys();
            long ttl = keys.remainTimeToLive(key);
            // 将毫秒转换为秒，向上取整
            return ttl > 0 ? (ttl + 999) / 1000 : ttl;
        } catch (Exception e) {
            log.warn("RedisUtil.getExpire() error: {}", e.getMessage(), e);
            return -1;
        }
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            RKeys keys = redissonClient.getKeys();
            return keys.countExists(key) > 0;
        } catch (Exception e) {
            log.warn("RedisUtil.hasKey() error: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        try {
            if (key != null && key.length > 0) {
                if (key.length == 1) {
                    redissonClient.getBucket(key[0]).delete();
                } else {
                    redissonClient.getKeys().delete(key);
                }
            }
        } catch (Exception e) {
            log.warn("RedisUtil.del() error: {}", e.getMessage(), e);
        }
    }

    // ============================String=============================

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        try {
            RBucket<Object> bucket = redissonClient.getBucket(key);
            return bucket.get();
        } catch (Exception e) {
            log.warn("RedisUtil.get() error: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 普通缓存放入
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            RBucket<Object> bucket = redissonClient.getBucket(key);
            bucket.set(value);
            return true;
        } catch (Exception e) {
            log.warn("RedisUtil.set() error: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            RBucket<Object> bucket = redissonClient.getBucket(key);
            if (time > 0) {
                bucket.set(value, time, TimeUnit.SECONDS);
            } else {
                bucket.set(value);
            }
            return true;
        } catch (Exception e) {
            log.warn("RedisUtil.set() error: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 递增
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        try {
            if (delta < 0) {
                throw new RuntimeException("递增因子必须大于0");
            }
            RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
            return atomicLong.addAndGet(delta);
        } catch (Exception e) {
            log.warn("RedisUtil.incr() error: {}", e.getMessage(), e);
            throw new RuntimeException("递增操作失败", e);
        }
    }

    /**
     * 递减
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        try {
            if (delta < 0) {
                throw new RuntimeException("递减因子必须大于0");
            }
            RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
            return atomicLong.addAndGet(-delta);
        } catch (Exception e) {
            log.warn("RedisUtil.decr() error: {}", e.getMessage(), e);
            throw new RuntimeException("递减操作失败", e);
        }
    }

    // ================================Map=================================

    /**
     * HashGet
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        try {
            RMap<Object, Object> map = redissonClient.getMap(key);
            return map.get(item);
        } catch (Exception e) {
            log.warn("RedisUtil.hget() error: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        try {
            RMap<Object, Object> map = redissonClient.getMap(key);
            return map.getAll(map.readAllKeySet());
        } catch (Exception e) {
            log.warn("RedisUtil.hmget() error: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            RMap<Object, Object> rMap = redissonClient.getMap(key);
            rMap.putAll(map);
            return true;
        } catch (Exception e) {
            log.warn("RedisUtil.hmset() error: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            RMap<Object, Object> rMap = redissonClient.getMap(key);
            rMap.putAll(map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.warn("RedisUtil.hmset() error: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            RMap<Object, Object> map = redissonClient.getMap(key);
            map.put(item, value);
            return true;
        } catch (Exception e) {
            log.warn("RedisUtil.hset() error: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            RMap<Object, Object> map = redissonClient.getMap(key);
            map.put(item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.warn("RedisUtil.hset() error: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        try {
            RMap<Object, Object> map = redissonClient.getMap(key);
            map.fastRemove(item);
        } catch (Exception e) {
            log.warn("RedisUtil.hdel() error: {}", e.getMessage(), e);
        }
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        try {
            RMap<Object, Object> map = redissonClient.getMap(key);
            return map.containsKey(item);
        } catch (Exception e) {
            log.warn("RedisUtil.hHasKey() error: {}", e.getMessage(), e);
            return false;
        }
    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            RSet<Object> set = redissonClient.getSet(key);
            return set.readAll();
        } catch (Exception e) {
            log.warn("RedisUtil.sGet() error: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            RSet<Object> set = redissonClient.getSet(key);
            return set.contains(value);
        } catch (Exception e) {
            log.warn("RedisUtil.sHasKey() error: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            RSet<Object> set = redissonClient.getSet(key);
            for (Object value : values) {
                set.add(value);
            }
            return values.length;
        } catch (Exception e) {
            log.warn("RedisUtil.sSet() error: {}", e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            RSet<Object> set = redissonClient.getSet(key);
            for (Object value : values) {
                set.add(value);
            }
            if (time > 0) {
                expire(key, time);
            }
            return values.length;
        } catch (Exception e) {
            log.warn("RedisUtil.sSetAndTime() error: {}", e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            RSet<Object> set = redissonClient.getSet(key);
            return set.size();
        } catch (Exception e) {
            log.warn("RedisUtil.sGetSetSize() error: {}", e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            RSet<Object> set = redissonClient.getSet(key);
            long removedCount = 0;
            for (Object value : values) {
                if (set.remove(value)) {
                    removedCount++;
                }
            }
            return removedCount;
        } catch (Exception e) {
            log.warn("RedisUtil.setRemove() error: {}", e.getMessage(), e);
            return 0;
        }
    }

    // ===============================list=================================

    /**
     * 获取list缓存的内容
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            RList<Object> list = redissonClient.getList(key);
            if (start < 0 || start >= list.size()) {
                return null;
            }
            if (end >= list.size()) {
                end = list.size() - 1;
            }
            if (end == -1) {
                return list.subList((int) start, list.size());
            } else {
                return list.subList((int) start, (int) end + 1);
            }
        } catch (Exception e) {
            log.warn("RedisUtil.lGet() error: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            RList<Object> list = redissonClient.getList(key);
            return list.size();
        } catch (Exception e) {
            log.warn("RedisUtil.lGetListSize() error: {}", e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            RList<Object> list = redissonClient.getList(key);
            if (index < 0) {
                index = list.size() + index;  // 转换负索引为正索引
            }
            if (index >= 0 && index < list.size()) {
                return list.get((int) index);
            }
            return null;
        } catch (Exception e) {
            log.warn("RedisUtil.lGetIndex() error: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            RList<Object> list = redissonClient.getList(key);
            list.add(value);
            return true;
        } catch (Exception e) {
            log.warn("RedisUtil.lSet() error: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            RList<Object> list = redissonClient.getList(key);
            list.add(value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.warn("RedisUtil.lSet() error: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            RList<Object> list = redissonClient.getList(key);
            list.addAll(value);
            return true;
        } catch (Exception e) {
            log.warn("RedisUtil.lSet() error: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            RList<Object> list = redissonClient.getList(key);
            list.addAll(value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.warn("RedisUtil.lSet() error: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            RList<Object> list = redissonClient.getList(key);
            if (index < 0) {
                index = list.size() + index;  // 转换负索引为正索引
            }
            if (index >= 0 && index < list.size()) {
                list.set((int) index, value);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.warn("RedisUtil.lUpdateIndex() error: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            RList<Object> list = redissonClient.getList(key);
            long removeCount = 0;
            for (int i = 0; i < Math.min(count, list.size()); i++) {
                if (list.remove(value)) {
                    removeCount++;
                }
            }
            return removeCount;
        } catch (Exception e) {
            log.warn("RedisUtil.lRemove() error: {}", e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 尝试获取分布式锁
     * @param key 锁的键
     * @param leaseTime 锁的租约时间（毫秒）
     * @return true: 获取成功, false: 获取失败
     */
    public boolean tryLock(String key, long leaseTime) {
        try {
            RLock lock = redissonClient.getLock(key);
            return lock.tryLock(0, leaseTime, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.warn("RedisUtil.tryLock() error: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 释放分布式锁
     * @param key 锁的键
     * @return true: 释放成功, false: 释放失败
     */
    public boolean unlock(String key) {
        try {
            RLock lock = redissonClient.getLock(key);
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                return true;
            }
            return false;
        } catch (Exception e) {
            log.warn("RedisUtil.unlock() error: {}", e.getMessage(), e);
            return false;
        }
    }
}
