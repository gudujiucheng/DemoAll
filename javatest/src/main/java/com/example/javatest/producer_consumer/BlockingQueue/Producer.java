package com.example.javatest.producer_consumer.BlockingQueue;

import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;

public class Producer extends Thread {
    private String name;
    private LinkedBlockingDeque<Integer> deque;
    private int maxSize;
    int i;

    public Producer(String name, LinkedBlockingDeque<Integer> deque, int maxSize) {
        super(name);
        this.name = name;
        this.deque = deque;
        this.maxSize = maxSize;
    }


    @Override
    public void run() {
        while (true) {
            try {
                while (deque.size() < maxSize) {
                    deque.put(i);
                    System.out.println("生产：[" + name + "] Producing value : +" + i);
                    i++;
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
