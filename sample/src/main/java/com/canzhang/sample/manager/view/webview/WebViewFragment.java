package com.canzhang.sample.manager.view.webview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.canzhang.sample.R;
import com.example.base.base.BaseFragment;

import java.util.Date;

/**
 * webview 相关测试
 * https://blog.csdn.net/qq_24530405/article/details/52067474
 */
public class WebViewFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout mLlContainer;
    private WebView mWebView;

    public static Fragment newInstance(int type) {
        return new WebViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sample_fragment_web, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {

    }


    private void initWeb() {
        WebSettings settings = mWebView.getSettings();
        settings.setSupportZoom(true);          //支持缩放
        settings.setBuiltInZoomControls(true);  //启用内置缩放装置
        settings.setJavaScriptEnabled(true);    //启用JS脚本
//        settings.setDatabasePath("xxx");      //设置数据库缓存路径
//        settings.setAppCachePath("xxxx");     //设置Application Caches缓存目录
        settings.setDefaultTextEncodingName("utf-8"); //设置默认编码
        settings.setUseWideViewPort(false);      //将图片调整到适合webview的大小
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局
        settings.supportMultipleWindows();       //多窗口
        settings.setAllowFileAccess(true);       //设置可以访问文件
        settings.setNeedInitialFocus(true);      //当webview调用requestFocus时为webview设置节点
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        settings.setLoadWithOverviewMode(true);  //缩放至屏幕的大小

        /**
         * 设置缓存方式，主要有以下几种：
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据。
         * LOAD_DEFAULT: 根据cache-control决定是否从网络上取数据。
         * LOAD_CACHE_NORMAL: API level 17中已经废弃, 从API level 11开始作用同LOAD_DEFAULT模式。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据。
         * LOAD_CACHE_ELSE_NETWORK：只要本地有，无论是否过期，或者no-cache，都使用缓存
         */
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        /**
         * 启DOM storage API功能
         * （HTML5 提供的一种标准的接口，主要将键值对存储在本地，在页面加载完毕后可以通过 JavaScript 来操作这些数据。）
         */
        settings.setDomStorageEnabled(true);


        /**
         * 加快HTML网页加载完成速度（默认情况html代码下载到WebView后，webkit开始解析网页各个节点，
         * 发现有外部样式文件或者外部脚本文件时，会异步发起网络请求下载文件，但如果在这之前也有解析到image节点，
         * 那势必也会发起网络请求下载相应的图片。在网络情况较差的情况下，过多的网络请求就会造成带宽紧张，
         * 影响到css或js文件加载完成的时间，造成页面空白loading过久。解决的方法就是告诉WebView先不要自动加载图片，
         * 等页面finish后再发起图片加载。）
         */
        //第一步： 首先在WebView初始化时添加如下代码
        if (Build.VERSION.SDK_INT >= 19) {
            /*对系统API在19以上的版本作了兼容。因为4.4以上系统在onPageFinished时再恢复图片加载时,如果存在多张图片引用的是相同的src时，会只有一个image标签得到加载，因而对于这样的系统我们就先直接加载。*/
            mWebView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            mWebView.getSettings().setLoadsImagesAutomatically(false);
        }


        mWebView.addJavascriptInterface(new JSInterface(), "Android");

        mWebView.setWebViewClient(new WebViewClient() {
            /**
             *  shouldOverrideUrlLoading 的返回值到底代表什么呢？
             *  return true，则在打开新的url时WebView就不会再加载这个url了，所有处理都需要在WebView中操作，包含加载；(具体体现：百度页面的某些按钮点击不了)
             *  return false，则系统就认为上层没有做处理，接下来还是会继续加载这个url的；默认return false（具体 体现：所有的都能正常跳转）
             *
             *  还有一点需要注意的是，如果我们拦截了某个url，那么return false 和 return true区别不大，所以一般建议 return false。
             * @param view
             * @param url
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                log("开始加载网页");
            }

            /**
             * 你永远无法确定当WebView调用这个方法的时候，网页内容是否真的加载完毕了。
             * 当前正在加载的网页产生跳转的时候这个方法可能会被多次调用，
             * StackOverflow上有比较具体的解释（How to listen for a Webview finishing loading a URL in Android?）， 但其中列举的解决方法并不完美。
             * 所以当你的WebView需要加载各种各样的网页并且需要在页面加载完成时采取一些操作的话，
             * 可能WebChromeClient.onProgressChanged()比WebViewClient.onPageFinished()都要靠谱一些。
             * @param view
             * @param url
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                log("网页加载完成,这个是不准确的");

                //(加快渲染速度)第二步：在WebView的WebViewClient子类中重写onPageFinished()方法添加如下代码：
                if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
                    mWebView.getSettings().setLoadsImagesAutomatically(true);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                log("网页加载失败");
            }

            /**
             * 处理https请求，为 WebView 处理 ssl证书 设置 WebView 默认是不处理https请求的，
             * 需要在 WebViewClient 子类中重写父类的 onReceivedSslError 函数
             * @param view
             * @param handler
             * @param error
             */
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();//接受信任所有网站的证书
//                handler.cancel();//默认操作 不处理
//                handler.handleMessage(null);//可做其他处理
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return super.shouldInterceptRequest(view, url);
            }

            /**
             * 可拦截一些请求，做处理，然后resp，比如可以修改图片，或者自行做缓存等等
             * @param view
             * @param request
             * @return
             */
            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);

            }
        });

        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {//和 onBackPressed 一样
                        mWebView.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            //当WebView进度改变时更新窗口进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                log("加载进度：" + newProgress);

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                log("页面标题：" + title);
            }
        });

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                //下载任务...，主要有两种方式
                //（1）自定义下载任务
                //（2）调用系统的download的模块
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }


    private void initView(View view) {
        /**
         * WebView对象并不是直接写在布局文件中的，而是通过一个LinearLayout容器，使用addview(webview)动态向里面添加的。
         * 另外需要注意创建webview需要使用applicationContext而不是activity的context，
         * 销毁时不再占有activity对象，最后离开的时候需要及时销毁webview，
         * onDestory()中应该先从LinearLayout中remove掉webview,再调用webview.removeAllViews();webview.destory();
         */
        mLlContainer = view.findViewById(R.id.ll_container);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(params);
        mLlContainer.addView(mWebView);

        log(mLlContainer.toString() + " <<<----------对比----------->>> " + view.toString());

        initWeb();
        view.findViewById(R.id.bt_jump).setOnClickListener(this);
        view.findViewById(R.id.bt_open_url).setOnClickListener(this);
        view.findViewById(R.id.bt_open_local_url).setOnClickListener(this);
        view.findViewById(R.id.bt_call_js).setOnClickListener(this);
        view.findViewById(R.id.bt_other_test).setOnClickListener(this);
    }

    private int i = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_jump: // 浏览器打开
