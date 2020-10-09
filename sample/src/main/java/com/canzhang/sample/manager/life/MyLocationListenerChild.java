package com.canzhang.sample.manager.life;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;


/**
 * 测试子类是否可以正常调用(可以正常调用)
 */
public class MyLocationListenerChild extends MyLocationListener {
    public MyLocationListenerChild(Context context) {
        super(context);
    }

    @Override
    void onCreate() {
        super.onCreate();
        showLog("xxxxxxxxxxx:onCreate");
    }

    @Override
    void onDestroy() {
        super.onDestroy();
        showLog("xxxxxxxxxxx:onDestroy");
    }
}