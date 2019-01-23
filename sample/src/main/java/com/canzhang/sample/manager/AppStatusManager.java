package com.canzhang.sample.manager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.example.base.base.AppProxy;

import java.util.ArrayList;
import java.util.List;

/**
 * 判断当前app是否处于前台
 * 参考：https://blog.csdn.net/u011386173/article/details/79095757
 */
public class AppStatusManager extends BaseManager {
    private static int sActivityStartedCount;
    private static boolean isFirstRun = true;
    private static Application.ActivityLifecycleCallbacks callBack;


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {


        List<ComponentItem> list = new ArrayList<>();
        list.add(unRegister());
        return list;
    }


    public static void register(Application application) {
        if (application == null) {
            return;
        }

        application.registerActivityLifecycleCallbacks(callBack = new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                sActivityStartedCount++;
                if (sActivityStartedCount == 1) {
                    if (isFirstRun) {
                        Log.e("AppStatusManager", "初次启动");
                        Toast.makeText(activity, "初次启动", Toast.LENGTH_SHORT).show();
                        isFirstRun = false;
                    } else {
                        Log.e("AppStatusManager", "从后台回到前台");
                        Toast.makeText(activity, "从后台回到前台", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                sActivityStartedCount--;
                if (sActivityStartedCount == 0) {
                    Log.e("AppStatusManager", "app 后台");
                    Toast.makeText(activity, "app 后台", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });


    }

    /**
     * 初始化
     *
     * @return
     */
    private ComponentItem unRegister() {

        return new ComponentItem("取消监听", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppProxy.getInstance().getApplication().unregisterActivityLifecycleCallbacks(callBack);

            }
        });
    }


}
