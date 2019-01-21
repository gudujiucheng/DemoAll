package com.example.administrator.demoall.filemanager;

import android.os.Environment;


import com.example.administrator.SafeLog;
import com.example.administrator.demoall.BaseApp;

import java.io.File;

public class FileUtils {

    /**
     * sd卡是否是正常挂载状态
     */
    public static boolean isSDCardEnableByEnvironment() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


    /**
     * 获取缓存路径（优先外部缓存目录）
     *
     * @return
     */
    public static File getCacheDir() {
        File dirFile = null;
        //判断sd卡是否是正常挂载状态
        if (FileUtils.isSDCardEnableByEnvironment()) {
            dirFile = BaseApp.mContext.getExternalCacheDir();
        }
        if (dirFile == null || !dirFile.exists()) {
            dirFile =  BaseApp.mContext.getCacheDir();
        }
        SafeLog.log("根目录："+dirFile.getAbsolutePath());
        return dirFile;
    }
}
