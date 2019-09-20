package com.canzhang.sample.manager.thread.fqlthreadpool;

/**
 * Created by owenli on 2018/4/9.
 */

public abstract class AbstractTask implements Runnable, Comparable<AbstractTask> {

    public static final int HIGH = Integer.MIN_VALUE;
    public static final int NORMAL = 0;
    public static final int LOW = Integer.MAX_VALUE;

    private int priority;

    public AbstractTask(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(AbstractTask o) {
        if (priority < o.priority) {
            return -1;
        } else {
            if (priority > o.priority) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
