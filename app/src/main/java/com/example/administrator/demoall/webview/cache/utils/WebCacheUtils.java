package com.example.administrator.demoall.webview.cache.utils;

import android.net.Uri;

/**
 * @author owenli
 * @date 2018/10/30
 */
public class WebCacheUtils {
    public static String getMime(Uri currentUri) {
        String mime = "text/html";
        String path = currentUri.getPath();
        if (path.endsWith(".css")) {
            mime = "text/css";
        } else if (path.endsWith(".js")) {
            mime = "application/x-javascript";
        } else if (path.endsWith(".jpg") || path.endsWith(".gif") ||
                path.endsWith(".png") || path.endsWith(".jpeg") ||
                path.endsWith(".webp") || path.endsWith(".bmp")) {
            mime = "image/*";
        }
        return mime;
    }

}
