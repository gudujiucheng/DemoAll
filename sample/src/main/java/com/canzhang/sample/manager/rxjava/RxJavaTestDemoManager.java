package com.canzhang.sample.manager.rxjava;

import android.app.Activity;
import android.os.Handler;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.example.simple_test_annotations.MarkManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * rxjava  实际应用测试
 */
@MarkManager(value = "rxJava实际应用")
public class RxJavaTestDemoManager extends BaseManager {


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        List<ComponentItem> list = new ArrayList<>();
        list.add(merge());
        list.add(flatMap());
        return list;
    }


    private String result="";

    /**
     * https://www.jianshu.com/p/fc2e551b907c
     * @return
     */
    private ComponentItem merge() {

        return new ComponentItem("合并两个接口请求", new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //被观察者

                Observable<String> observable01 = Observable.create(new ObservableOnSubscribe<String>() {

                    @Override

                    public void subscribe(final ObservableEmitter<String> emitter) throws Exception {

                        getFromNet01(new CallBack() {
                            @Override
                            public void onSuccess(String result) {
                                emitter.onNext(result);
                                emitter.onComplete();
                            }

                            @Override
                            public void onFail(String msg) {
                                emitter.onError(new Exception(msg));
                            }
                        });


                    }

                });


                Observable<String> observable02 = Observable.create(new ObservableOnSubscribe<String>() {

                    @Override

                    public void subscribe(final ObservableEmitter<String> emitter) throws Exception {

                        getFromNet02(new CallBack() {
                            @Override
                            public void onSuccess(String result) {
                                emitter.onNext(result);
                                emitter.onComplete();//必须调用
                            }

                            @Override
                            public void onFail(String msg) {
                                emitter.onError(new Exception(msg));
                            }
                        });


                    }

                });

                /*
                 * 通过merge（）合并事件 & 同时发送事件
                 **/
                Observable.merge(observable01, observable02)
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(String value) {//这里还要考虑先后顺序，如何组织。另外异常咱么处理
                                log("数据源有： " + value);
                                result += value + "+";
                            }

                            @Override
                            public void onError(Throwable e) {
                                log("对Error事件作出响应");
                            }

                            // 接收合并事件后，统一展示
                            @Override
                            public void onComplete() {
                                log("获取数据完成" + result);
                            }
                        });


            }
        });
    }


    /**
     * https://www.jianshu.com/p/5f5d61f04f96
     *
     * @return
     */
    private ComponentItem flatMap() {

        return new ComponentItem("串行两个接口请求", new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //被观察者

                Observable<String> observable01 = Observable.create(new ObservableOnSubscribe<String>() {

                    @Override

                    public void subscribe(final ObservableEmitter<String> emitter) throws Exception {

                        getFromNet01(new CallBack() {
                            @Override
                            public void onSuccess(String result) {
                                emitter.onNext(result);
                                emitter.onComplete();
                            }

                            @Override
                            public void onFail(String msg) {
                                emitter.onError(new Exception(msg));
                            }
                        });


                    }

                });


                final Observable<String> observable02 = Observable.create(new ObservableOnSubscribe<String>() {

                    @Override

                    public void subscribe(final ObservableEmitter<String> emitter) throws Exception {

                        getFromNet02(new CallBack() {
                            @Override
                            public void onSuccess(String result) {
                                emitter.onNext(result);
                                emitter.onComplete();//必须调用
                            }

                            @Override
                            public void onFail(String msg) {
                                emitter.onError(new Exception(msg));
                            }
                        });


                    }

                });


                Disposable subscribe = observable01.doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        log("第一次网络请求成功：" + s);

                    }
                }).flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        return observable02;
                    }
                }).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        log("第二次网络请求成功：" + s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });


            }
        });
    }



    private void getFromNet01(final CallBack callBack) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess("成功01");

            }
        }, 4000);
    }

    private void getFromNet02(final CallBack callBack) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess("成功02");

            }
        }, 3000);

    }


    interface CallBack {
        void onSuccess(String result);

        void onFail(String msg);
    }


}
