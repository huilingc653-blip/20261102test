package com.bytter.core.sequence.transaction;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * @author zhoul
 */
public class MemoryTransactionHolder {
    private AtomicInteger data;
    private AtomicInteger count;
    public MemoryTransactionHolder() {
        this.count = new AtomicInteger();
    }
    public MemoryTransactionHolder(AtomicInteger data) {
        this();
        this.data = data;
    }
    public AtomicInteger getData() {
        return data;
    }
    public void setData(AtomicInteger data) {
        this.data = data;
    }
    public AtomicInteger getCount() {
        return count;
    }
    public void setCount(AtomicInteger count) {
        this.count = count;
    }
}
