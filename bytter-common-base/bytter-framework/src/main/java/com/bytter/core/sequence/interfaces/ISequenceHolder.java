package com.bytter.core.sequence.interfaces;
import java.util.List;
/**
 * @author zhoul
 */
public interface ISequenceHolder {
    /**
     * 获取单个流水号
     *
     * @return 流水号
     */
    String get();
    /**
     * 获取多个流水号
     *
     * @param number      获取的数量
     * @param specialCode 特殊编码
     * @param lockTag     锁KEY
     * @return 流水号
     */
    List<String> get(Integer number, String specialCode, String lockTag);
    /**
     * 直接数据库获取多个流水号
     *
     * @param number      获取的数量
     * @param specialCode 特殊编码
     * @param lockTag     锁KEY
     * @return 流水号
     */
    List<String> getNoCache(Integer number, String specialCode, String lockTag);
    /**
     * 只单独锁流水
     *
     * @param specialCode 特殊编码
     * @param lockTag     锁KEY
     */
    void lockOnly(String specialCode, String lockTag);
    /**
     * 是否锁定
     *
     * @param specialCode 特殊编码
     * @param lockTag     锁标识
     * @return true锁定
     */
    boolean isLocked(String specialCode, String lockTag);
}
