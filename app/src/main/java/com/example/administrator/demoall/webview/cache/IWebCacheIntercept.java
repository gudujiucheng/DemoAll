package com.example.administrator.demoall.webview.cache;

import android.graphics.Bitmap;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

/**
 * @author owenli
 * @date 2018/11/20
 */
public interface IWebCacheIntercept {

    WebResourceResponse shouldInterceptRequest(WebResourceRequest request);

    void onPageStarted(WebView view, String url, Bitmap favicon);

    void onPageFinished(WebView view, String url);
}
