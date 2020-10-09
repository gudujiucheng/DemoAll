package com.canzhang.sample.manager.lifetest;


import android.annotation.SuppressLint;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * 模拟耗时操作 不绑定生命周期
 */
public class MsgStatus {

    private static MsgStatus instance;


    public static MsgStatus getInstance() {
        if (instance == null) {
            synchronized (MsgStatus.class) {
                if (instance == null) {
                    instance = new MsgStatus();
                }
            }
        }
        return instance;
    }

    private MsgStatus() {

    }

    private List<OnMsgStatusChangedListener> onMsgStatusChangedList = new LinkedList<>();


    public void addListener(OnMsgStatusChangedListener onMsgStatusChangedListener) {
        if (onMsgStatusChangedListener == null) {
            return;
        }
        onMsgStatusChangedList.add(onMsgStatusChangedListener);
    }

    public void removeListener(OnMsgStatusChangedListener onMsgStatusListener) {
        if (onMsgStatusListener == null) {
            return;
        }
        onMsgStatusChangedList.remove(onMsgStatusListener);
    }

    public void notifyMsgStatusChanged() {
        if (onMsgStatusChangedList != null) {
            for (OnMsgStatusChangedListener listener : onMsgStatusChangedList) {
                if (listener != null) {
                    listener.onMsgStatusChanged("哈哈哈哈哈哈逗比");
                }
            }
        }
    }


    @SuppressLint("CheckResult")
    public void getStatusFromNet() {
        Observable.timer(10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //注意这里不是调用线程
                        Log.e("Test","回调："+Thread.currentThread().getName());
                        notifyMsgStatusChanged();
                    }
                });
    }


}
