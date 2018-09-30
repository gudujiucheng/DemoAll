package com.example.javatest.producer_consumer.wait_nofity;

import java.util.Queue;
import java.util.Random;

public class Consumer extends  Thread{

    private String name;
    private Queue<Integer> queue;
    private int maxSize;

    public Consumer(String name, Queue<Integer> queue,int maxSize){
        super(name);
        this.name = name;
        this.queue = queue;
        this.maxSize = maxSize;

    }
    @Override
    public void run() {

        while (true){
            synchronized (queue){
                while (queue.isEmpty()){
                    try {
                        System.out .println("消费完了, 消费者[" + name + "] thread 等待 " + "生产者生产");
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                int x = queue.poll();
                System.out.println("[" + name + "] Consuming value : " + x);
                queue.notifyAll();

                try {
                    Thread.sleep(new Random().nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }

    }
}
