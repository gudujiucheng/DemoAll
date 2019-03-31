package com.canzhang.sample.manager.thread.bingfa.bankdemo;

/**
 * implements 的方式创建线程
 */
public class BankThread implements Runnable {

    private Bank b = new Bank();

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            b.add(100);
        }
    }
}
