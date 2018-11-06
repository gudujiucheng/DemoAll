package com.example.administrator.demoall.rxjava;

import android.annotation.SuppressLint;
import android.nfc.Tag;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.example.administrator.demoall.MainActivity.TAG;

/**
 * 参考文章：https://www.jianshu.com/p/e19f8ed863b1
 */
public class RxjavaTestManager {

    private RxjavaTestManager() {

    }

    public static class Holder {
        private static RxjavaTestManager instance = new RxjavaTestManager();
    }

    public static RxjavaTestManager getInstance() {
        return Holder.instance;
    }


    public void test() {
        testFlatMap();
    }


    /**
     * 可以将被观察者发送的时间序列进行拆分，或者单独转换，合并成一个新的事件序列，最后在进行发送。
     *  重要：注：新合并生成的事件序列顺序可能是无序的，即 与旧序列发送事件的顺序无关
     *
     * 原理：
     * 1、为事件序列中每个事件都创建一个 Observable 对象；
     * 2、将对每个 原始事件 转换后的 新事件 都放入到对应 Observable对象；
     * 3、将新建的每个Observable 都合并到一个 新建的、总的Observable 对象；
     * 4、新建的、总的Observable 对象 将 新合并的事件序列 发送给观察者（Observer）
     */
    @SuppressLint("CheckResult")
    private void testFlatMap() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                emitter.onNext(1);
                emitter.onNext(10);
                emitter.onNext(100);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                Log.d(TAG, "接收到的:" + integer);
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("\n我是事件 " + integer + "拆分后的子事件" + i);
                    // 通过flatMap中将被观察者生产的事件序列先进行拆分，再将每个事件转换为一个新的发送三个String事件
                    // 最终合并，再发送给被观察者
                }
                return Observable.fromIterable(list);

                //也可以一个个发出去，下面一个个接收
//                return Observable.just(integer+"");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "flatmap:" + s);
            }
        });
    }


    /**
     * 对 被观察者发送的每1个事件都通过 指定的函数 处理，从而变换成另外一种事件【即， 将被观察者发送的事件转换为任意的类型事件。】
     *
     * 实验场景：数据类型转换
     */

    @SuppressLint("CheckResult")
    private void testMap() {
        //事件对象处理
        Observable.just("100") // 发射数据
                .map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String s) {
                        return Integer.valueOf(s);//变换成int值，（）
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
//                        print("数字：" + integer);

                    }
                });
    }


    /**
     * 延迟指定时间后，发送1个数值0（Long类型）
     * 注意这个回调不是在调用线程，可以用三个参数的api切换线程
     */
    private void testTimer() {
        final Disposable subscribe = Observable.timer(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread()) 可以在这里切换线程，也可以在第三个参数切换线程
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //注意这里不是调用线程
                        Log.d(TAG, "接收到的整数是" + aLong + " Thread:" + Thread.currentThread().getName());

                    }
                });
    }


    /**
     * defer
     * 直到有观察者（Observer ）订阅时，才动态创建被观察者对象（Observable） & 发送事件
     * <p>
     * 观察者接收到的值会是15 因为只有当订阅关系发生的时候，才会创建被观察者对象，并发送事件。
     */
//        <-- 1. 第1次对i赋值 ->>
    Integer i = 10;

    public void testDefer() {


        // 2. 通过defer 定义被观察者对象
        // 注：此时被观察者对象还没创建
        Observable<Integer> observable = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                return Observable.just(i);
            }
        });

//        <-- 2. 第2次对i赋值 ->>
        i = 15;

//        <-- 3. 观察者开始订阅 ->>
        // 注：此时，才会调用defer（）创建被观察者对象（Observable）
        observable.subscribe(new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "开始采用subscribe连接");
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "接收到的整数是" + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "对Error事件作出响应");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }
        });

    }


    /**
     * 可发送10个以上参数
     * 若直接传递一个list集合进去，否则会直接把list当做一个数据元素发送
     */
    public void testFromArray() {

        /*
         * 数组遍历
         **/
        // 1. 设置需要传入的数组
        Integer[] items = {0, 1, 2, 3, 4};

        // 2. 创建被观察者对象（Observable）时传入数组
        // 在创建后就会将该数组转换成Observable & 发送该对象中的所有数据
        Observable.fromArray(items)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "数组遍历");
                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.e(TAG, "数组中的元素 = " + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "遍历结束");
                    }

                });

    }


    /**
     * 快速创建1个被观察者对象（Observable）
     * 发送事件的特点：直接发送 传入的事件
     * <p>
     * 注意：最多只能发送十个参数，内部是通过重载方法实现的
     */
    public void testJust() {

        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "开始采用subscribe连接");
            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "jieshou:" + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "错误");
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete");
            }
        });
    }


    Disposable mDisposable;//可用于取消订阅

    /**
     * 测试基本的rxjava 操作   观察者 Observer 被观察者Observable
     */
    public void testBase() {
        //被观察者
        Observable novel = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //被观察者  推送自己的小说连载
                //emitter 事件发射器
                emitter.onNext("连载1");
                emitter.onNext("连载2");
                emitter.onNext("连载3");
                emitter.onComplete();
                //接收的参数时一个异常对象
//                emitter.onError(new NullPointerException());
//                emitter.onError(new Exception());
            }
        });


        //观察者
        Observer<String> reader = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
                Log.e(TAG, "onSubscribe");
            }

            @Override
            public void onNext(String value) {
                if ("2".equals(value)) {
                    mDisposable.dispose();
                    return;
                }
                Log.e(TAG, "onNext:" + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError=" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete()");
            }
        };


        try {
            //建议订阅关系
            novel.subscribe(reader);//这里直接支持链式操作，不必单独一行。
        } catch (Exception e) {
            //在这里是捕获不到异常的
            Log.e(TAG, "捕获异常：" + e.toString());

        }


    }
}
