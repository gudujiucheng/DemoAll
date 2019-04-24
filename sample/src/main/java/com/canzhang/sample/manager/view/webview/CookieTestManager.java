package com.canzhang.sample.manager.view.webview;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.canzhang.sample.debug.DebugBaseApp;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 测试cookie的使用规则
 * @Author: canzhang
 * @CreateDate: 2019/4/24 15:34
 * <p>
 * 参考文章：https://blog.csdn.net/Kelaker/article/details/82751287
 *
 * TODO 还可以继续测试过期时间等等场景
 */
public class CookieTestManager extends BaseManager {
    /**
     * 之前同步cookie需要用到CookieSyncManager类，现在这个类已经被deprecated。
     * 如今WebView已经可以在需要的时候自动同步cookie了，
     * 所以不再需要创建CookieSyncManager类的对象来进行强制性的同步cookie了。
     * 现在只需要获得 CookieManager的对象将cookie设置进去就可以了。
     */
    CookieSyncManager cookieSyncManager;
    CookieManager cookieManager;

    {
        //初始化实际上是会被提取到类的构造器中被执行的，但是会比类构造器中的代码块优先执行到
        cookieSyncManager = CookieSyncManager.createInstance(DebugBaseApp.sContext);
        cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
    }

    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        List<ComponentItem> list = new ArrayList<>();
        list.add(addCookie());
        list.add(addCookie01());
        list.add(addCookie02());
        list.add(removeCookie());
        list.add(removeAllCookie());
        return list;
    }

    private ComponentItem addCookie() {
        return new ComponentItem("添加cookie(不同path,相同host，存host，取url)", "1、WebView是基于webkit内核的UI控件，相当于一个浏览器客户端。它会在本地维护每次会话的cookie(保存在data/data/package_name/app_WebView/Cookies.db)" +
                "\n2、当WebView加载URL的时候,WebView会从本地读取该URL对应的cookie，并携带该cookie与服务器进行通信。WebView通过android.webkit.CookieManager类来维护cookie。CookieManager是 WebView的cookie管理类。" +
                "\n3、数据库会根据name、host、path等生成一条记录，也即是说在CookieManager#setCookie()中name、host和path一致会导致覆盖原来的记录。", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://buy.m.fenqile.com/pay/query_order_line.json";
                Uri uri = Uri.parse(url);
                setCookie(uri.getHost(), "zc=zrc");

                String url1 = "https://buy.m.fenqile.com/201903180137531/index.html";
                Uri uri1 = Uri.parse(url1);
                setCookie(uri1.getHost(), "zk=zdk");


                log("\npath:" + uri.getPath() + "\npath1:" + uri1.getPath());
                log("\nhost:" + uri.getHost() + "\nhost1:" + uri1.getHost());
                queryCookie("第一个：", url);
                queryCookie("第二个：", url1);
                queryCookie("通用host 查询：", "https://buy.m.fenqile.com");


//                2019-04-24 16:50:09.155 21613-21613/com.canzhang.sample E/Test: CookieTestManager:第一个： zc=zrc; zk=zdk
//                2019-04-24 16:50:09.158 21613-21613/com.canzhang.sample E/Test: CookieTestManager:第二个： zc=zrc; zk=zdk
//                2019-04-24 16:50:09.160 21613-21613/com.canzhang.sample E/Test: CookieTestManager:通用host 查询： zc=zrc; zk=zdk
            }
        });
    }

    private ComponentItem addCookie01() {
        return new ComponentItem("添加cookie(相同host，不同path，存url，取url)", "这样只能各自获取各自的，path局限", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://sale.fenqile.com/pay/query_order_line.json";
                Uri uri = Uri.parse(url);
                setCookie(url, "zc=zrc");

                String url1 = "https://sale.fenqile.com/201903180137531/index.html";
                Uri uri1 = Uri.parse(url1);
                setCookie(url1, "zk=zdk");


                log("\npath:" + uri.getPath() + "\npath1:" + uri1.getPath());
                log("\nhost:" + uri.getHost() + "\nhost1:" + uri1.getHost());
                queryCookie("第一个：", url);
                queryCookie("第二个：", url1);
                queryCookie("通用host 查询：", "https://sale.fenqile.com");


//                2019-04-24 16:51:19.944 21613-21613/com.canzhang.sample E/Test: CookieTestManager:第一个： zc=zrc
//                2019-04-24 16:51:19.946 21613-21613/com.canzhang.sample E/Test: CookieTestManager:第二个： zk=zdk
//                2019-04-24 16:51:19.948 21613-21613/com.canzhang.sample E/Test: CookieTestManager:通用host 查询： null
            }
        });
    }


    private ComponentItem addCookie02() {
        return new ComponentItem("添加cookie(不同path,相同host，存删减host，取url)",  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://buy.m.fenqile.com/pay/query_order_line.json";
                Uri uri = Uri.parse(url);
                setCookie("fenqile.com", "zc=zrc");

                String url1 = "https://buy.m.fenqile.com/201903180137531/index.html";
                Uri uri1 = Uri.parse(url1);
                setCookie("fenqile.com", "zk=zdk");


                log("\npath:" + uri.getPath() + "\npath1:" + uri1.getPath());
                log("\nhost:" + uri.getHost() + "\nhost1:" + uri1.getHost());
                queryCookie("第一个：", url);
                queryCookie("第二个：", url1);
                queryCookie("通用host 查询：", "https://buy.m.fenqile.com");
                queryCookie("通用host 查询2：", "https://fenqile.com");


//                2019-04-24 16:50:09.155 21613-21613/com.canzhang.sample E/Test: CookieTestManager:第一个： zc=zrc; zk=zdk
//                2019-04-24 16:50:09.158 21613-21613/com.canzhang.sample E/Test: CookieTestManager:第二个： zc=zrc; zk=zdk
//                2019-04-24 16:50:09.160 21613-21613/com.canzhang.sample E/Test: CookieTestManager:通用host 查询： zc=zrc; zk=zdk
            }
        });
    }

    private void queryCookie(String tips, String url) {
        String cookie04 = CookieManager.getInstance().getCookie(url);
        log(tips + " " + cookie04);
    }

    private ComponentItem removeCookie() {
        return new ComponentItem("removeCookie", "这里cookie无法单独移除，可以通过置空处理", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCookie("https://buy.m.fenqile.com/pay/query_order_line.json", "zc=");
            }
        });
    }


    private ComponentItem removeAllCookie() {
        return new ComponentItem("removeAllCookie", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cookieManager.removeAllCookie();
                cookieManager.removeSessionCookie();
                if (Build.VERSION.SDK_INT < 21) {
                    cookieSyncManager.sync();
                } else {
                    cookieManager.flush();
                }
            }
        });
    }


    /**
     * 设置cookie
     *
     * @param url
     * @param cookie
     */
    private void setCookie(String url, String cookie) {
        try {
            cookieManager.setCookie(url, cookie);
        } catch (Throwable e) {

        } finally {
            //防止catch之后cookie并未同步
            try {
                if (Build.VERSION.SDK_INT < 21) {
                    CookieSyncManager.getInstance().sync();
                } else {
                    CookieManager.getInstance().flush();
                }
            } catch (Throwable e) {

            }
        }
    }
}
