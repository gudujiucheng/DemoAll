package com.canzhang.sample.manager.thread;

import android.app.Activity;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.canzhang.sample.debug.DebugBaseApp;
import com.canzhang.sample.manager.thread.bingfa.SleepThread;
import com.canzhang.sample.manager.thread.bingfa.SyncSleepThread;
import com.canzhang.sample.manager.thread.bingfa.bankdemo.BankThread;
import com.canzhang.sample.manager.thread.demo.fqlreport.AppEvent;
import com.canzhang.sample.manager.thread.demo.fqlreport.UniversalReport;
import com.canzhang.sample.manager.thread.fqlthreadpool.ThreadPoolUtils;
import com.canzhang.sample.manager.thread.newThread.ExtendStyleThread;
import com.example.base.base.AppProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 线程操作相关（并发等场景测试）
 */
public class ThreadTestManager extends BaseManager {


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        //初始化上报组件
        UniversalReport.init(AppProxy.getInstance().getApplication());

        List<ComponentItem> list = new ArrayList<>();
        list.add(testSync());
        list.add(testLocal());
        list.add(testFqlThreadPool());
        list.add(fqlReport());
        list.add(createThread());
        list.add(createThread02());
        list.add(moreThreadTest01());
        list.add(moreThreadTest02());
        list.add(syncBlockThreadTest());
        list.add(syncMethodThreadTest());
        list.add(sleepAndWait());
        list.add(waitTest01());
        list.add(sleepTest01());
        list.add(waitTest02());
        list.add(waitTest03());
        list.add(deadLock());
        list.add(countDownLatchTest());//https://www.jianshu.com/p/e233bb37d2e6
        //TODO 多线程、各种锁的介绍、各种集合的应用
        return list;
    }

    private ComponentItem countDownLatchTest() {
        return new ComponentItem("测试 countDownLatch应用 ", "countDownLatch这个类使一个线程等待其他线程各自执行完毕后再执行。",new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //是通过一个计数器来实现的，计数器的初始值是线程的数量。每当一个线程执行完毕后，计数器的值就-1，当计数器的值为0时，表示所有线程都执行完毕，然后在闭锁上等待的线程就可以恢复工作了。
                final CountDownLatch latch = new CountDownLatch(2);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        latch.countDown();
                        log("线程1执行完毕");

                    }
                }).start();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        latch.countDown();
                        log("线程2执行完毕");
                    }
                }).start();
                try {
                    log("主线程等待 start");
                    latch.await();//调用await()方法的线程会被挂起，它会等待直到count值为0才继续执行
                    log("主线程等待 end");


                    //还有一个重载方法 和await()类似，只不过等待一定的时间后count值还没变为0的话就会继续执行
                    //public boolean await(long timeout, TimeUnit unit) throws InterruptedException { };
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //测试效果
//                2020-09-25 15:41:35.840 11129-11129/com.canzhang.sample E/Test: ThreadTestManager:主线程等待 start
//                2020-09-25 15:41:38.842 11129-12130/com.canzhang.sample E/Test: ThreadTestManager:线程2执行完毕
//                2020-09-25 15:41:38.842 11129-12129/com.canzhang.sample E/Test: ThreadTestManager:线程1执行完毕
//                2020-09-25 15:41:38.843 11129-11129/com.canzhang.sample E/Test: ThreadTestManager:主线程等待 end
            }
        });
    }

    private ComponentItem testSync() {

        return new ComponentItem("测试同步代码块存取", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 20; i++) {
                    int finalI = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            testWrite(finalI);
                        }
                    }, "写入线程：" + i).start();
                }


                for (int i = 0; i <20; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            testRead();
                        }
                    }, "读取线程----------->>>：" + i).start();
                }

            }
        });
    }

    private int nowIndex = -1;

    private void testRead() {
        synchronized (ThreadTestManager.class) {
            log("读取到的索引：" + nowIndex);
        }
    }

    private void testWrite(int i) {
        synchronized (ThreadTestManager.class) {
            nowIndex = i;
            log("写入的索引：" + nowIndex);
        }
    }


    private ComponentItem testLocal() {
        return new ComponentItem("fql 线程池执行顺序测试", "结论：是乱序执行的", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 100; i++) {
                    int finalI = i;
                    ThreadPoolUtils.execute(new Runnable() {
                        @Override
                        public void run() {
                            //确实是乱序执行的，这里是个bug
                            log("编号：" + finalI);
//                            2019-09-19 11:37:33.231 31846-32757/com.canzhang.sample E/Test: ThreadTestManager:编号：11
//                            2019-09-19 11:37:33.231 31846-32757/com.canzhang.sample E/Test: ThreadTestManager:编号：8
//                            2019-09-19 11:37:33.231 31846-32758/com.canzhang.sample E/Test: ThreadTestManager:编号：9
//                            2019-09-19 11:37:33.231 31846-32756/com.canzhang.sample E/Test: ThreadTestManager:编号：10
//                            2019-09-19 11:37:33.231 31846-32757/com.canzhang.sample E/Test: ThreadTestManager:编号：7
//                            2019-09-19 11:37:33.231 31846-32760/com.canzhang.sample E/Test: ThreadTestManager:编号：6
                        }
                    });
                }

            }
        });
    }

    private ComponentItem testFqlThreadPool() {
        return new ComponentItem("测试多线程中的局部变量", "局部变量是在堆栈中运行。每个运行的线程都有自己的堆栈。\n" +
                "别的线程无法访问得到，因此我们说，局部变量是“安全”的。\n" +
                "全局变量在堆中，堆是对所有的线程都可见的。\n" +
                "因此在两个以上的线程访问全局变量时，就会出现所谓的\n" +
                "“不安全”，a线程访问全局变量，赋值为a，然后中间睡眠了0.001秒，在此期间b进程访问了全局变量，赋值为b了，此时a线程醒来了，抢了处理机，发现全局变量是b，显然不是我们a线程所要到的值，这时就要加入同步机制或者定义为局部变量，比如如果是方法的话就加同步方法，代码块就加同步代码块。", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        testLocalVariable(10);
                    }
                }, "线程----1").start();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        testLocalVariable(5);
                    }
                }, "线程2").start();

            }
        });
    }

    private void testLocalVariable(int num) {
        for (int i = 0; i < num; i++) {
            log("当前线程：" + Thread.currentThread().getName() + " i:" + i);
        }
        int s = 3;
        for (int i = 0; i < s; i++) {
            log("当前线程：" + Thread.currentThread().getName() + " s------------->>>:" + i);
        }
    }


    private ComponentItem fqlReport() {
        return new ComponentItem("fql report 点击上报", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppEvent.report("eventId", "label", null, true, false);
            }
        });
    }


    private ComponentItem deadLock() {

        //对多条操作共享数据的语句，只能让一个线程都执行完再执行过程中其他线程不可以参与运行
        return new ComponentItem("死锁",
                "死锁发生的必要条件\n" +
                        "1、互斥条件：资源是独占的且排他使用，进程互斥使用资源，即任意时刻一个资源只能给一个进程使用，其他进程若申请一个资源，而该资源被另一进程占有时，则申请者等待直到资源被占有者释放。\n" +
                        "2、请求和保持条件：指进程已经保持至少一个资源，但又提出了新的资源请求，而该资源已被其它进程占有，此时请求进程阻塞，但又对自己已获得的其它资源保持不放。\n" +
                        "3、不剥夺条件：指进程已获得的资源，在未使用完之前，不能被剥夺，只能在使用完时由自己释放。\n" +
                        "4、环路等待条件：指在发生死锁时，必然存在一个进程——资源的环形链，即进程集合{P0，P1，P2，···，Pn}中的P0正在等待一个P1占用的资源；P1正在等待P2占用的资源，……，Pn正在等待已被P0占用的资源。"
                , new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Object left = new Object();
                final Object right = new Object();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        log("线程开始");

                        synchronized (left) {
                            log("得到left 锁");

                            try {
                                log("休息三秒");
                                Thread.sleep(3000);//人为制造等待，不然死锁是很难出现的
                                log("休息完毕，继续执行");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            synchronized (right) {
                                log("得到right锁  结束线程");
                            }
                        }

                    }
                }).start();


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        log("线程开始");
                        synchronized (right) {
                            log("得到right 锁");
                            synchronized (left) {
                                log("得到left锁  结束线程");
                            }

                        }

                    }
                }).start();


