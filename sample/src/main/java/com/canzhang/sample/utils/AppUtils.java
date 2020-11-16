package com.canzhang.sample.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.util.List;

public class AppUtils {

    /**
     * 等价于用户选择从设备设置UI中清除应用程序的数据。
     * 它删除与应用程序关联的所有动态数据
     * ——其私有数据和其内部数据外部存储上的私有区域
     * ——但不删除已安装的应用程序本身，或任何OBB文件。
     *
     * @return {@code true}如果应用程序成功请求应用程序的删除数据;{@code false}或者失败。
     * /
     */
    public static boolean clearAppUserData(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                if (manager != null) {
                    boolean success = manager.clearApplicationUserData();
                    if (success) {
                        return true;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            String packageName = context.getPackageName();
            Process p = execRuntimeProcess("pm clear " + packageName);
            if (p == null) {
                Log.i("clearAppUserData", "packageName:" + packageName + ", FAILED !");
                return false;
            } else {
                Log.i("clearAppUserData", "packageName:" + packageName + ", SUCCESS !");
                return true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }


    public static Process execRuntimeProcess(String command) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(command);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        Log.i("execRuntime", "exec Runtime command:" + command + ", Process:" + p);
        return p;
    }

    /**
     * 判断程序是否在后台运行
     *
     * 这种方法不适合判断推送唤起app场景，因为这里仅判断了进程，而一般我们认为的启动是 主页已经打开  或者唤起了登陆页面
     */
    public static boolean isRunBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)) {
                logInfo(appProcess);
                // 表明程序在后台运行
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND;
            }
        }
        return false;
    }

    private static void logInfo(ActivityManager.RunningAppProcessInfo appProcess) {
        Log.e("CAN_TEST", "processName:" + appProcess.processName + " importance:" + appProcess.importance);
    }


    /**
     * 判断程序是否在前台运行（当前运行的程序）
     *
     * 这种方法不适合判断推送唤起app场景，因为这里仅判断了进程，而一般我们认为的启动是 主页已经打开  或者唤起了登陆页面
     */
    public static boolean isRunForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            logInfo(appProcess);
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;// 程序运行在前台
            }
        }
        return false;
    }
}
