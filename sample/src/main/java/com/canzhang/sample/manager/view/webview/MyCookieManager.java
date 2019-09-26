package com.canzhang.sample.manager.view.webview;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.canzhang.sample.debug.DebugBaseApp;
import com.component.debugdialog.DebugDialog;
import com.example.base.base.AppProxy;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("StaticFieldLeak")
public class MyCookieManager {

    private static final String DEFAULT_PATH_DOMAIN = "; path=/; domain=.canzhang.com; ";
    /**
     * @param url          链接
     * @param key          键
     * @param value        值
     * @param isNeedEncode 是否进行UTF-8编码
     * @param expiresTime  cookie过期时间戳  当<=0时不设置有效期 只存储在内存 下次启动丢失
     */
    public void setKVCookie(String url, String key, String value, boolean isNeedEncode, long expiresTime) {
        setKVCookie(url, Collections.singletonMap(key, value), isNeedEncode, expiresTime);
    }

    /**
     * @param url          链接
     * @param map          键值对映射Map
     * @param isNeedEncode 是否进行UTF-8编码
     * @param expiresTime  cookie过期时间戳  当<=0时不设置有效期 只存储在内存 下次启动丢失
     */
    private void setKVCookie(String url, Map<String, String> map, boolean isNeedEncode, long expiresTime) {
        if (map == null || map.size() == 0) {
            return;
        }
        ArrayList<String> list = new ArrayList<>(10);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String cookie = generateCookie(entry.getKey(), entry.getValue(), isNeedEncode, expiresTime);
            if (!TextUtils.isEmpty(cookie)) {
                list.add(cookie);
            }
        }
        setFinalCookie(url, list);
    }

    /**
     * 生成最终的cookie字符串 用于Set-Cookie 带默认的domain和path
     *
     * @param key          键
     * @param value        值
     * @param isNeedEncode 是否对值进行UTF-8编码
     * @param expiresTime  cookie过期时间戳
     * @return cookie字符串
     */
    private String generateCookie(String key, String value, boolean isNeedEncode, long expiresTime) {
        if (TextUtils.isEmpty(key) ) {
            return "";
        }
        StringBuilder build = new StringBuilder();
        if (isNeedEncode) {
            try {
                value = URLEncoder.encode(value, "UTF-8");
            } catch (Throwable e) {

            }
        }
        build.append(key).append("=").append(value).append(DEFAULT_PATH_DOMAIN);
        if (expiresTime > 0) {
            String time = "expires=" + new Date(expiresTime).toGMTString();
            build.append(time).append("; ");
        }
        return build.toString();
    }


    /**
     * @param url        链接
     * @param cookieList 设置最终的cookie到系统CookieManager并同步
     */
    private void setFinalCookie(String url, List<String> cookieList) {

        if (TextUtils.isEmpty(url) || cookieList == null || cookieList.size() == 0) {
            return;
        }
        Log.e("Test","cookieList:"+cookieList.toString());
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(AppProxy.getInstance().getApplication());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        try {
            for (String cookie : cookieList) {
                if (!TextUtils.isEmpty(cookie)) {
                    cookieManager.setCookie(url, cookie);
                    Log.e("Test","cookie:"+cookie);
                }
            }
        } catch (Throwable e) {

        } finally {
            //防止catch之后cookie并未同步
            try {
                cookieSyncManager.sync();
            } catch (Throwable e) {

            }
        }
    }


    /**
     * @param url 链接
     * @return url 对应的cookie字符串 包含系统CookieManager返回的以及本地的
     */
    public String getCookieStr(String url) {
        Map<String, String> cookieMap = getCookieMap(url);
        if (cookieMap == null || cookieMap.isEmpty()) {
            return "";
        }
        // 改编码的已经编码 无需再次编码
        return appendKVStr(cookieMap, false);
    }


    public Map<String, String> getCookieMap(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        CookieManager cookieManager = CookieManager.getInstance();
        String webCookie = cookieManager.getCookie(url);
        Log.d("Test","webCookie:"+webCookie);
        return  resolveToMap(webCookie);
    }



    /**
     * @param str 带";"分割的字符串
     * @return 切割成Map
     */
    public HashMap<String, String> resolveToMap(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        HashMap<String, String> map = new HashMap<>();
        String[] split = str.split(";");
        String k, v;
        for (String s : split) {
            if (TextUtils.isEmpty(s)) {
                continue;
            }
            int index = s.indexOf("=");
            if (index <= 0 || index == s.length() - 1) {
                continue;
            }
            try {
                k = s.substring(0, index).trim();
                v = s.substring(index + 1, s.length()).trim();
            } catch (Throwable e) {
                continue;
            }
            if (TextUtils.isEmpty(k) || TextUtils.isEmpty(v)) {
                continue;
            }
            map.put(k, v);
        }
        return map;
    }


    /**
     * 生成最终的cookie字符串 用于Cookie
     *
     * @param map          映射Map
     * @param isNeedEncode 是否对值进行UTF-8编码
     * @return 返回字符串 键值对 ";"分割
     */
    public String appendKVStr(Map<String, String> map, boolean isNeedEncode) {
        if (map == null || map.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                continue;
            }
            if (isNeedEncode) {
                try {
                    value = URLEncoder.encode(value, "UTF-8");
                } catch (Throwable e) {
                }
            }
            sb.append(key).append("=").append(value).append(";");
        }
        return sb.toString();
    }


}