/**
 * 可以看出两个线程都没有没有结束线程，线程0持有left锁，在等待right锁的释放继续执行下一步，线程1则持有right锁，在等待left锁的释放继续执行，双方都在等待对方让步，陷入了死锁
 *
 * 这个例子可以通过固定锁的获取顺序来解决，比如都是先left后 right
 */

//        当前线程：Thread-0 携带数据：线程开始
//        当前线程：Thread-0 携带数据：得到left 锁
//        当前线程：Thread-0 携带数据：休息三秒
//        当前线程：Thread-1 携带数据：线程开始
//        当前线程：Thread-1 携带数据：得到right 锁
//        当前线程：Thread-0 携带数据：休息完毕，继续执行

            }
        });
    }


    private ComponentItem waitTest03() {

        final Object o = new Object();

        //对多条操作共享数据的语句，只能让一个线程都执行完再执行过程中其他线程不可以参与运行
        return new ComponentItem("notify 和 notifyAll 的区别",
                "可以用 notify 和 notifyAll 来通知那些等待中的线程重新开始运行。不同之处在于，notify 仅仅通知一个线程，并且我们不知道哪个线程会收到通知，然而 notifyAll 会通知所有等待中的线程。换言之，如果只有一个线程在等待一个信号灯，notify和notifyAll都会通知到这个线程。但如果多个线程在等待这个信号灯，那么notify只会通知到其中一个，而其它线程并不会收到任何通知，而notifyAll会唤醒所有等待中的线程。"
                , new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        synchronized (o) {
                            log("进入线程");

                            try {
                                Thread.sleep(500);
                                log("休息0.5秒");
                                log("wait 前");
                                o.wait();
                                log("wait 后");

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            log("执行结束");
                        }

                    }
                }).start();

                try {
                    log("------------------>>>>>>主线程休息1秒,让三个线程顺序执行");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        synchronized (o) {
                            log("进入线程");

                            try {
                                Thread.sleep(500);
                                log("休息0.5秒");
                                log("wait 前");
                                o.wait();
                                log("wait 后");

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            log("执行结束");
                        }

                    }
                }).start();

                try {
                    log("------------------>>>>>>主线程休息1秒,让三个线程顺序执行");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (o) {

                            log("进入线程");

                            try {
                                Thread.sleep(500);
                                log("休息0.5秒");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            log("执行结束");
                            log("调用notify 前");
                            o.notify();
                            log("调用notify 后");

                        }

                    }
                }).start();

                /**
                 * 调用notify的情况（只是唤醒了一个线程）
                 * 三个线程如果在最下面那个线程只是调用notify的话，只有线程0继续执行完毕了，而线程2则一直停在了wait那一步。
                 */
