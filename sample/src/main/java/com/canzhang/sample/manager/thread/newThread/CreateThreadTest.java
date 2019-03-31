package com.canzhang.sample.manager.thread.newThread;

import com.canzhang.sample.manager.thread.bingfa.SleepThread;
import com.canzhang.sample.manager.thread.bingfa.SyncSleepThread;
import com.canzhang.sample.manager.thread.bingfa.bankdemo.BankThread;

/**
 * 创建线程方法简单示例
 */
public class CreateThreadTest {


    public static void main(String[] args) {
        test();
    }

    private static void test() {
        /**
         * ﻿1.明确哪些代码是多线成运行代码
         * 2.明确共享数据
         * 3.明确多线成运行代码中哪些语句是操作共享数据的
         *
         */
        Runnable runnable = new BankThread();
        Thread thread = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        thread.start();
        thread2.start();


//        Thread-0卖票:3
//        Thread-0卖票:2
//        Thread-0卖票:1



    }


    private static void log(String str) {
        System.out.print("\n当前线程：" + Thread.currentThread().getName() + " 携带数据：" + str);
    }


}
