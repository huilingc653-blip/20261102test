package com.bytter.core.sequence.interfaces;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * 流水号生成接口
 *
 * @author zhoul
 * @date 2019/01/02
 */
public interface ISequenceLock {
    /**
     * 获取一个流水号
     *
     * @param key 流水号KEY
     * @return 流水号
     */
    AtomicInteger get(String key);
    /**
     * 初始化流水号
     *
     * @param key           流水号KEY
     * @param atomicInteger 初始化的流水
     */
    void init(String key, AtomicInteger atomicInteger);
    /**
     * 将流水塞入到缓存中
     *
     * @param key           流水主键
     * @param atomicInteger 流水值
     * @param lockKey       锁标识
     */
    void put(String key, AtomicInteger atomicInteger, String lockKey);
    /**
     * 对流水进行加锁
     *
     * @param lockKey 锁标识
     */
    void lock(String lockKey);
    /**
     * 对流水进行解锁
     *
     * @param lockKey 锁标识
     */
    void unlock(String lockKey);
    /**
     * 判断流水是否被锁
     *
     * @param lockKey 锁标识
     * @return true锁定
     */
    boolean isLocked(String lockKey);
    /**
     * 设置日期格式
     *
     * @param dateFormat 日期格式
     */
    void setDateFormat(SimpleDateFormat dateFormat);
    /**
     * 设置是否为连续流水号
     *
     * @param continueSequence 是否为连续流水号
     * @since 2019-12-25 by:zhoul
     */
    void setContinueSequence(boolean continueSequence);
}
