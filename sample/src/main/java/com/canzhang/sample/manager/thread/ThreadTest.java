package com.canzhang.sample.manager.thread;

import com.canzhang.sample.manager.thread.bingfa.bankdemo.BankThread;

/**
 * 创建线程方法简单示例
 */
public class ThreadTest {
    static Object o = new Object();

    public static void main(String[] args) {
        test();
    }

    private static void test() {

        new Thread(new MyRunnable()).start();
        new Thread(new MyRunnable()).start();
    }


    private static void log(String str) {
        System.out.print("\n当前线程：" + Thread.currentThread().getName() + " 携带数据：" + str);
    }


    static class  MyRunnable implements  Runnable{

        @Override
        public void run() {
            synchronized (o){

                for (int i = 0; i <3; i++) {
                    log(i+"");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

        }
    }


}
