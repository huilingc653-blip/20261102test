package com.bytter.core.sequence.interfaces.impl;

import cn.hutool.crypto.digest.MD5;
import cn.hutool.system.HostInfo;
import cn.hutool.system.SystemUtil;
import com.bytter.core.sequence.enums.SequenceDateEnum;
import com.bytter.core.sequence.interfaces.ISequenceEnum;
import com.bytter.core.sequence.interfaces.ISequenceHolder;
import com.bytter.core.sequence.interfaces.ISequenceLock;
import com.bytter.core.sequence.interfaces.ISequenceService;
import com.bytter.core.sequence.lock.MemoryLock;
import com.bytter.core.sequence.lock.RedisLock;
import com.bytter.core.sequence.singlefactory.SequenceStrategyFactory;
import com.bytter.core.sequence.strategy.BaseSequenceStrategy;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认流水号生成持久类
 *
 * @author zhoul
 * @date 2019/01/04
 */
public class DefaultSequenceHolder implements ISequenceHolder {
    private static final Logger logger = LoggerFactory.getLogger(DefaultSequenceHolder.class);
    private static final String EMPTY_STRING = "";
    private ISequenceLock lock;
    private ISequenceEnum sequenceEnum;
    private ISequenceService sequenceService;

    public DefaultSequenceHolder(ISequenceEnum sequenceEnum, ISequenceService sequenceService, RedissonClient redissonClient) {
        this.sequenceEnum = sequenceEnum;
        this.sequenceService = sequenceService;
        if (null != redissonClient) {
            this.lock = new RedisLock(redissonClient, sequenceEnum);
        } else {
            this.lock = new MemoryLock();
        }
        SequenceDateEnum dateEnum = sequenceEnum.getDateEnum();
        if (dateEnum != null) {
            this.lock.setDateFormat(dateEnum.getSimpleDateFormat());
        }
        // 设置是否为连续流水号
        this.lock.setContinueSequence(sequenceEnum.isContinueSequence());
    }

