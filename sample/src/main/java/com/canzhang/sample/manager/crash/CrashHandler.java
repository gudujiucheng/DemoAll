package com.canzhang.sample.manager.crash;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.canzhang.sample.R;
import com.example.base.base.AppProxy;
import com.example.base.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @Description: 异常崩溃捕获
 * @Author: canzhang
 * @CreateDate: 2019/9/23 18:35
 */
public class CrashHandler implements UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    //存储根目录
    public static final String CRASH_LOG_PATH = AppProxy.getInstance().getExternalCacheDir() + "/canzhang/log/";
    public static final String CRASH_LOG_ZIP_FILE_PATH = AppProxy.getInstance().getExternalCacheDir() + "/canzhang/crash_log.zip";
    // CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    // 是否已经初始化
    private static boolean isInit = false;
    // 系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultHandler;
    // 程序的Context对象
    private Context mContext;
    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<>();
    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private String nameString;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     */
    public void init(final Context context) {
        mContext = context;
        // Android M will check permission dynamically, avoid crush here.
        if (!isInit) {
            // 获取系统默认的UncaughtException处理器
            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            // 设置该CrashHandler为程序的默认处理器
            Thread.setDefaultUncaughtExceptionHandler(CrashHandler.this);
            isInit = true;
            nameString = context.getString(R.string.app_name);
            ToastUtil.toastShort("初始化成功");
        } else {
            ToastUtil.toastShort("已初始化");
        }
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e(TAG, "捕获到异常: thread:" + thread.getName() + " " + ex.getMessage());
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件
        /* String fileName = */
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("版本名称", versionName);
                infos.put("版本code", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        // Log.d(WModel.CrashUpload, result);
        sb.append(result);
        try {
            Log.d(TAG, "msg:" + result);
            // long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = nameString + "-" + time + ".txt";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(CRASH_LOG_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(CRASH_LOG_PATH + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
                onClearFile();
            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occurred while writing file...", e);
        }
        return null;
    }

    /**
     * 文件过多的话则做清理工作
     */
    private void onClearFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(CRASH_LOG_PATH);
            if (file.exists()) {
                if (file.listFiles().length > 3) {
                    Log.e(TAG, "文件过多（>3）清理文件");
                    int index = 0;
                    for (File temp : file.listFiles()) {
                        long time = System.currentTimeMillis() - temp.lastModified();
                        if (time / 1000 > 3600 * 24 * 3) {//非三天以内的删除
                            Log.e(TAG, "文件最后修改时间>3天，删除");
                            temp.delete();
                            index++;
                        }
                        if (index >= file.listFiles().length - 3) break;
                    }
                }
            }
        }
    }
}

