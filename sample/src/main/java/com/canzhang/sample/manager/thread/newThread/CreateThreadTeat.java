package com.canzhang.sample.manager.thread.newThread;

/**
 * 创建线程方法简单示例
 */
public class CreateThreadTeat {


    public static void main(String[] args) {
        test();
    }

    private static void test() {

        MyThread my1 = new MyThread();
        MyThread my2 = new MyThread();
        my1.start();
        my2.start();

        //很明显的买票结果，3张票卖了两遍
//
//        Thread-1卖票:3
//        Thread-0卖票:3
//        Thread-1卖票:2
//        Thread-1卖票:1
//        Thread-0卖票:2
//        Thread-0卖票:1

    }


    private static void log(String str) {
        System.out.print("\n当前线程：" + Thread.currentThread().getName() + " 携带数据：" + str);
    }


}
