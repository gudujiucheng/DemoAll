package com.example.javatest.producer_consumer.wait_nofity;


import java.util.LinkedList;
import java.util.Queue;


/**
 * 传统的  wait notify 实现
 */
public class MainTest {

    private static final int  maxSize = 5;

    public static  void main(String[] args){
        Queue<Integer> queue = new LinkedList<>();

        Producer producer = new Producer("1号", queue, maxSize);
        Producer producer02 = new Producer("2号", queue, maxSize);
        Producer producer03 = new Producer("3号", queue, maxSize);

        Consumer consumer = new Consumer("a", queue, maxSize);
        Consumer consumer2 = new Consumer("b", queue, maxSize);
        Consumer consumer3 = new Consumer("c", queue, maxSize);

        producer.start();
        producer02.start();
        producer03.start();


        consumer.start();
        consumer2.start();
        consumer3.start();


    }
}
