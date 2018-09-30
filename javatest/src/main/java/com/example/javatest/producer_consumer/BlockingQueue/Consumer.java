package com.example.javatest.producer_consumer.BlockingQueue;

import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;

public class Consumer extends Thread {

    private String name;
    private LinkedBlockingDeque<Integer> deque;
    private int maxSize;

    public Consumer(String name, LinkedBlockingDeque<Integer> deque, int maxSize) {
        super(name);
        this.name = name;
        this.deque = deque;
        this.maxSize = maxSize;

    }

    @Override
    public void run() {

        while (true) {

            try {
                while (!deque.isEmpty()) {
                    int x = deque.take();
                    System.out.println("消费者  [" + name + "] Consuming : " + x);
                }

                //最多暂停一秒
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
