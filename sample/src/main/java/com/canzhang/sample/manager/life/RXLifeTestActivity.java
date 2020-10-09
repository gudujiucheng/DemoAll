package com.canzhang.sample.manager.life;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.canzhang.sample.R;
import com.example.base.base.BaseActivity;
import com.trello.rxlifecycle4.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 对应开源框架：https://github.com/trello/RxLifecycle
 * 参考：https://www.jianshu.com/p/e75d320a668c
 */
public class RXLifeTestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_life_test);
        TextView tvTest = findViewById(R.id.tv_life_test);

        time(tvTest);
        tvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testNormal();
                if (mTimeSubscribe != null) {
                    mTimeSubscribe.dispose();
                }
            }
        });


    }

    Disposable mTimeSubscribe;

    private void time(TextView tvTest) {
        mTimeSubscribe = Observable.interval(1, TimeUnit.SECONDS)//计时，每秒钟回调一次
                .subscribeOn(Schedulers.io())//指定被观察者线程
                .observeOn(AndroidSchedulers.mainThread())//指定观察者线程
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Throwable {
                        log("Life_test:--------------->>>>>>>取消观察 from DESTROY() thread name:" + Thread.currentThread().getName());

                    }
                })
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Throwable {
                        log("Life_test:--------------->>>>>>>回调 thread name:" + Thread.currentThread().getName());
                        tvTest.setText("倒计时：" + aLong);
                        log("Life_test:倒计时" + aLong);
                    }
                });
    }


    private void testNormal() {
        log("XLife_test:-------testNormal");
        //被观察者
        Observable.create(new ObservableOnSubscribe<String>() {

            @Override

            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                log("XLife_test:-------getFromNet");
                getFromNet(new CallBack() {
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

        }).subscribeOn(Schedulers.io())//指定被观察者线程
                .observeOn(AndroidSchedulers.mainThread())//指定观察者线程;
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Throwable {
                        log("XLife_test:--------------->>>>>>>取消观察 from DESTROY() thread name:" + Thread.currentThread().getName());

                    }
                })
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        log("XLife_test:--------------->>>>>>结果：" + s + " thread name:" + Thread.currentThread().getName());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        log("XLife_test:--------------->>>>>>异常结果：" + throwable.getMessage() + " thread name:" + Thread.currentThread().getName());
                    }
                });

    }

    private static int index = 0;

    private void getFromNet(final CallBack callBack) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (index % 2 == 0)
                    callBack.onSuccess("成功01");
                else
                    callBack.onFail("失败了 哈哈哈哈哈");

                index++;

                log("XLife_test:-------index:"+index);

            }
        }, 4000);
    }


    interface CallBack {
        void onSuccess(String result);

        void onFail(String msg);
    }
}