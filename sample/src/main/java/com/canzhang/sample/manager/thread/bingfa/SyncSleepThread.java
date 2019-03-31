package com.canzhang.sample.manager.thread.bingfa;

/**
 * implements 的方式创建线程
 */
public class SyncSleepThread implements Runnable {

    // 票数
    private int tick = 3;
    private final Object oj = new Object();

    @Override
    public void run() {

        while (true) {
            synchronized (oj) {
                if (tick > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "卖票:" + tick--);
                }
            }
        }
    }
}
