package com.canzhang.sample.manager.life;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;


/**
 *google官方Lifecycle 生命周期管理组件测试
 * 参考文章 https://zhuanlan.zhihu.com/p/80681329
 */
public class MyLocationListener implements LifecycleObserver {//生命周期观察者需要实现LifeCycleObserver接口，这个接口没有任何方法。那他如何对生命周期进行观察呢？答案是依赖注解-->OnLifecycleEvent

    private Context context;

    public MyLocationListener(Context context) {
        this.context = context;
    }

    //可以自由选择都要实现哪几个生命周期方法
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        showLog("onCreate");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        showLog("onDestroy");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStart() {
        showLog("onStart");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop() {
        showLog("onStop");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume() {
        showLog("onResume");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        showLog("onPause");
    }

    private void showLog(String msg) {
        Log.e("===MyLocationListener", msg);
    }


}