//                Uri parse = Uri.parse("https://www.baidu.com/");
                //临时测试小程序打开
//                Uri parse = Uri.parse("https://m.q.qq.com/a/p/1108314714?s=pages%2fmain%2fliveRoom%2findex%3froomid%3d655543%26source%3dCFMSTAR");
                Uri parse = Uri.parse("https://privacy.qq.com/document/priview/896637b6dc4f4b80b48163580d63e07e");
                startActivity(new Intent(Intent.ACTION_VIEW, parse));
                break;
            case R.id.bt_open_url: //webView 内打开
//                mWebView.loadUrl("https://www.baidu.com/");
//                mWebView.loadUrl("https://pt.m.fenqile.com/index.html#/app-video");
//                mWebView.loadUrl("https://m.q.qq.com/a/p/1108314714?s=pages%2fmain%2fliveRoom%2findex%3froomid%3d655543%26source%3dCFMSTAR");
                mWebView.loadUrl("https://privacy.qq.com/document/priview/896637b6dc4f4b80b48163580d63e07e");
                break;
            case R.id.bt_open_local_url: //打开本地html文件
                mWebView.loadUrl("file:///android_asset/JavaAndJavaScriptCall.html");
                break;
            case R.id.bt_call_js: //调用js
                mWebView.loadUrl("javascript:javaCallJs(" + "'" + "第" + i + "次调用" + "'" + ")");
                i++;
                break;
            case R.id.bt_other_test: //其他测试
                otherTest();
                break;
        }
    }


    int j = 0;

    private class JSInterface {
        //JS需要调用的方法
        @JavascriptInterface
        public void showNativeToast(final String arg) {
            j++;
            showToast(arg);
            log("js调用java：" + arg + " 当前线程：" + Thread.currentThread());
            if (j % 2 == 0) {//人为制造崩溃
                String s = null;
                s.toString();
                log("崩溃之后，后面的代码就不会继续执行了");
            }


            /**
             * 结论：
             * 1、js回调的线程并非主线程，这点需要留意 Thread[JavaBridge,7,main]
             * 2、js线程内部有错误的时候，外部无感知，会导致后面的方法执行不到，甚至可能导致下一次web加载异常，最好是在整个js方法上加上try catch方法。
             */

        }
    }


    /**
     * 跟上面的js线程做对比测试
     * 测试一般线程如果内部报错，是否有崩溃
     * 结论：是会崩溃的
     */
    private void otherTest() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                log("666666----------线程执行:" + Thread.currentThread().getName());
//                int j = 10 / 0;
//                log("666666----------线程崩溃后:" + Thread.currentThread().getName());
//
//            }
//        }).start();
//
//        log("88888----------主线程:" + Thread.currentThread().getName());


        log("------------------>>>" + new Date(0).toGMTString());

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mLlContainer.removeView(mWebView);
        mWebView.stopLoading();
        mWebView.removeAllViews();
        mWebView.destroy();
        mWebView = null;

    }

    /**
     * 在activity被杀死之后，依然保持webView的状态，方便用户下次打开的时候可以回到之前的状态。
     * webview支持saveState(bundle)和restoreState(bundle)方法。
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebView = new WebView(((Activity) mContext).getApplication());
        if (null != savedInstanceState) {
            mWebView.restoreState(savedInstanceState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();//可以较小影响的处理后台后 音频继续播放问题（如果想把js也停掉 可以用pauseTimers）
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }
}
