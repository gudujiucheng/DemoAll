package com.example.javatest;

import java.util.LinkedList;
import java.util.Queue;

public class MainTest {

    public static Queue<Integer> queue = new LinkedList<>();
    static int maxSize = 5;
    static int i;
    public static void main(String[] args) {
        test03();
    }

    private static void test03() {
        int a = 0;
        int b = 1;
        System.out.print("b%0:"+b%a);
    }


    private static void test02() {

        int a = 120*58/168;
        int b = (int)(120*58f/168);
        System.out.print("a:"+a+" b:"+b);
    }

    private static void test01() {
        int[] s = new int[]{1,-3,4,-5,7,10,-22};

        int tempPosition = -1;
        for (int j = 0; j <s.length ; j++) {
            int x = s[j];
            if(x<0){//跟前面的换位
                int temp = s[tempPosition+1];
                s[tempPosition+1] = x;
                s[j] =temp;
                tempPosition++;
            }
        }


        for (int j = 0; j <s.length ; j++) {
            System.out.print(s[j]+"\n");
        }
    }
























    private static void test() {

        new Pro("1x").start();
        new Pro("2x").start();
        new Pro("3x").start();
        new Con("1c").start();
        new Con("2c").start();
        new Con("3c").start();


    }



    public static class  Con extends  Thread{//消费者
        private String name;
        public Con(String name){
            this.name = name;
        }


        @Override
        public void run() {
           while (true){
               synchronized (queue){
                   while (queue.size()>=5){
                       try {
                           System.out.print(name+" 仓库满了");
                           queue.wait();
                           //没货了
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }

                  queue.offer(i);
                   i++;
                   queue.notifyAll();
                   //消费了x
                   System.out.print(name+" 生产了:"+i);

                   try {
                       Thread.sleep(1000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           }



        }
    }


    public static class  Pro extends  Thread{//消费者
        private String name;
        public Pro(String name){
            this.name = name;
        }


        @Override
        public void run() {
            while (true){
                synchronized (queue){
                    while (queue.isEmpty()){
                        try {
                            System.out.print(name+" 没货了");
                            queue.wait();
                            //没货了
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    int x = queue.poll();
                    queue.notifyAll();
                    //消费了x
                    System.out.print(name+" 消费了:"+x);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }



        }
    }


}