//        当前线程：Thread-0 携带数据：进入线程
//        当前线程：main 携带数据：------------------>>>>>>主线程休息1秒,让三个线程顺序执行
//        当前线程：Thread-0 携带数据：休息0.5秒
//        当前线程：Thread-0 携带数据：wait 前
//        当前线程：main 携带数据：------------------>>>>>>主线程休息1秒,让三个线程顺序执行
//        当前线程：Thread-1 携带数据：进入线程
//        当前线程：Thread-1 携带数据：休息0.5秒
//        当前线程：Thread-1 携带数据：wait 前
//        当前线程：Thread-2 携带数据：进入线程
//        当前线程：Thread-2 携带数据：休息0.5秒
//        当前线程：Thread-2 携带数据：执行结束
//        当前线程：Thread-2 携带数据：调用notify 前
//        当前线程：Thread-2 携带数据：调用notify 后
//        当前线程：Thread-0 携带数据：wait 后
//        当前线程：Thread-0 携带数据：执行结束


                /**
                 * 调用notifyAll（唤醒了所有的线程）
                 */


//        当前线程：Thread-0 携带数据：进入线程
//        当前线程：main 携带数据：------------------>>>>>>主线程休息1秒,让三个线程顺序执行
//        当前线程：Thread-0 携带数据：休息0.5秒
//        当前线程：Thread-0 携带数据：wait 前
//        当前线程：main 携带数据：------------------>>>>>>主线程休息1秒,让三个线程顺序执行
//        当前线程：Thread-1 携带数据：进入线程
//        当前线程：Thread-1 携带数据：休息0.5秒
//        当前线程：Thread-1 携带数据：wait 前
//        当前线程：Thread-2 携带数据：进入线程
//        当前线程：Thread-2 携带数据：休息0.5秒
//        当前线程：Thread-2 携带数据：执行结束
//        当前线程：Thread-2 携带数据：调用notify 前
//        当前线程：Thread-2 携带数据：调用notify 后
//        当前线程：Thread-1 携带数据：wait 后
//        当前线程：Thread-1 携带数据：执行结束
//        当前线程：Thread-0 携带数据：wait 后
//        当前线程：Thread-0 携带数据：执行结束
//        Process finished with exit code 0    所有线程执行完毕，都有进程结束符提示了，上面那个则没有。

            }
        });

    }

    private ComponentItem waitTest02() {

        final Object o = new Object();

        //对多条操作共享数据的语句，只能让一个线程都执行完再执行过程中其他线程不可以参与运行
        return new ComponentItem("wait释放锁测试",
                "1、调用wait()方法，当前线程会放弃对象锁，进入等待此对象的等待锁定池，wait后面的语句也不会继续执行了，直到调用notify的时候，才会继续向下执行"
                , new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        synchronized (o) {
                            log("1-----》》》进入线程");

                            try {
                                Thread.sleep(3000);
                                log("1-----》》》休息三秒");
                                log("1-----》》》wait 前");
                                o.wait();
                                log("1-----》》》wait 后");

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            log("1-----》》》执行结束");
                        }

                    }
                }).start();


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (o) {

                            log("2-----》》》进入线程");

                            try {
                                log("2-----》》》休息三秒");
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            log("2-----》》》执行结束");
                            log("2-----》》》调用notify 前");
                            o.notify();
                            log("2-----》》》调用notify 后");

                        }

                    }
                }).start();


