package com.bytter.core.utils;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

public class TransactionalUtil {

    /**
     * 事务提交后同步执行某任务
     * @param runnable 任务
     */
    public static void afterCommitSync(Runnable runnable) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                   runnable.run();
                }
            });
        }else {
            runnable.run();
        }
    }

    /**
     * 事务提交后同步执行某任务
     * @param callable 任务
     */
    public static <T> T afterCommitSyncCallable(Callable<T> callable) throws Exception {
        FutureTask<T> tFutureTask = new FutureTask<>(callable);
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    try {
                        tFutureTask.run();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }else {
            tFutureTask.run();
        }
        return tFutureTask.get();
    }

    /**
     * 事务提交后异步执行某任务
     * @param runnable 任务
     */
    public static void afterCommitAsync(Executor executor, Runnable runnable) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    executor.execute(runnable);
                }
            });
        } else {
            executor.execute(runnable);
        }
    }
}
