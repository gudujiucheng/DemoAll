package com.canzhang.sample.manager.thread;

import android.app.Activity;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.canzhang.sample.manager.thread.bingfa.SleepThread;
import com.canzhang.sample.manager.thread.bingfa.SyncSleepThread;
import com.canzhang.sample.manager.thread.bingfa.bankdemo.BankThread;
import com.canzhang.sample.manager.thread.newThread.ExtendStyleThread;

import java.util.ArrayList;
import java.util.List;

/**
 * 线程操作相关（并发等场景测试）
 */
public class ThreadTestManager extends BaseManager {


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {

        List<ComponentItem> list = new ArrayList<>();
        list.add(createThread());
        list.add(createThread02());
        list.add(moreThreadTest01());
        list.add(moreThreadTest02());
        list.add(syncBlockThreadTest());
        list.add(syncMethodThreadTest());
        return list;
    }


    /**
     *
     * ﻿同步函数的锁是this
     */
    private ComponentItem syncMethodThreadTest() {

        //对多条操作共享数据的语句，只能让一个线程都执行完再执行过程中其他线程不可以参与运行
        return new ComponentItem("多线程存钱（同步方法 正常）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * ﻿1.明确哪些代码是多线成运行代码
                 * 2.明确共享数据
                 * 3.明确多线成运行代码中哪些语句是操作共享数据的
                 *
                 */
                Runnable runnable = new BankThread();
                Thread thread = new Thread(runnable);
                Thread thread2 = new Thread(runnable);
                thread.start();
                thread2.start();


//                Thread-0 sum:100
//                Thread-0 sum:200
//                Thread-0 sum:300
//                Thread-1 sum:400
//                Thread-1 sum:500
//                Thread-1 sum:600


            }
        });
    }


    /**
     *
     * ﻿我们为什么可以这样去同步线程？
     *
     * 对象如同锁，持有锁的线程可以在同步中执行，没有执行锁的线程即使获取了CPU的执行权，也进不去，因为没有获取锁，我们可以这样理解
     * 四个线程，哪一个进去就开始执行，其他的拿不到执行权，所以即使拿到了执行权，也进不去，这个同步能解决线程的安全问题
     * 但是，同步是有前提的
     * 1.必须要有两个或者两个以上的线程，不然你同步也没必要呀
     * 2.必须是多个线程使用同一锁
     * 必须保证同步中只能有一个线程在运行
     *
     * 但是他也有一个弊端：那就是多个线程都需要判断锁，较为消耗资源
     */
    private ComponentItem syncBlockThreadTest() {

        //对多条操作共享数据的语句，只能让一个线程都执行完再执行过程中其他线程不可以参与运行
        return new ComponentItem("多线程卖票（同步代码块 正常）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //公用同一个runnable
                Runnable runnable = new SyncSleepThread();
                Thread thread = new Thread(runnable);
                Thread thread2 = new Thread(runnable);
                thread.start();
                thread2.start();


//        Thread-0卖票:3
//        Thread-0卖票:2
//        Thread-0卖票:1

            }
        });
    }


    private ComponentItem moreThreadTest02() {

        return new ComponentItem("多线程卖票（sleep场景 异常2）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //公用同一个runnable
                Runnable runnable = new SleepThread();
                Thread thread = new Thread(runnable);
                Thread thread2 = new Thread(runnable);
                thread.start();
                thread2.start();

                //出现了0票，这显然是不正常的，当多条语句在操作同一个线程共享数据时，一个线程对多条语句只执行了一个部分，还没有执行完，另外一个线程参与了执行，导致共享数据的错误

//                Thread-1卖票:3
//                Thread-0卖票:2
//                Thread-0卖票:0
//                Thread-1卖票:1

            }
        });
    }


    private ComponentItem moreThreadTest01() {

        return new ComponentItem("多线程卖票（异常）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ExtendStyleThread my1 = new ExtendStyleThread();
                ExtendStyleThread my2 = new ExtendStyleThread();
                my1.start();
                my2.start();

                //很明显的买票结果，3张票卖了两遍,这样是不合理的
//
//        Thread-1卖票:3
//        Thread-0卖票:3
//        Thread-1卖票:2
//        Thread-1卖票:1
//        Thread-0卖票:2
//        Thread-0卖票:1

            }
        });
    }


    private ComponentItem createThread02() {

        return new ComponentItem("继承Thread方式实现", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ExtendStyleThread().start();

            }
        });
    }

    private ComponentItem createThread() {

        return new ComponentItem("Runnable方式创建线程", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread01 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //会有个默认的名字
                        System.out.print("线程名字：" + Thread.currentThread().getName());
                    }
                });


                Thread thread02 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.print("\n线程名字：" + Thread.currentThread().getName());
                    }
                }, "66666");


                thread01.start();
                thread02.start();

            }
        });
    }


}