//                当前线程：Thread-0 携带数据：1-----》》》进入线程
//                当前线程：Thread-0 携带数据：1-----》》》休息三秒
//                当前线程：Thread-0 携带数据：1-----》》》wait 前
//                当前线程：Thread-1 携带数据：2-----》》》进入线程
//                当前线程：Thread-1 携带数据：2-----》》》休息三秒
//                当前线程：Thread-1 携带数据：2-----》》》执行结束
//                当前线程：Thread-1 携带数据：2-----》》》调用notify 前
//                当前线程：Thread-1 携带数据：2-----》》》调用notify 后
//                当前线程：Thread-0 携带数据：1-----》》》wait 后
//                当前线程：Thread-0 携带数据：1-----》》》执行结束

            }
        });

    }


    private ComponentItem sleepTest01() {

        final Object o = new Object();

        //对多条操作共享数据的语句，只能让一个线程都执行完再执行过程中其他线程不可以参与运行
        return new ComponentItem("sleep 不会释放锁 测试", "sleep的时候也不会释放锁，就不给你，我等睡完继续使用，必须等我用完才能给你"
                , new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        synchronized (o) {
                            log("1-----》》》进入线程");

                            try {
                                Thread.sleep(3000);
                                log("1-----》》》休息三秒");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            log("1-----》》》执行结束");
                        }

                    }
                }).start();


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (o) {

                            log("2-----》》》进入线程");

                            try {
                                log("2-----》》》休息三秒");
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            log("2-----》》》执行结束");

                        }

                    }
                }).start();

