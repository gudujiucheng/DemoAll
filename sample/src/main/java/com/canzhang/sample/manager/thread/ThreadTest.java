package com.canzhang.sample.manager.thread;

/**
 * 创建线程方法简单示例
 */
public class ThreadTest {


    public static void main(String[] args) {
        test();
    }

    private static void test() {



    }


    private static void log(String str) {
        System.out.print("\n当前线程：" + Thread.currentThread().getName() + " 携带数据：" + str);
    }


}
