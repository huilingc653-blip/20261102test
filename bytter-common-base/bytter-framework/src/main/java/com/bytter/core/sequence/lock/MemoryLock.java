package com.bytter.core.sequence.lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import com.bytter.core.sequence.interfaces.ISequenceLock;
import com.bytter.core.sequence.transaction.MemoryTransactionHolder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
/**
 * 流水号-单机节点生成
 *
 * @author zhoul
 * @date 2019/01/02
 */
public class MemoryLock extends AbstractTransactionThreadLock implements ISequenceLock {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private SimpleDateFormat dateFormat;
    private boolean continueSequence;
    private ConcurrentHashMap<String, ReentrantLock> lockClient = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, CacheData> cache = new ConcurrentHashMap<>();
    @Override
    public AtomicInteger get(String key) {
        //当前线程是否存在活动事务
        if (continueSequence && TransactionSynchronizationManager.isSynchronizationActive()) {
            //从当前线程中获取流水内存事务持久对象
            MemoryTransactionHolder memoryTransactionHolder = (MemoryTransactionHolder) getResource(key);
            //验证当前线程是否存在流水内存事务持久对象
            if (memoryTransactionHolder == null) {
                //从内存中获取
                CacheData cacheData = cache.get(key);
                //存在流水内存事务持久对象
                if (cacheData != null) {
                    //获取内存中的流水序列
                    AtomicInteger data = cacheData.getData();
                    if (data == null) {
                        return null;
                    }
                    //复制一份，存入当前线程中
                    memoryTransactionHolder = new MemoryTransactionHolder(new AtomicInteger(data.intValue()));
                    bindResource(key, memoryTransactionHolder);
                } else {
                    return null;
                }
            }
            //最后返回流水数据
            return memoryTransactionHolder.getData();
        } else {
            //当前线程不存在事务，则直接从内存中获取流水数据
            CacheData cacheData = cache.get(key);
            if (cacheData == null) {
                return null;
            }
            return cacheData.getData();
        }
    }
    @Override
    public void init(String key, AtomicInteger atomicInteger) {
        //单独线程进入到if块中，进行创建，后续线程再作判断时，cache中已存在实例对象
        CacheData cacheData;
        SimpleDateFormat dateFormat = getDateFormat();
        if (dateFormat == null) {
            cacheData = new CacheData();
        } else {
            cacheData = new CacheData(dateFormat.format(new Date()));
        }
        cacheData.setData(atomicInteger);
        cache.put(key, cacheData);
    }
    @Override
    public void put(final String key, final AtomicInteger atomicInteger, final String lockKey) {
        //当前线程是否存在活动事务
        if (continueSequence && TransactionSynchronizationManager.isSynchronizationActive()) {
            //从当前线程中获取流水内存事务持久对象
            MemoryTransactionHolder memoryTransactionHolder = (MemoryTransactionHolder) getResource(key);
            //设置内存中自增长的数据
            memoryTransactionHolder.setData(atomicInteger);
            //使用统计对象累加统计调用次数
            memoryTransactionHolder.getCount().incrementAndGet();
            //注册spring事务回调事件
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCompletion(int status) {
                    //从当前线程中获取流水内存事务持久对象
                    MemoryTransactionHolder transactionHolder = (MemoryTransactionHolder) getResource(key);
                    //获取统计对象
                    AtomicInteger count = transactionHolder.getCount();
                    //回调一次，减一，等于0为所有事务都回调完毕
                    if (count.decrementAndGet() == 0) {
                        //当事务为提交时，提交临时数据到内存中
                        if (status == TransactionSynchronization.STATUS_COMMITTED) {
                            //CacheData加入时间，为了获取时清除非当天或当月的缓存
                            CacheData cacheData = cache.get(key);
                            cacheData.setData(atomicInteger);
                            cache.put(key, cacheData);
                        }
                        //从当前线程中解绑当前流水资源
                        unbindResource(key);
                    }
                    //释放锁
                    unlock(lockKey);
                }
            });
        } else {
            CacheData cacheData = cache.get(key);
            cacheData.setData(atomicInteger);
            cache.put(key, cacheData);
            unlock(lockKey);
        }
    }
    @Override
    public void lock(String lockKey) {
        //在lock之前，查看cache中是否存在lockKey，加上synchronized避免多个线程竞争创建实例
        synchronized (this) {
            if (!lockClient.containsKey(lockKey)) {
                lockClient.put(lockKey, new ReentrantLock());
            }
        }
        lockClient.get(lockKey).lock();
        logger.info("Thread:{},lockKey:{},status:lock", Thread.currentThread().getName(), lockKey);
    }
    @Override
    public void unlock(String lockKey) {
        lockClient.get(lockKey).unlock();
        logger.info("Thread:{},lockKey:{},status:unlock", Thread.currentThread().getName(), lockKey);
        //先清空非当天或当月的流水
        cleanNotCurrentDate();
    }
    @Override
    public boolean isLocked(String lockKey) {
        if (!lockClient.containsKey(lockKey)) {
            return false;
        }
        return lockClient.get(lockKey).isLocked();
    }
    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }
    @Override
    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }
    @Override
    public void setContinueSequence(boolean continueSequence) {
        this.continueSequence = continueSequence;
    }
    /**
     * 清空非当前日期流水号
     */
    private void cleanNotCurrentDate() {
        if (getDateFormat() == null) {
            return;
        }
        String format = getDateFormat().format(new Date());
        Iterator<Map.Entry<String, CacheData>> entries = cache.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, CacheData> entry = entries.next();
            CacheData value = entry.getValue();
            if (!format.equals(value.getDate())) {
                entries.remove();
            }
        }
    }
    /**
     * 内存流水号持久类
     */
    private static class CacheData {
        public CacheData() {
        }
        public CacheData(String date) {
            this.date = date;
        }
        private AtomicInteger data;
        private String date;
        public AtomicInteger getData() {
            return data;
        }
        public void setData(AtomicInteger data) {
            this.data = data;
        }
        public String getDate() {
            return date;
        }
        public void setDate(String date) {
            this.date = date;
        }
    }
}