//                当前线程：Thread-0 携带数据：1-----》》》进入线程
//                当前线程：Thread-0 携带数据：1-----》》》休息三秒
//                当前线程：Thread-0 携带数据：1-----》》》执行结束
//                当前线程：Thread-1 携带数据：2-----》》》进入线程
//                当前线程：Thread-1 携带数据：2-----》》》休息三秒
//                当前线程：Thread-1 携带数据：2-----》》》执行结束

            }
        });
    }


    private ComponentItem waitTest01() {

        //对多条操作共享数据的语句，只能让一个线程都执行完再执行过程中其他线程不可以参与运行
        return new ComponentItem("wait，notify和notifyAll不在同步控制方法或者同步控制块里面使用测试",
                "1、wait，notify和notifyAll只能在同步控制方法或者同步控制块里面使用(否则会抛异常 IllegalMonitorStateException)\n"
                , new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object o = new Object();

                try {
                    o.wait();
                } catch (Exception e) {//
                    log("捕获异常:" + e.getMessage());
                    e.printStackTrace();
                }

                o.notify();
                o.notifyAll();

                //实验表明这三个方法，不在同步代码块或者同步方法里面调用任何一个都会抛出异常，具体可参见：https://blog.csdn.net/wangshuang1631/article/details/53815519
            }
        });
    }


    private ComponentItem sleepAndWait() {

        //对多条操作共享数据的语句，只能让一个线程都执行完再执行过程中其他线程不可以参与运行
        return new ComponentItem("sleep 和 wait",
                "1、sleep来自Thread，wait是object的方法\n" +
                        "2、最主要是sleep方法没有释放锁，而wait方法释放了锁（你们先拿去用吧），使得其他线程可以使用同步控制块或者方法\n" +
                        "3、wait，notify和notifyAll只能在同步控制方法或者同步控制块里面使用(否则会抛异常 IllegalMonitorStateException)，而sleep可以在任何地方使用\n" +
                        "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    private ComponentItem syncMethodThreadTest() {

        //对多条操作共享数据的语句，只能让一个线程都执行完再执行过程中其他线程不可以参与运行
        return new ComponentItem("多线程存钱（同步方法 正常）",
                "1、非静态同步方法的锁是this，不同对象持有的锁自然也是不同的\n" +
                        "2、静态同步方法的锁是类对象本身，所以和非静态同步方法的锁是不同的，没有竞态关系\n" +
                        "3、控制域是整个方法（注意不要放非同步的内容进去导致效率的降低）", new View.OnClickListener() {
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
                        "一定要理解清楚，这里的锁指的是实际对象，而不是其引用\n" +
                        "3、java中每个对象都有一个内置锁,并且每个对象只有一个锁，如果被其他线程占用，别的线程就进入不了，将会进入阻塞状态，该线程进入锁对象的一种池子中等待\n" +
                        "4、只能同步方法，而不能同步变量和类\n" +
                        "5、线程sleep时候持有的锁不会释放\n" +
                        "", new View.OnClickListener() {
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

        return new ComponentItem("多线程卖票（异常）", "如果不加锁，多个线程同时操作一个共享数据，就会出错", new View.OnClickListener() {
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
                "1、此方式创建线程可以避免多继承问题", new View.OnClickListener() {
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
