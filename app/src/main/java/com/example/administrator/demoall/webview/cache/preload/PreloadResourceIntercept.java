package com.example.administrator.demoall.webview.cache.preload;

import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import android.text.TextUtils;
import android.webkit.WebResourceResponse;


import com.example.administrator.demoall.webview.cache.config.WebCacheConfig;
import com.example.administrator.demoall.webview.cache.utils.WebCacheUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author owenli
 * @date 2018/10/30
 */
public class PreloadResourceIntercept {

    public static boolean useCache(String host) {
        return true;
//        boolean clear = WebCacheConfig.sClear;
//        ArrayList<String> hosts = WebCacheConfig.sHosts;
//        ArrayList<WebCacheConfig.SourceInfo> sources = WebCacheConfig.sSources;
//
//        if (clear
//                || TextUtils.isEmpty(host)
//                || hosts == null || hosts.size() <= 0
//                || sources == null || sources.size() <= 0) {
//            return false;
//        }
//        host = host.toLowerCase();
//        for (String item : hosts) {
//            if (host.equals(item)) {
//                return true;
//            }
//        }
//        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static WebResourceResponse shouldInterceptRequest(Uri uri) {
        if (useCache(uri.getHost())) {
            String uriPath = uri.getPath();
            if (!TextUtils.isEmpty(uriPath)) {
                String filePath = WebCacheConfig.getPreloadPath() + uri.getPath();
                File file = new File(filePath);//TODO 第一次明显不存在
                if (file.exists() && file.isFile()) {
                    try {
                        return getWebResourceResponse(uri, new FileInputStream(file));
                    } catch (FileNotFoundException e) {

                    }
                }
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    private static WebResourceResponse getWebResourceResponse(Uri uri, InputStream inputStream) {
        WebResourceResponse response = new WebResourceResponse(WebCacheUtils.getMime(uri),
                "UTF-8",
                inputStream);
        response.setResponseHeaders(WebCacheConfig.getDefaultResponseHeaders());
        return response;
    }
}
