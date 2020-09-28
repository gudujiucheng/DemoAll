package com.canzhang.sample.manager.view.webview;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.canzhang.sample.debug.DebugBaseApp;
import com.example.base.base.AppProxy;
import com.example.simple_test_annotations.MarkManager;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 测试cookie的使用规则
 * @Author: canzhang
 * @CreateDate: 2019/4/24 15:34
 * <p>
 * 参考文章：https://blog.csdn.net/Kelaker/article/details/82751287
 * <p>
 * TODO 还可以继续测试 会话cookie等场景
 */
@MarkManager(value = "cookie 测试")
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
        cookieSyncManager = CookieSyncManager.createInstance(AppProxy.getInstance().getApplication());
        cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
    }

    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        List<ComponentItem> list = new ArrayList<>();
        list.add(cookie());
        list.add(addCookie());
        list.add(addCookie01());
        list.add(addCookie02());
        list.add(addCookie03());
        list.add(removeCookie());
        list.add(removeAllCookie());
        return list;
    }

    private ComponentItem cookie() {
        return new ComponentItem("cookie介绍", "1、WebView是基于webkit内核的UI控件，相当于一个浏览器客户端。它会在本地维护每次会话的cookie(保存在data/data/package_name/app_WebView/Cookies.db)" +
                "\n2、当WebView加载URL的时候,WebView会从本地读取该URL对应的cookie，并携带该cookie与服务器进行通信。WebView通过android.webkit.CookieManager类来维护cookie。CookieManager是 WebView的cookie管理类。" +
                "\n3、数据库会根据name、host、path等生成一条记录，也即是说在CookieManager#setCookie()中name、host和path一致会导致覆盖原来的记录。" +
                "\n\n Domain:属性指定浏览器发出 HTTP 请求时，哪些域名要附带这个 Cookie。如果没有指定该属性，浏览器会默认将其设为当前 URL 的一级域名，比如www.example.com会设为.example.com，而且以后如果访问.example.com的任何子域名，HTTP 请求也会带上这个 Cookie。如果服务器在Set-Cookie字段指定的域名，不属于当前域名，浏览器会拒绝这个 Cookie。" +
                "\nPath:属性指定浏览器发出 HTTP 请求时，哪些路径要附带这个 Cookie。只要浏览器发现，Path属性是 HTTP 请求路径的开头一部分，就会在头信息里面带上这个 Cookie。比如，PATH属性是/，那么请求/docs路径也会包含该 Cookie。当然，前提是域名必须一致。path属性的默认值是发送Set-Cookie消息头所对应的URL中的path部分。" +
                "\nExpires:属性指定一个具体的到期时间，到了指定时间以后，浏览器就不再保留这个 Cookie。它的值是 UTC 格式。可以通过设置它的expires属性为一个过去的日期来删除这个cookie。" +
                "\nMax-Age:属性指定从现在开始 Cookie 存在的秒数，比如60 * 60 * 24 * 365（即一年）。过了这个时间以后，浏览器就不再保留这个 Cookie。" +
                "\n如果同时指定了Expires和Max-Age，那么Max-Age的值将优先生效。" +
                "\n如果Set-Cookie字段没有指定Expires或Max-Age属性，那么这个 Cookie 就是 Session Cookie，即它只在本次对话存在，一旦用户关闭浏览器，浏览器就不会再保留这个 Cookie。", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showToast("参考下方测试数据，进一步了解，具体参考顶部链接");


            }
        });
    }

    private ComponentItem addCookie() {
        return new ComponentItem("添加cookie(不同path,相同host，存host，取url)", "依host存，这样都能取出", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://buy.m.canzhang.com/pay/query_order_line.json";
                Uri uri = Uri.parse(url);
                setCookie(uri.getHost(), "zc=zrc");

                String url1 = "https://buy.m.canzhang.com/201903180137531/index.html";
                Uri uri1 = Uri.parse(url1);
                setCookie(uri1.getHost(), "zk=zdk");


                log("\npath:" + uri.getPath() + "\npath1:" + uri1.getPath());
                log("\nhost:" + uri.getHost() + "\nhost1:" + uri1.getHost());
                queryCookie("第一个：", url);
                queryCookie("第二个：", url1);
                queryCookie("通用host 查询：", "https://buy.m.canzhang.com");


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
                String url = "https://sale.canzhang.com/pay/query_order_line.json";
                Uri uri = Uri.parse(url);
                setCookie(url, "zc=zrc");

                String url1 = "https://sale.canzhang.com/201903180137531/index.html";
                Uri uri1 = Uri.parse(url1);
                setCookie(url1, "zk=zdk");


                log("\npath:" + uri.getPath() + "\npath1:" + uri1.getPath());
                log("\nhost:" + uri.getHost() + "\nhost1:" + uri1.getHost());
                queryCookie("第一个：", url);
                queryCookie("第二个：", url1);
                queryCookie("通用host 查询：", "https://sale.canzhang.com");


//                2019-04-24 16:51:19.944 21613-21613/com.canzhang.sample E/Test: CookieTestManager:第一个： zc=zrc
//                2019-04-24 16:51:19.946 21613-21613/com.canzhang.sample E/Test: CookieTestManager:第二个： zk=zdk
//                2019-04-24 16:51:19.948 21613-21613/com.canzhang.sample E/Test: CookieTestManager:通用host 查询： null
            }
        });
    }


    private ComponentItem addCookie02() {
        return new ComponentItem("添加cookie(不同path,相同host，存一级host，取url)", "一级更短的domin都能取出来", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://buy.m.canzhang.com/pay/query_order_line.json";
                Uri uri = Uri.parse(url);
                setCookie("canzhang.com", "zc=zrc");

                String url1 = "https://buy.m.canzhang.com/201903180137531/index.html";
                Uri uri1 = Uri.parse(url1);
                setCookie("canzhang.com", "zk=zdk");


                log("\npath:" + uri.getPath() + "\npath1:" + uri1.getPath());
                log("\nhost:" + uri.getHost() + "\nhost1:" + uri1.getHost());
                queryCookie("第一个：", url);
                queryCookie("第二个：", url1);
                queryCookie("通用host 查询：", "https://buy.m.canzhang.com");
                queryCookie("通用host 查询2：", "https://canzhang.com");


//                2019-04-24 16:50:09.155 21613-21613/com.canzhang.sample E/Test: CookieTestManager:第一个： zc=zrc; zk=zdk
//                2019-04-24 16:50:09.158 21613-21613/com.canzhang.sample E/Test: CookieTestManager:第二个： zc=zrc; zk=zdk
//                2019-04-24 16:50:09.160 21613-21613/com.canzhang.sample E/Test: CookieTestManager:通用host 查询： zc=zrc; zk=zdk
            }
        });
    }


    private ComponentItem addCookie03() {
        return new ComponentItem("设置cookie为过期(不同path,相同host，存一级host，取url)", "设置为过期，就查不到这个cookie了", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://buy.m.canzhang.com/pay/query_order_line.json";

                setCookie(url, "zc", "is a good man", getCookieExpires());
                queryCookie("首次添加有效cookie查询查询：", url);

                setCookie(url, "zc", "is a good man", 0);
                queryCookie("设置无效后cookie查询查询：", url);


            }
        });
    }

    private void queryCookie(String tips, String url) {
        String cookie04 = CookieManager.getInstance().getCookie(url);
        log(tips + " " + cookie04);
    }

    private ComponentItem removeCookie() {
        return new ComponentItem("removeCookie（置空方式，key还在）", "这里cookie无法单独移除，可以通过置空处理", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCookie("https://buy.m.canzhang.com/pay/query_order_line.json", "zc=");
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


    //--------------------------------------------caidan-------------------------------------------------------------

    /**
     * @param url         地址
     * @param key         key
     * @param value       value
     * @param expiresTime 过期时间（如果传入0 就表示1970年起始点）
     */
    private void setCookie(String url, String key, String value, long expiresTime) {
        String topDomain = getTopDomain(url);
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(AppProxy.getInstance().getApplication());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //现在的现象是 用户进入fintech.lx.com域名之后，这个域名下会发起一个请求，这个请求是走到网关接口中请求数据，
            //此时，网关接口没有获取到session，认为未登录，所以返回未登录状态码，fintech接受到状态码跳转到passeport登录，
            // 但是进入passeport登录页面之后，passeport获取到了session，认为已登录，又跳转回了fintech
//            cookieManager.setAcceptThirdPartyCookies(mWvCustom, true);//TODO 跨域cookie读取(moa之前出现个问题,上面为问题表述)
        }
        String time = "; path=/; domain=." + topDomain + ";expires=" + (new Date(expiresTime)).toGMTString();
        StringBuilder build = new StringBuilder();
        try {
            build.append(key).append("=").append(URLEncoder.encode(value, "UTF-8")).append(time);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log("cookie value:" + build.toString());
        log("topDomain:" + topDomain);
        cookieManager.setCookie(topDomain, build.toString());
        cookieSyncManager.sync();

    }

    /**
     * 有效时间
     *
     * @return
     */
    public long getCookieExpires() {
        return System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30;
    }

    private String getHostFromUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }

        Uri uri = Uri.parse(url);

        String host = uri.getHost();
        if (TextUtils.isEmpty(host)) {
            return "";
        }
        return host;
    }


