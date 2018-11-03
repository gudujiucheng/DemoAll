package com.example.administrator.demoall.rxjava;

import android.nfc.Tag;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

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
        testFromArray();
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
