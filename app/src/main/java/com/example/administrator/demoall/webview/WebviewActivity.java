package com.example.administrator.demoall.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.administrator.demoall.BaseApp;
import com.example.administrator.demoall.R;
import com.example.administrator.demoall.webview.cache.IWebCacheIntercept;
import com.example.administrator.demoall.webview.cache.WebCacheIntercept;
import com.example.administrator.demoall.webview.cache.bean.CacheStrategy;
import com.example.administrator.demoall.webview.cache.config.WebCacheConfig;

import java.util.ArrayList;

/**
 * 测试下缓存逻辑
 */
public class WebviewActivity extends AppCompatActivity {

    private IWebCacheIntercept mWebCacheIntercept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        WebView webView = findViewById(R.id.wv);

        String url = "https://item.m.xxx.com/S201709130587049.html";

        CacheStrategy mCacheStrategy = createCacheStrategy();
        mWebCacheIntercept = new WebCacheIntercept(mCacheStrategy);
        initWebClient(webView);
        initWebSetting(webView);
        webView.loadUrl(url);


    }


    private void initWebSetting(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setDatabaseEnabled(true);
        webSettings.setSupportZoom(true);//支持缩放
        webSettings.setBuiltInZoomControls(true);//是否显示缩放工具，魅族手机必须设置此属性，缩放属性才有效，低版本的手机会显示缩放工具
        webSettings.setUseWideViewPort(true);//使用广泛的视窗 ,调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true);//可以加载更多格式页面
        webSettings.setAllowFileAccessFromFileURLs(false);
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        String dir = this.getApplicationContext().getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setGeolocationDatabasePath(dir);
        webSettings.setGeolocationEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCachePath(BaseApp.mContext.getCacheDir().getAbsolutePath());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //支持webview在同一界面中，一些不安全的内容（Http）能被加载到一个安全的站点上（Https），而其他类型的内容将会被阻塞。
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
    }


    private void initWebClient(final WebView webView) {
        webView.setWebViewClient(new WebViewClient() {

                                     @Override
                                     public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                         super.onReceivedError(view, errorCode, description, failingUrl);
                                         Log.d("WebCache", "preload onReceivedError. errorCode:" + errorCode + description);
//                loadNextPage(view);
                                     }

                                     @RequiresApi(api = Build.VERSION_CODES.M)
                                     @Override
                                     public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                                         Log.e("WebCache", "preload onReceivedError.error :" + error.getDescription() + " url：" + request.getUrl() + " " + request.getRequestHeaders());
                                         super.onReceivedError(view, request, error);
//                loadNextPage(view);
                                     }

                                     @Override
                                     public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                         super.onPageStarted(view, url, favicon);
                                         Log.d("WebCache", "preload onPageStarted." + url);
                                         mWebCacheIntercept.onPageStarted(view, url, favicon);
                                     }

                                     @Override
                                     public void onPageFinished(WebView view, String url) {
                                         super.onPageFinished(view, url);
                                         Log.d("WebCache", "preload onPageFinished." + url);
                                         mWebCacheIntercept.onPageFinished(view, url);
                                     }

                                     int i = 0;

                                     @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                     @Nullable
                                     @Override
                                     public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                                         WebResourceResponse response = mWebCacheIntercept.shouldInterceptRequest(request);
                                         if (response != null) {
                                             return response;
                                         }

                                         return super.shouldInterceptRequest(view, request);

                                     }
                                 }
        );
    }


    private CacheStrategy createCacheStrategy() {
        CacheStrategy cacheStrategy = new CacheStrategy();
        cacheStrategy.setCachePagesRegular(WebCacheConfig.sCachePagesRegular);
        cacheStrategy.setDiskEnabled(true);
        cacheStrategy.setMemoryEnabled(true);
        cacheStrategy.setDiskCacheExt(WebCacheConfig.sCacheExtensions);
        ArrayList<String> menCacheExt = new ArrayList<>();
        menCacheExt.add("html");
        menCacheExt.add(".js");
        menCacheExt.add(".css");
        cacheStrategy.setMenCacheExt(menCacheExt);
        return cacheStrategy;
    }


}
