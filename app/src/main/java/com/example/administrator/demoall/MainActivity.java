package com.example.administrator.demoall;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "Test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,ChannelActivity.class));

//                test();
                testChangeThread();
            }
        });

    }


    /**
     * 测试错误事件的一个流向
     */
    @SuppressLint("CheckResult")
    private static void test() {
        print("开始————————》》》》");
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) {
//                emitter.onNext("事件1");
//                emitter.onNext("事件2");
                emitter.onNext(null);
                emitter.onComplete();//调用了onComplete 之后 后面的事件就收不到了。
//                emitter.onNext("事件4");
            }
        }, BackpressureStrategy.LATEST)
//                .observeOn(Schedulers.io())
//                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        print("doOnNext：" + s);
                    }
                }).onErrorReturnItem("出现错误").flatMap(new Function<String, Publisher<List<String>>>() {
            @Override
            public Publisher<List<String>> apply(String s) throws Exception {
                final List<String> list = new ArrayList<>();
                list.add(s);
                return Flowable.create(new FlowableOnSubscribe<List<String>>() {
                    @Override
                    public void subscribe(FlowableEmitter<List<String>> emitter) throws Exception {
                        emitter.onNext(list);
                    }
                }, BackpressureStrategy.LATEST);
            }
        }).subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> strings) throws Exception {
                for (int i = 0; i < strings.size(); i++) {
                    print("xxxx:" + strings.get(i));
                }

            }
        });

    }


    private static void print(String s) {
        Log.e("Test", "Thread:" + Thread.currentThread().getName() + "  " + s + "\n");
        System.out.print("Thread:" + Thread.currentThread().getName() + "  " + s + "\n");
    }




    @SuppressLint("CheckResult")
    private void testChangeThread() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                print("发送事件");
                emitter.onNext(1);
            }
        });

        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                print("onSubscribe");
            }

            @Override
            public void onNext(Integer value) {
                print("onNext");
            }

            @Override
            public void onError(Throwable e) {
                print("onError");
            }

            @Override
            public void onComplete() {
                print("onComplete");
            }
        };

        observable.subscribeOn(Schedulers.newThread())//指定上游，也就是发送事件的一方（此操作符只会生效一次）
                .subscribeOn(Schedulers.io())//不生效
                .observeOn(AndroidSchedulers.mainThread())//指定下游，接收事件的一方（此操作符可多次生效）
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        print("doOnNext："+ integer);
                    }
                })
                .observeOn(Schedulers.computation())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        print("doOnNextX："+ integer);
                    }
                })
                .observeOn(Schedulers.single())
                .subscribe(observer);
    }

    }

