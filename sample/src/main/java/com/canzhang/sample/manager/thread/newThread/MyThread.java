package com.canzhang.sample.manager.thread.newThread;

public class MyThread extends Thread {

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
