package com.example.administrator.demoall.webview.cache.dynamic;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;


import com.example.administrator.demoall.webview.cache.IWebCacheIntercept;
import com.example.administrator.demoall.webview.cache.bean.Cache;
import com.example.administrator.demoall.webview.cache.bean.CacheStrategy;
import com.example.administrator.demoall.webview.cache.bean.MemoryObject;
import com.example.administrator.demoall.webview.cache.config.WebCacheConfig;
import com.example.administrator.demoall.webview.cache.disklru.DiskLruCache;
import com.example.administrator.demoall.webview.cache.disklru.LruCacheHelper;
import com.example.administrator.demoall.webview.cache.utils.MD5;
import com.example.administrator.demoall.webview.cache.utils.WebCacheUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * @author owenli
 * @date 2018/11/20
 */
public class DynamicResourceIntercept implements IWebCacheIntercept {

    public boolean inPageList = false;

    private CacheStrategy cacheStrategy;

    public DynamicResourceIntercept(CacheStrategy cacheStrategy) {
        this.cacheStrategy = cacheStrategy;
    }

    public boolean needUseCache(String path, Cache cache) {
        ArrayList<String> diskExt = cacheStrategy.getDiskCacheExt();
        ArrayList<String> menExt = cacheStrategy.getMenCacheExt();

        cache.enableDisk = inExtList(path, diskExt);
        cache.enableMemory = inExtList(path, menExt);
        return cache.enableMemory | cache.enableDisk;
    }

    private boolean inExtList(String path, ArrayList<String> diskExt) {
        return true;
//        if (diskExt == null || diskExt.size() <= 0) {
//            return false;
//        }
//
//        if (!TextUtils.isEmpty(path)) {
//            for (String extension : diskExt) {
//                if (path.endsWith(extension)) {
//                    return true;
//                }
//            }
//        }
//        return false;
    }

    public boolean isInPageList(String urlPath, String url) {
//        ArrayList<Pattern> pages = cacheStrategy.getCachePagesRegular();
//
//        if (pages == null || pages.size() <= 0) {
//            inPageList = false;
//            return inPageList;
//        }
//
//        if (!TextUtils.isEmpty(urlPath) && !TextUtils.isEmpty(url)) {
//            //如果不是html结尾的，返回之前useCache的值
//            if (!urlPath.endsWith(".html")) {
//                return inPageList;
//            }
//            for (Pattern page : pages) {
//                if (page.matcher(url).matches()) {
//                    inPageList = true;
//                    return inPageList;
//                }
//            }
//        }
//        inPageList = false;
//        return inPageList;
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WebResourceResponse shouldInterceptRequest(WebResourceRequest request) {
        Uri uri = request.getUrl();
        String url = uri.toString();
        String path = uri.getPath().toLowerCase();//TODO 空指针警报
        Cache cache = new Cache();
        if (cacheStrategy == null
                || !isInPageList(path, url)) {
            return null;
        }
        String fileName = null;
        // 如果是html先判断内存缓存中有没有，如果有使用内存缓存
        // 这边单独判断html是因为我们预加载的页面需要缓存html，
        // 而非预加载的页面不缓存html，如果没在这边提前判断，
        // 下面的needUseCache中CacheExt列表中不存在html，
        // 导致预加载的页面html缓存无法命中
        if (path.endsWith(".html")) {
            fileName = MD5.getMessageDigest(url);
            WebResourceResponse memoryRsp = getMemoryWebResourceResponse(uri, fileName);//TODO 从内存里面尝试获取
            if (memoryRsp != null) {
                return memoryRsp;
            }
        }

        if (!needUseCache(path, cache)) {
            return null;
        }

        if (TextUtils.isEmpty(fileName)) {
            fileName = MD5.getMessageDigest(url);
        }

        if (WebCacheConfig.sBlacklist.contains(fileName)) {
            Log.d("WebCache", "Blacklist fileName = " + fileName
                    + ",\n uri = " + uri);
            return null;
        }

        WebResourceResponse webResourceResponse = getCacheWebResourceResponse(uri, fileName, cache);//TODO 同时从内存和硬盘尝试获取
        if (webResourceResponse != null) {
            return webResourceResponse;
        }

        return getHttpWebResourceResponse(uri, fileName, cache);
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {

    }


    public void onPageFinished(WebView view, String url) {
        inPageList = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    private WebResourceResponse getCacheWebResourceResponse(Uri uri, String fileName, Cache cache) {
        if (cache.enableMemory) {//TODO 首先尝试从内存获取
            WebResourceResponse memoryRsp = getMemoryWebResourceResponse(uri, fileName);
            if (memoryRsp != null) {
                return memoryRsp;
            }
        }

        if (cache.enableDisk) {//TODO 然后尝试从硬盘获取
            WebResourceResponse diskRsp = getDiskWebResourceResponse(uri, fileName, cache);
            if (diskRsp != null) return diskRsp;
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    private WebResourceResponse getDiskWebResourceResponse(Uri uri, String fileName, Cache cache) {
        Log.e("REVIEW","getDiskWebResourceResponse线程："+Thread.currentThread().getName());
        DiskLruCache.Snapshot snapshot = LruCacheHelper.getInstance().get(fileName);//TODO 从硬盘获取数据
        if (snapshot != null) {
            try {
                Log.d("WebCache", "From disk cache fileName = " + fileName
                        + ",\n uri = " + uri);
                InputStream inputStream;
                if (cache.enableMemory) {
                    inputStream = new ResourceInputStream(uri.toString(), snapshot.getInputStream(0),
                            fileName,
                            snapshot.getLength(0),
                            cache);
                } else {
                    inputStream = snapshot.getInputStream(0);
                }
                return getWebResourceResponse(uri, inputStream);
            } catch (Exception e) {

            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    private WebResourceResponse getMemoryWebResourceResponse(Uri uri, String fileName) {
        MemoryObject memoryObject = LruCacheHelper.getInstance().getMemoryLruCache().get(fileName);
        if (memoryObject != null) {//TODO 从内存获取
            Log.d("WebCache", "From memory cache fileName = " + fileName
                    + ",\n uri = " + uri);
            return getWebResourceResponse(uri, memoryObject.getStream());
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WebResourceResponse getHttpWebResourceResponse(Uri uri, String fileName, Cache cache) {
        return getWebResourceResponse(uri, new HttpInputStream(uri.toString(), fileName, cache));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    private WebResourceResponse getWebResourceResponse(Uri uri, InputStream inputStream) {
        WebResourceResponse response = new WebResourceResponse(WebCacheUtils.getMime(uri),
                "UTF-8",
                inputStream);
        response.setResponseHeaders(WebCacheConfig.getDefaultResponseHeaders());
        return response;
    }
}
