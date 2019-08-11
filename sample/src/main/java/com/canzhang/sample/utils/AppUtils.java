package com.canzhang.sample.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

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

}
