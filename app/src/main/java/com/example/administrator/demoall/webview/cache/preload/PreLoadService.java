package com.example.administrator.demoall.webview.cache.preload;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.administrator.demoall.BaseApp;
import com.example.administrator.demoall.webview.cache.IWebCacheIntercept;
import com.example.administrator.demoall.webview.cache.WebCacheIntercept;
import com.example.administrator.demoall.webview.cache.bean.CacheStrategy;
import com.example.administrator.demoall.webview.cache.config.WebCacheConfig;

import java.util.ArrayList;

/**
 * 预加载的一个实现
 * Created by owneli on 2018/12/04.
 */
public class PreLoadService extends Service {

    private int mPreLoadIndex = 0;
    private PreLoadUrl mPreLoadUrl;
    private CacheStrategy mCacheStrategy;//TODO 可以声明成局部变量
    private ArrayList<PreLoadUrl> mPreLoadList = new ArrayList<>();
    private IWebCacheIntercept mWebCacheIntercept;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }

        ArrayList<PreLoadUrl> allPreLoadUrl = new ArrayList<>();
        PreLoadUrl pre =  new PreLoadUrl();
        pre.url = "https://item.m.fenqile.com/S201709130587049.html";
        PreLoadUrl pre2 =  new PreLoadUrl();
        pre2.url = "https://item.m.fenqile.com/S201804130004657.html";
        allPreLoadUrl.add(pre);
//        allPreLoadUrl.add(pre2);

        if (!allPreLoadUrl.isEmpty()) {
            boolean result = initPreLoadList(allPreLoadUrl);
            mCacheStrategy = createCacheStrategy();
            mWebCacheIntercept = new WebCacheIntercept(mCacheStrategy);
            if (result) {
                WebView webView = new WebView(this.getApplicationContext());
                initWebClient(webView);
                initWebSetting(webView);
                loadNextPage(webView);
            }
        }

        return super.onStartCommand(intent, flags, startId);
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

    private boolean initPreLoadList(ArrayList<PreLoadUrl> allPreLoadUrl) {
        mPreLoadIndex = 0;
        mPreLoadUrl = null;
        mPreLoadList.clear();


        for (PreLoadUrl item : allPreLoadUrl) {
            if (item != null && !item.loaded && !TextUtils.isEmpty(item.url)) {
                mPreLoadList.add(item);
            }
        }
        return !mPreLoadList.isEmpty();
    }

    private void initWebClient(final WebView webView) {
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.d("WebCache","preload onReceivedError. errorCode:"+errorCode+description);
//                loadNextPage(view);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.e("WebCache","preload onReceivedError.error"+error.getDescription());
                super.onReceivedError(view, request, error);
//                loadNextPage(view);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("WebCache","preload onPageStarted." + url);
                mWebCacheIntercept.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("WebCache","preload onPageFinished." + url);
                mWebCacheIntercept.onPageFinished(view, url);
                loadNextPage(view);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                if ("post".equals(request.getMethod().toLowerCase())) {
                    return new WebResourceResponse("", "", null);//会导致post请求异常现象
                } else {
                    WebResourceResponse response = mWebCacheIntercept.shouldInterceptRequest(request);
                    if (response != null) {
                        Log.d("WebCache","preload shouldInterceptRequest. 1 url = " + request.getUrl().toString());
                        return response;
                    } else {
                        Log.d("WebCache","preload shouldInterceptRequest. 2 url = " + request.getUrl().toString());
                        String path = request.getUrl().getPath();
                        if (!TextUtils.isEmpty(path) && path.toLowerCase().endsWith(".html")) {
                            return super.shouldInterceptRequest(view, request);
                        } else {
                            return new WebResourceResponse("", "", null);
                        }
                    }
                }
            }
        });
    }

    private void loadNextPage(WebView view) {
        if (mPreLoadUrl != null) {
            mPreLoadUrl.loaded = true;
        }
        if (mPreLoadList != null && mPreLoadIndex < mPreLoadList.size()) {
            mPreLoadUrl = mPreLoadList.get(mPreLoadIndex);
            mPreLoadIndex++;
            Log.d("WebCache", "Load next page url = " + mPreLoadUrl.url);
            view.loadUrl(mPreLoadUrl.url);
        }else{
           stopSelf();//TODO
        }
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





    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
