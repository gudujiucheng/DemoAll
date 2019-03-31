package com.canzhang.sample.manager.thread.newThread;

/**
 * implements 的方式创建线程
 */
public class ImplementStyleThread implements Runnable {

    // 票数
    private int tick = 3;

    @Override
    public void run() {
        while (true) {
            if (tick > 0) {
                System.out.println(Thread.currentThread().getName() + "卖票:" + tick--);
            }
        }
    }
}
