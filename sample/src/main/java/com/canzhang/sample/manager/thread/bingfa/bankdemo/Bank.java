package com.canzhang.sample.manager.thread.bingfa.bankdemo;

public class Bank {

    int sum;

    /**
     * 同步方法
     * @param value
     */
    public synchronized void add(int value){
        sum = sum+value;

        System.out.println(Thread.currentThread().getName() +" sum:"+sum);

    }


}
