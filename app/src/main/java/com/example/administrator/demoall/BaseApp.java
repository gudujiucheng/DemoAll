package com.example.administrator.demoall;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.administrator.demoall.MMKV.MMKVActivity;
import com.example.administrator.demoall.weex.ImageAdapter;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.tencent.mmkv.MMKV;

import java.io.File;

import static com.example.administrator.demoall.MainActivity.TAG;

public class BaseApp extends Application {
    ActivityLifecycleCallbacks callbacks;
    public static  Context mContext;

    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext =getAppContext();
        int pid = android.os.Process.myPid();
        Log.i(TAG, "MyApplication is onCreate===="+"pid="+pid);
        initMMKV();

        //初始化weex
        InitConfig config = new InitConfig.Builder().setImgAdapter(new ImageAdapter()).build();
        WXSDKEngine.initialize(this, config);

        //注册对app内所有activity 的生命周期监听
        registerActivityLifecycleCallbacks(callbacks = new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.e("Test", "onActivityCreated:" + activity.getLocalClassName());
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.e("Test", "onActivityStarted:" + activity.getLocalClassName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.e("Test", "onActivityResumed:" + activity.getLocalClassName());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.e("Test", "onActivityPaused:" + activity.getLocalClassName());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.e("Test", "onActivityStopped:" + activity.getLocalClassName());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.e("Test", "onActivitySaveInstanceState:" + activity.getLocalClassName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.e("Test", "onActivityDestroyed:" + activity.getLocalClassName());
            }
        });
//        注销app内所有activity的生命周期监听
//        unregisterActivityLifecycleCallbacks(callbacks);
    }

    private void initMMKV() {
        String rootDir = MMKV.initialize(this);
        Log.e(MMKVActivity.MMKV_TAG, "mmkv root: " + rootDir);
    }






}
