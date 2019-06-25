package com.canzhang.sample.manager.url;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;


/**
 * 自定义uri（兼容特殊字符导致参数获取异常的问题）
 * 新增public方法的话一定注意替换回来对应字符，防止出现问题
 */
public class CustomUri {

    private Uri uri;

    private CustomUri(Uri uri) {
        this.uri = uri;
    }

    //用于截取特殊字符的key
    private static final String HASH_KEY = "08d383f559c568de92f5cbbf849bd2b2";

    public static CustomUri parse(String uriString) {
        log("uriString:" + uriString);
        if (!TextUtils.isEmpty(uriString)) {
            if (uriString.contains("#")) {
                uriString = uriString.replaceAll("#", HASH_KEY);
                log("after replace uriString:" + uriString);
            }
        }
        return new CustomUri(Uri.parse(uriString));
    }


    public String getQueryParameter(String key) {
        try {
            String parameter = uri.getQueryParameter(key);
            if (parameter == null) {
                return null;
            }
            log("parameter:" + parameter);

            return reCoverAllHashKey(uri.getQueryParameter(key));
        } catch (Throwable ignore) {
            return null;
        }
    }

    public String toString() {
        return reCoverAllHashKey(uri.toString());
    }

    public Uri getUri() {
        if (uri == null) {
            return null;
        }
        String s = reCoverAllHashKey(uri.toString());
        return Uri.parse(s);
    }


    /**
     * 恢复被替换的字符
     *
     * @param value
     * @return
     */
    private String reCoverAllHashKey(String value) {
        if (TextUtils.isEmpty(value)) {
            return value;
        }
        if (value.contains(HASH_KEY)) {
            return value.replaceAll(HASH_KEY, "#");
        }
        return value;

    }


    private static void log(String tips) {
        Log.d("Test", tips);
    }
}
