package com.bytter.biz.payment.api.manage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockManager {
    private final ConcurrentHashMap<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    public Lock getLock(String businessDataId) {
        return lockMap.computeIfAbsent(businessDataId, k -> new ReentrantLock());
    }
}
