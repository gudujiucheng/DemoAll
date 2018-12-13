package com.example.administrator.demoall.webview.cache.config;

import android.os.Environment;
import android.text.TextUtils;

import com.example.administrator.demoall.BaseApp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * @author owenli
 * @date 2018/10/29
 */
public class WebCacheConfig {
    //用于存储加载失败的fileName，下次打开不缓存
    public volatile static LimitQueue<String> sBlacklist = new LimitQueue<>(20);
    public volatile static ArrayList<String> sHosts;
    public volatile static ArrayList<SourceInfo> sSources;
    public volatile static ArrayList<Pattern> sCachePagesRegular;
    public volatile static ArrayList<String> sCacheExtensions;


    public static class SourceInfo {

    }


    public static String getWebCachePath() {//TODO private 即可
        return getDownloadPath() + "WebCache";
    }

    public static String getPreloadPath() {
        return getWebCachePath() + "/Preload";
    }

    public static String getPageCachePath() {
        return getWebCachePath() + "/PageCache";
    }




    public static HashMap<String, String> getDefaultResponseHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        return headers;
    }



    private static String mDownloadPath;
    public  static String getDownloadPath() {
        if (!TextUtils.isEmpty(mDownloadPath)) {
            return mDownloadPath;
        }
        String path;
        File dirFile = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dirFile = BaseApp.mContext.getExternalCacheDir();
        }
        if (dirFile == null || !dirFile.exists()) {


            dirFile = BaseApp.mContext.getCacheDir();
        }
        path = dirFile.getAbsolutePath() + File.separator + "download" + File.separator;
        mDownloadPath = path;
        return mDownloadPath;
    }
}
