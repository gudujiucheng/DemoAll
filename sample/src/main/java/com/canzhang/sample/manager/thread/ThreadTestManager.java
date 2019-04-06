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



    private ComponentItem syncMethodThreadTest() {

        //对多条操作共享数据的语句，只能让一个线程都执行完再执行过程中其他线程不可以参与运行
        return new ComponentItem("多线程存钱（同步方法 正常）",
                "1、非静态同步方法的锁是this，不同对象持有的锁自然也是不同的\n" +
                        "2、静态同步方法的锁是类对象本身，所以和非静态同步方法的锁是不同的，没有竞态关系\n" +
                        "3、控制域是整个方法（注意不要放非同步的内容进去导致侠侣的降低）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


    private ComponentItem syncBlockThreadTest() {

        //对多条操作共享数据的语句，只能让一个线程都执行完再执行过程中其他线程不可以参与运行
        return new ComponentItem("多线程卖票（同步代码块 正常）",
                "1、需要保证是同一个锁\n" +
                        "2、使用一个引用对象的实例变量作为锁并不是一个好的选择，因为同步块在执行过程中可能会改变它的值，" +
                        "其中就包括将其设置为null，而对一个null对象加锁会产生异常(空指针)，并且对不同的对象加锁也违背了同步的初衷！" +
                        "一定要理解清楚，这里的锁指的是实际对象，而不是其引用", new View.OnClickListener() {
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

        return new ComponentItem("多线程卖票（异常）", "如果不加锁，多个线程同时操作一个共享数据，就会出错",new View.OnClickListener() {
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

        return new ComponentItem("Runnable方式创建线程",
                "1、此方式创建线程可以避免多继承问题",new View.OnClickListener() {
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