//    顶级域名、一级域名(TLDs,Top-level domains,first-level domains)是这样的： 如 .com / .net / .org /.cn
//
//    二级域名(SLD)： 如 .CCTV.COM / .google.com / 例： www.cctv.com和abc.cctv.com其中倒数第二个点前的www和abc是主机名，地位上并无差别。
//
//    三级域名：如 .a.google.com  。例： qq110.a.google.com 其中倒数第三个点前的qq110是主机名。
//
//    所以，认为只有www.abc.com 这样的域名是顶级域名是错误的！ www只不过是万维网的一个表示形式，和ftp 、mail 、或者任意的名字地位上并无差别。 有些交换链接和网址站只要带www的，觉得奇怪，没有基本的认识。
//
//            .com.cn同样是一个二级域名 ，www.abc.com.cn这样的网址就是三级域名了。
//
//    可以参阅维基百科：http://zh.wikipedia.org/w/index.php?title=%E5%9F%9F%E5%90%8D&variant=zh-hans#.E9.A1.B6.E7.BA.A7.E5.9F.9F.E5.90.8D


    /**
     * 获取url的顶级域名
     * 如果传入类似.canzhang.com 会出异常
     * @param
     * @return
     */
    public static String getTopDomain(String url) {
//        if (TextUtils.isEmpty(url)) {
//            return null;
//        }
        try {
            //获取值转换为小写
            String host = new URL(url).getHost().toLowerCase();//news.hexun.com
            Pattern pattern = Pattern.compile("[^\\.]+(\\.com\\.cn|\\.net\\.cn|\\.org\\.cn|\\.gov\\.cn|\\.com|\\.net|\\.cn|\\.org|\\.cc|\\.me|\\.tel|\\.mobi|\\.asia|\\.biz|\\.info|\\.name|\\.tv|\\.hk|\\.公司|\\.中国|\\.网络)");
            Matcher matcher = pattern.matcher(host);
            while (matcher.find()) {
                return matcher.group();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
