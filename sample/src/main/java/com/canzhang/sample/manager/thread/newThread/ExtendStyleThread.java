package com.canzhang.sample.manager.thread.newThread;

/**
 * 继承Thread 的方式创建线程
 */
public class ExtendStyleThread extends Thread {

    // 票数
    private int tick = 3;

    @Override
    public void run() {
        while (true) {
            if (tick > 0) {
                System.out.println(currentThread().getName() + "卖票:" + tick--);
            }
        }
    }
}