    private String getSequenceNumber(Integer currentNumber) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < (sequenceEnum.getSequenceLength() - String.valueOf(currentNumber).length()); i++) {
            sb.append(0);
        }
        sb.append(currentNumber);
        return sb.toString();
    }

    /**
     * 获取流水
     *
     * @return 流水号
     */
    @Override
    public String get() {
        return get(null);
    }

    private String getServerIp() {
        // 追加 服务器IP地址  临时解决在集群环境 会生成重复数据问题
        HostInfo hostInfo = SystemUtil.getHostInfo();
        String address = hostInfo.getAddress();
        String data = MD5.create().digestHex(address);
        String substring = data.substring(0, 8);
        return substring;
    }

    /**
     * 获取单个流水，带特殊编码
     *
     * @param specialCode 特殊编码
     * @return 流水号
     */
    public String get(String specialCode) {
        List<String> strings = get(1, specialCode, null);
        if (CollectionUtils.isEmpty(strings)) {
            return null;
        }
        return strings.get(strings.size() - 1);
    }

    /**
     * 获取单个流水，带特殊编码
     *
     * @param specialCode 特殊编码
     * @return 流水号
     */
    public String getNoCache(String specialCode) {
        List<String> strings = getNoCache(1, specialCode, null);
        if (CollectionUtils.isEmpty(strings)) {
            return null;
        }
        return strings.get(strings.size() - 1);
    }

    public String get(String specialCode, String lockKey) {
        List<String> strings = get(1, specialCode, lockKey);
        if (CollectionUtils.isEmpty(strings)) {
            return null;
        }
        return strings.get(strings.size() - 1);
    }

    /**
     * @param number              生成流水数量
     * @param sequencePlaceHolder 流水占位符，如BX20190103%
     * @param atomicInteger       流水增长
     */
    private ArrayList<String> generateSequence(Integer number, String sequencePlaceHolder, AtomicInteger atomicInteger) {
        ArrayList<String> generateSequence = new ArrayList<>(number);
        for (int i = 0; i < number; i++) {
            String fullSequence = String.format(sequencePlaceHolder, getSequenceNumber(atomicInteger.incrementAndGet()));
            generateSequence.add(fullSequence);
        }
        return generateSequence;
    }

    private String getLockKey(String sequencePrefixCacheKey, String lockTag) {
        if (!StringUtils.hasText(lockTag)) {
            return sequencePrefixCacheKey;
        }
        return sequenceEnum.getEnumName() + ":" + lockTag;
    }

    /**
     * 获取流水生成策略生成完成后的流水前缀
     *
     * @param specialCode 特殊编码
     * @return
     */
    private String getSequencePlaceHolder(String specialCode) {
        // 根据流水枚举获取流水生成策略
        BaseSequenceStrategy sequenceStrategy = SequenceStrategyFactory.createSequenceStrategy(sequenceEnum, specialCode);
        // 获取生成完成的流水前缀
        return sequenceStrategy.getSequencePlaceHolder();
    }

    private String getSequenceCacheKey(String sequencePlaceHolder) {
        // 将%s替换为空字符串作为内存缓存或者redis的key
        String sequenceCacheKey = String.format(sequencePlaceHolder, EMPTY_STRING);
        if (!StringUtils.hasText(sequenceCacheKey)) {
            sequenceCacheKey = sequenceEnum.getEnumName();
        }
        return sequenceCacheKey;
    }

    @Override
    public List<String> get(Integer number, String specialCode, String lockTag) {
        Assert.isTrue(number > 0, "获取的流水必须大于0");
        String sequencePlaceHolder = getSequencePlaceHolder(specialCode);
        String sequenceCacheKey = getSequenceCacheKey(sequencePlaceHolder);
        String lockKey = getLockKey(sequenceCacheKey, lockTag);
        logger.info("sequenceCacheKey===>【{}】，lockKey===>【{}】", sequenceCacheKey, lockKey);
        // 锁定流水获取方法
        lock.lock(lockKey);
        // 获取锁中的值
        AtomicInteger atomicInteger = lock.get(sequenceCacheKey);
        // 如果缓存中不存在该数值则新增
        if (atomicInteger == null) {
            try {
                // 到数据库查找枚举业务的最大流水
                int businessMaxSequence = getBusinessMaxSequence(sequenceCacheKey);
                lock.init(sequenceCacheKey, new AtomicInteger(businessMaxSequence));
            } catch (Exception e) {
                lock.unlock(lockKey);
                throw e;
            }
            atomicInteger = lock.get(sequenceCacheKey);
        }
        // 生成流水号
        ArrayList<String> sequenceList = generateSequence(number, sequencePlaceHolder, atomicInteger);
        // 将生成的流水号存入缓存
        lock.put(sequenceCacheKey, atomicInteger, lockKey);
        return sequenceList;
    }

    @Override
    public List<String> getNoCache(Integer number, String specialCode, String lockTag) {
        Assert.isTrue(number > 0, "获取的流水必须大于0");
        String sequencePlaceHolder = getSequencePlaceHolder(specialCode);
        String sequenceCacheKey = getSequenceCacheKey(sequencePlaceHolder);
        String lockKey = getLockKey(sequenceCacheKey, lockTag);
        logger.info("lockKey==>" + lockKey);
        // 锁定流水获取方法
        lock.lock(lockKey);
        // 获取锁中的值
        AtomicInteger atomicInteger = lock.get(sequenceCacheKey);
        // 如果缓存中不存在该数值则新增
        if (atomicInteger == null) {
            try {
                // 到数据库查找枚举业务的最大流水
                int businessMaxSequence = getBusinessMaxSequence(sequenceCacheKey);
                lock.init(sequenceCacheKey, new AtomicInteger(businessMaxSequence));
            } catch (Exception e) {
                lock.unlock(lockKey);
                throw e;
            }
            atomicInteger = lock.get(sequenceCacheKey);
        }
        // 生成流水号
        ArrayList<String> sequenceList = generateSequence(number, sequencePlaceHolder, atomicInteger);
        // 将生成的流水号存入缓存
        lock.put(sequenceCacheKey, atomicInteger, lockKey);
        return sequenceList;
    }

    @Override
    public void lockOnly(String specialCode, String lockTag) {
        String sequencePlaceHolder = getSequencePlaceHolder(specialCode);
        final String sequenceCacheKey = getSequenceCacheKey(sequencePlaceHolder);
        final String lockKey = getLockKey(sequenceCacheKey, lockTag);
        lock.lock(lockKey);
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCompletion(int status) {
                    lock.unlock(lockKey);
                }
            });
        }
    }

    @Override
    public boolean isLocked(String specialCode, String lockTag) {
        String sequencePlaceHolder = getSequencePlaceHolder(specialCode);
        final String sequenceCacheKey = getSequenceCacheKey(sequencePlaceHolder);
        final String lockKey = getLockKey(sequenceCacheKey, lockTag);
        return lock.isLocked(lockKey);
    }

    private int getBusinessMaxSequence(String sequencePrefixCacheKey) {
        String sequence;
        if (sequenceEnum.getMultiTables() != null && sequenceEnum.getMultiTables().length > 0) {
            sequence = sequenceService.getSequence(sequenceEnum.getMultiTables(), sequencePrefixCacheKey);
        } else {
            sequence = sequenceService.getSequence(sequenceEnum.getTableName(), sequenceEnum.getFieldName(), sequencePrefixCacheKey);
        }
        if (StringUtils.hasText(sequence)) {
            String sequenceNumber = sequence.replace(sequencePrefixCacheKey, EMPTY_STRING);
            logger.info("init {} sequence from Database table is {} fileName is {},and max sequence is {}", sequenceEnum, sequenceEnum.getTableName(), sequenceEnum.getFieldName(), sequenceNumber);
            return Integer.parseInt(sequenceNumber);
        }
        return 0;
    }
}
