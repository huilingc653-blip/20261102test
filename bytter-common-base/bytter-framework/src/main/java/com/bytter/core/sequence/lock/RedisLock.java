package com.bytter.core.sequence.lock;

import com.bytter.core.sequence.interfaces.ISequenceEnum;
import com.bytter.core.sequence.interfaces.ISequenceLock;
import com.bytter.core.sequence.transaction.RedisTransactionHolder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 221212fix-bug 解决集群部署启用redis生成序列号没有自定义前缀导致多个业务共享问题
 * 集群流水号生成及锁
 *
 * @author zhoul
 * @date 2019/01/02
 */
@Slf4j
public class RedisLock extends AbstractTransactionThreadLock implements ISequenceLock {
    private static final Long DEFAULT_LOCK_TIME = 3L;
    private static final String LOCK_NAME_PREFIX = "sequence:lock:";
    private static final String SEQUENCE_NAME_PREFIX = "sequence:number:";
    private static final String DAY_PLACE_HOLDER = "dd";
    private SimpleDateFormat dateFormat;
    private final RedissonClient redissonClient;
    private boolean continueSequence;
    private ISequenceEnum sequenceEnum;

    public RedisLock(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public RedisLock(RedissonClient redissonClient, ISequenceEnum sequenceEnum) {
        this.redissonClient = redissonClient;
        this.sequenceEnum = sequenceEnum;
    }

    private String getSeqPrefixKey() {
        return String.format("%s%s:", SEQUENCE_NAME_PREFIX, sequenceEnum.getTableName());
    }

    private String getLockPrefixKey() {
        return String.format("%s%s:", LOCK_NAME_PREFIX, sequenceEnum.getTableName());
    }

    private RedisTransactionHolder createRedisTransactionHolder() {
        TransactionOptions defaults = TransactionOptions.defaults();
        defaults.timeout(DEFAULT_LOCK_TIME, TimeUnit.MINUTES);
        RTransaction transaction = redissonClient.createTransaction(defaults);
        return new RedisTransactionHolder(transaction);
    }

    @Override
    public AtomicInteger get(String key) {
        if (continueSequence && TransactionSynchronizationManager.isSynchronizationActive()) {
            RedisTransactionHolder redisTransactionHolder = (RedisTransactionHolder) getResource(key);
            if (redisTransactionHolder == null) {
                RBucket<Object> map = redissonClient.getBucket(getSeqPrefixKey() + key);
                AtomicInteger atomicInteger = (AtomicInteger) map.get();
                if (atomicInteger != null) {
                    redisTransactionHolder = createRedisTransactionHolder();
                    redisTransactionHolder.setData(new AtomicInteger(atomicInteger.intValue()));
                    bindResource(key, redisTransactionHolder);
                } else {
                    return null;
                }
            }
            return redisTransactionHolder.getData();
        } else {
            RBucket<Object> map = redissonClient.getBucket(getSeqPrefixKey() + key);
            return (AtomicInteger) map.get();
        }
    }

    @Override
    public void init(String key, AtomicInteger atomicInteger) {
        RBucket<Object> map = redissonClient.getBucket(getSeqPrefixKey() + key);
        map.set(atomicInteger);
    }

    @Override
    public void put(final String key, final AtomicInteger atomicInteger, final String lockKey) {
        if (continueSequence && TransactionSynchronizationManager.isSynchronizationActive()) {
            RedisTransactionHolder redisTransactionHolder = (RedisTransactionHolder) getResource(key);
            if (redisTransactionHolder == null) {
                redisTransactionHolder = createRedisTransactionHolder();
            }
            redisTransactionHolder.setData(atomicInteger);
            redisTransactionHolder.getCount().incrementAndGet();
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCompletion(int status) {
                    try {
                        RedisTransactionHolder transactionData = (RedisTransactionHolder) getResource(key);
                        AtomicInteger count = transactionData.getCount();
                        if (count.decrementAndGet() == 0) {
                            if (status == TransactionSynchronization.STATUS_COMMITTED) {
                                RTransaction transaction = transactionData.getTransaction();
                                RBucket<Object> map = transaction.getBucket(getSeqPrefixKey() + key);
                                map.set(atomicInteger, getTimeToLive(), TimeUnit.MILLISECONDS);
                                transaction.commit();
                            }
                            unbindResource(key);
                        }
                    } catch (Exception e) {
                        log.error("RedisLock.put error", e);
                    } finally {
                        unlock(lockKey);
                    }
                }
            });
        } else {
            try {
                RBucket<Object> map = redissonClient.getBucket(getSeqPrefixKey() + key);
                map.set(atomicInteger, getTimeToLive(), TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                log.error("RedisLock.put error", e);
            } finally {
                unlock(key);
            }
        }
    }

    private long getTimeToLive() {
        SimpleDateFormat dateFormat = getDateFormat();
        if (dateFormat == null) {
            return 1;
        }
        // 使用线程安全的日期API 防止计算的过期时间小于0的情况
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now;
        if (!dateFormat.toPattern().contains(DAY_PLACE_HOLDER)) {
            // 不包含dd取月末最后一天沿用以前的逻辑
            expireTime = expireTime.with(TemporalAdjusters.lastDayOfMonth());
        }
        expireTime = expireTime.with(LocalTime.MAX);
        long ttl = ChronoUnit.MILLIS.between(now, expireTime);
        return ttl > 0 ? ttl : 24 * 60 * 60 * 1000L;
    }

    @Override
    public void lock(String lockKey) {
        RLock lock = redissonClient.getLock(getLockPrefixKey() + lockKey);
        lock.lock(DEFAULT_LOCK_TIME, TimeUnit.MINUTES);
    }

    @Override
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(getLockPrefixKey() + lockKey);
        lock.unlock();
    }

    @Override
    public boolean isLocked(String lockKey) {
        RLock lock = redissonClient.getLock(getLockPrefixKey() + lockKey);
        if (lock == null) {
            return false;
        }
        return lock.isLocked();
    }

    @Override
    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public void setContinueSequence(boolean continueSequence) {
        this.continueSequence = continueSequence;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }
}
