package com.canzhang.sample.manager.appstatus;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.LinkedList;
import java.util.List;

/**
 * App前后台状态监听
 * 参考：https://blog.csdn.net/u011386173/article/details/79095757
 */

public class AppStatus {

    private static AppStatus sInstance;
    private static boolean isFirstRun = true;
    private static int sActivityStartedCount;
    private final List<AppStatusChangeListener> mChangeListenerList = new LinkedList<>();

    public static AppStatus getInstance() {
        if (sInstance == null) {
            synchronized (AppStatus.class) {
                if (sInstance == null) {
                    sInstance = new AppStatus();
                }
            }
        }
        return sInstance;
    }


    public static int activityStartedCount() {
        return sActivityStartedCount;
    }

    public void setActivityLifeCallBack(Application app) {
        if (app == null) {
            return;
        }
        app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                sActivityStartedCount++;
                final boolean isFirstRunFinal = isFirstRun;
                if (isFirstRun) {
                    isFirstRun = false;
                }
                if (sActivityStartedCount == 1) {
                    for (AppStatusChangeListener listener : mChangeListenerList) {
                        if (listener == null) {
                            return;
                        }
                        if (isFirstRunFinal) {
                            listener.onAppFirstCreate();
                        } else {
                            listener.onAppToFront();
                        }
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
                //如果mFinalCount ==0，说明是前台到后台
                if (sActivityStartedCount == 0) {
                    for (AppStatusChangeListener listener : mChangeListenerList) {
                        if (listener != null) {
                            listener.onAppToBackground();
                        }
                    }
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


    public void addChangeListener(AppStatusChangeListener listener) {
        if (listener == null) {
            return;
        }
        mChangeListenerList.add(listener);
    }

    public void removeChangeListener(AppStatusChangeListener listener) {
        if (listener == null) {
            return;
        }
        mChangeListenerList.remove(listener);
    }


}
