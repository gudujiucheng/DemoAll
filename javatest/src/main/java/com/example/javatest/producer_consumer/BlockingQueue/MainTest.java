package com.example.javatest.producer_consumer.BlockingQueue;

import java.util.concurrent.LinkedBlockingDeque;

public class MainTest {

    private static  int  MAX_SIZE = 5;

    public static  void  main(String[] args){
        LinkedBlockingDeque<Integer> deque = new LinkedBlockingDeque<>();
        Producer producer = new Producer("1号", deque, MAX_SIZE);
        Producer producer02 = new Producer("2号", deque, MAX_SIZE);
        Producer producer03 = new Producer("3号", deque, MAX_SIZE);

        Consumer consumer = new Consumer("a", deque, MAX_SIZE);
        Consumer consumer2 = new Consumer("b", deque, MAX_SIZE);
        Consumer consumer3 = new Consumer("c", deque, MAX_SIZE);

        producer.start();
        producer02.start();
        producer03.start();
        consumer.start();
        consumer2.start();
        consumer3.start();

    }
}
