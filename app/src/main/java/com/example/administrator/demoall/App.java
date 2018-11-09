package com.example.administrator.demoall;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.administrator.demoall.weex.ImageAdapter;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;

public class App extends Application {
    ActivityLifecycleCallbacks callbacks;

    public  Context getAppContext(){
        return  getApplicationContext();
    }
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化weex
        InitConfig config=new InitConfig.Builder().setImgAdapter(new ImageAdapter()).build();
        WXSDKEngine.initialize(this,config);

        //注册对app内所有activity 的生命周期监听
        registerActivityLifecycleCallbacks(callbacks = new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.e("Test","onActivityCreated:"+activity.getLocalClassName());
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.e("Test","onActivityStarted:"+activity.getLocalClassName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.e("Test","onActivityResumed:"+activity.getLocalClassName());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.e("Test","onActivityPaused:"+activity.getLocalClassName());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.e("Test","onActivityStopped:"+activity.getLocalClassName());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.e("Test","onActivitySaveInstanceState:"+activity.getLocalClassName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.e("Test","onActivityDestroyed:"+activity.getLocalClassName());
            }
        });
//        注销app内所有activity的生命周期监听
//        unregisterActivityLifecycleCallbacks(callbacks);
    }


}
