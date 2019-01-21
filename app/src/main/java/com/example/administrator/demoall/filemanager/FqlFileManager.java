package com.example.administrator.demoall.filemanager;

import android.text.TextUtils;

import com.example.administrator.SafeLog;

import java.io.File;

/**
 * 分期乐文件管理中心
 * by zc  2018年12月10日16:23:16
 */
public class FqlFileManager {
    private static FqlFileManager instance;
    private String mDownloadPath;


    private FqlFileManager() {
    }

    public static FqlFileManager getInstance() {
        return FqlFileManager.FqlFileManagerHolder.instance;
    }

    private static class FqlFileManagerHolder {
        private static final FqlFileManager instance = new FqlFileManager();
    }


    /**
     * app下载根路径（子文件夹包含 so、webView、image等）
     *
     * @return
     */
    public String getDownloadRootPath() {
        if (!TextUtils.isEmpty(mDownloadPath)) {
            SafeLog.log("app下载目录："+mDownloadPath);
            return mDownloadPath;
        }
        String path;
        File dirFile = FileUtils.getCacheDir();
        path = dirFile.getAbsolutePath() + File.separator + "download" + File.separator;
        mDownloadPath = path;
        SafeLog.log("app下载目录："+mDownloadPath);
        return mDownloadPath;
    }


    /**
     * 获取webView预加载缓存路径。
     *
     * @return
     */
    public String getPreloadWebCachePath() {
        return getWebCacheRootPath() + File.separator + "Preload";
    }

    /**
     * 获取webView缓存路径
     *
     * @return
     */
    public String getNormalWebCachePath() {
        return getWebCacheRootPath() + File.separator + "PageCache";
    }

    /**
     * webView 缓存的根路径（子集包含预加载、和普通常规缓存等）
     *
     * @return
     */
    private String getWebCacheRootPath() {
        return getDownloadRootPath() + "WebCache";
    }


    /**
     * 获取weex 下载路径
     *
     * @return
     */
    public String getWeexLoadPath() {
        return getDownloadRootPath() + "weex" + File.separator+File.separator+File.separator+File.separator;
    }

    public String getWeexLoadPath2() {
        return getDownloadRootPath() + "weex";
    }


    /**
     * 获取so下载存储路径
     *
     * @return
     */
    public String getSoLoadPath() {
        return getDownloadRootPath();
    }


}
