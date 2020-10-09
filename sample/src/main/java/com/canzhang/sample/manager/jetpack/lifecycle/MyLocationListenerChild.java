package com.canzhang.sample.manager.jetpack.lifecycle;

import android.content.Context;


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