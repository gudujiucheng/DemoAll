package com.canzhang.sample.manager.thread.bingfa;

/**
 * implements 的方式创建线程
 */
public class SleepThread implements Runnable {

    // 票数
    private int tick = 3;

    @Override
    public void run() {

        while (true) {
            if (tick > 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "卖票:" + tick--);
            }
        }
    }
}
