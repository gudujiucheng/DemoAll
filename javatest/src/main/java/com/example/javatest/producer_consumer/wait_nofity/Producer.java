package com.example.javatest.producer_consumer.wait_nofity;

import java.util.Queue;
import java.util.Random;


public class Producer  extends   Thread{

    private String name;
    private Queue<Integer> queue;
    private int maxSize;
    int i = 0;
    public Producer(String name, Queue<Integer> queue,int maxSize){
        super(name);
        this.name = name;
        this.queue = queue;
        this.maxSize = maxSize;

    }
    @Override
    public void run() {

        while (true){
            synchronized (queue){
                while (queue.size()==maxSize){
                    try {
                        System.out .println("满了，暂停生产, Producer[" + name + "] thread 等待 " + "消费者消费");
                        queue.wait();//释放锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                System.out.println("[" + name + "] Producing value : +" + i);
                //将产品插入
                queue.offer(i++);
                queue.notifyAll();//重新触发

                try {
                    Thread.sleep(new Random().nextInt(1000));//随机暂停，不释放锁
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }




    }
}
