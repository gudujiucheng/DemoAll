package com.canzhang.sample.manager.life;

import android.os.Bundle;
import android.widget.TextView;

import com.canzhang.sample.R;
import com.example.base.base.BaseActivity;
import com.trello.rxlifecycle4.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 参考：https://www.jianshu.com/p/e75d320a668c
 */
public class LifeTestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_life_test);
        TextView tvTest = findViewById(R.id.tv_life_test);

        Observable.interval(1, TimeUnit.SECONDS)//计时，每秒钟回调一次
                .subscribeOn(Schedulers.io())//指定被观察者线程
                .observeOn(AndroidSchedulers.mainThread())//指定观察者线程
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Throwable {
                        log("Life_test:--------------->>>>>>>Unsubscribing subscription from onPause() thread name:" + Thread.currentThread().getName());

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
}