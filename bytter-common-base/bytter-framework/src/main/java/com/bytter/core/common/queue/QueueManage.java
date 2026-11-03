package com.bytter.core.common.queue;
import java.util.LinkedList;
import java.util.Queue;
public class QueueManage {
    Queue<Object> queue = new LinkedList<Object>();
    public static class Singleton {
        static final QueueManage instance = new QueueManage();
    }
    public static QueueManage getInstance() {
        return Singleton.instance;
    }
    public synchronized void addMessageObject(Object s) {
        this.notifyAll();
        queue.offer(s);
    }
    public synchronized Object delMessageInfo() {
        if (queue.size() == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
        }
        return queue.poll();
    }
    public int num() {
        return queue.size();
    }
}
