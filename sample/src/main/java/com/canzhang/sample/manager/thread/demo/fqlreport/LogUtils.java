package com.canzhang.sample.manager.thread.demo.fqlreport;

import android.util.Log;

/**
 * @Description:
 * @Author: canzhang
 * @CreateDate: 2019/4/28 15:16
 */
public class LogUtils {
    private static final String APP_EVENT_TAG = "AppEvent";

    public static void log(String tips) {
        Log.e(APP_EVENT_TAG, "当前线程 :" + Thread.currentThread().getName() + " tips：" + tips);
    }
}
