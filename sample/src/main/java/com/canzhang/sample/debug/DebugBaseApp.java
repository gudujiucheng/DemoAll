package com.canzhang.sample.debug;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.lexinfintech.component.debugdialog.DebugDialog;


/**
 * 调试模式下的 app（simple作为单独工程运行时候使用）
 */
public class DebugBaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //调试
        DebugDialog.getInstance().init(this);
        DebugDialog.setIsDebug(true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);


        this.getApplicationContext();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.e("Test", activity.getClass().getSimpleName());

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }


}
