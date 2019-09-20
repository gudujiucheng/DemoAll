package com.canzhang.sample.manager.thread.fqlthreadpool;

import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by owenli on 2018/4/18.
 */

public class FqlPriorityBlockingQueue<E> extends PriorityBlockingQueue {

    private int maxSize = 0;
    public static boolean DEBUG = false;

    public FqlPriorityBlockingQueue() {
        super();
    }

    public FqlPriorityBlockingQueue(int initialCapacity) {
        super(initialCapacity);
    }

    public FqlPriorityBlockingQueue(int initialCapacity,
                                    Comparator<? super E> comparator) {
        super(initialCapacity, comparator);
    }

    public FqlPriorityBlockingQueue(Collection<? extends E> c) {
        super(c);
    }

    @Override
    public boolean offer(Object o) {
        boolean result = super.offer(o);
        if (DEBUG) {
            if (maxSize < size()) {
                maxSize = size();
            }
        }
        return result;
    }

    public int getMaxSize() {
        return maxSize;
    }
}
