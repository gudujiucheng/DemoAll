package com.example.administrator.demoall;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.administrator.demoall.weex.WeexTestActivity;

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
                startActivity(new Intent(MainActivity.this,WeexTestActivity.class));
//              TestActivity.startTestActivity(MainActivity.this);
            }
        });

    }


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
                        print("数字：" + integer);

                    }
                });
    }

    @SuppressLint("CheckResult")
    private void testFlatMap() {
//        Observable.just("xxxx").flatMap(new Function<String, ObservableSource<String>>() {
//            @Override
//            public ObservableSource<String> apply(String s) throws Exception {
//                return Observable.just("66666");
//            }
//        }).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                print(s);
//            }
//        });
        print("sssssssss");

        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        Flowable.fromArray(list).flatMap(new Function<List<String>, Publisher<String>>() {
            @Override
            public Publisher<String> apply(List<String> strings) throws Exception {
                print("xxx:"+strings);
                return Flowable.fromIterable(strings);
            }
        }).map(new Function<String, String>() {
            @Override
            public String apply(String s) {
                print("x:"+s);
                return s;
            }
        });


//        Observable.just("xxxxx").map(new Function<String, String>() {
//            @Override
//            public String apply(String s) throws Exception {
//                return "map66666";
//            }
//        }).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                print(s);
//            }
//        });


    }


    @SuppressLint("CheckResult")
    private void test() {
        testoncatWith().subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                print("回调：" + s);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                print("异常----》》》》》");
            }
        });
    }


    /**
     * 测试操作符
     */
    private static Flowable<String> testoncatWith() {
        //缓存和网络串接起来
        return getFromCache()
                .concatWith(getFromNet())
                .distinct();//去重复操作符（ 默认是通过这两个方法实现的 {@code Object.equals()} and {@link Object#hashCode()}）

    }

    private static Flowable<String> getFromCache() {
        return Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) {

                String t = "我来自缓存";
                if (t != null)//传入空值会直接抛异常，这里可以考虑用包装类进行二次包装
                    emitter.onNext(t);
                emitter.onComplete();//事件完成

            }
        }, BackpressureStrategy.LATEST).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private static Flowable<String> getFromNet() {

        return Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) {

                String t = "我来自网络";
                if (t != null)
                    emitter.onNext(t);
                emitter.onComplete();

            }
        }, BackpressureStrategy.LATEST).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 测试错误事件的一个流向
     */
    @SuppressLint("CheckResult")
    private static void testListError() {
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
                        print("doOnNext：" + integer);
                    }
                })
                .observeOn(Schedulers.computation())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        print("doOnNextX：" + integer);
                    }
                })
                .observeOn(Schedulers.single())
                .subscribe(observer);
    }

    private static void print(String s) {
        Log.e("Test", "Thread:" + Thread.currentThread().getName() + "  " + s + "\n");
        System.out.print("Thread:" + Thread.currentThread().getName() + "  " + s + "\n");
    }

}

