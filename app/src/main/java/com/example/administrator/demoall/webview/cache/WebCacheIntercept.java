package com.example.administrator.demoall.webview.cache;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.example.administrator.demoall.webview.cache.bean.CacheStrategy;
import com.example.administrator.demoall.webview.cache.dynamic.DynamicResourceIntercept;
import com.example.administrator.demoall.webview.cache.preload.PreloadResourceIntercept;


/**
 * @author owenli
 * @date 2018/11/20
 */
public class WebCacheIntercept implements IWebCacheIntercept{

    private IWebCacheIntercept mWebCacheIntercept;

    public WebCacheIntercept(CacheStrategy cacheStrategy) {
        mWebCacheIntercept = new DynamicResourceIntercept(cacheStrategy);
    }

    public WebResourceResponse shouldInterceptRequest(WebResourceRequest request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if ("get".equals(request.getMethod().toLowerCase())) {
                Uri uri = request.getUrl();
                if (uri == null) {
                    return null;
                }
                String scheme = uri.getScheme();
                if (TextUtils.isEmpty(scheme) || !scheme.startsWith("http")) {
                    return null;
                }

                WebResourceResponse response = PreloadResourceIntercept.shouldInterceptRequest(uri);//TODO 预加载
                if (response != null) {
                    return response;
                } else {
                    return mWebCacheIntercept.shouldInterceptRequest(request);//TODO 本地取
                }
            }
        }
        return null;
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        mWebCacheIntercept.onPageStarted(view, url, favicon);
    }

    public void onPageFinished(WebView view, String url) {
        mWebCacheIntercept.onPageFinished(view, url);
    }
}
