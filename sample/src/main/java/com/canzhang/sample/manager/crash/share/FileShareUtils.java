package com.canzhang.sample.manager.crash.share;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.canzhang.sample.manager.crash.CrashHandler;
import com.canzhang.sample.manager.crash.file.FileUtil;
import com.example.base.utils.ToastUtil;

import java.io.File;

/**
 * 系统自带分享功能
 * Created by zhangcan603 on 2018/3/29.
 */
public class FileShareUtils {
    public static void shareCrashFile(Activity activity) {
        File logPath = new File(CrashHandler.CRASH_LOG_PATH);
        if (!logPath.exists() || logPath.listFiles() == null || logPath.listFiles().length == 0) {
            ToastUtil.toastShort("没有崩溃日志可以反馈");
            return;
        }
        if (logPath.listFiles().length == 1) {//只有一个崩溃文件则直接打开
            Uri uri = FileUtil.getUri(activity, logPath.listFiles()[0]);
            shareFile(activity, uri, "text/plain");
        } else {//多个文件的话，压缩之后发送压缩包
            FileUtil.zipFiles(logPath.listFiles(), CrashHandler.CRASH_LOG_ZIP_FILE_PATH);
            Uri uri = FileUtil.getUri(activity, new File(CrashHandler.CRASH_LOG_ZIP_FILE_PATH));
            shareFile(activity, uri, "application/zip");
        }
    }

    /**
     * 分享文件
     *
     * @param fileUri  文件uri
     * @param fileType 文件类型（具体文件类型可参见本类下方注释）
     */
    public static void shareFile(Activity activity, Uri fileUri, String fileType) {
        //表示要创建一个发送指定内容的隐式意图
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //读取权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //指定发送的内容
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
        //intent.putExtra(Intent.EXTRA_STREAM,uri);
        //指定发送内容的类型
        intent.setType(fileType);
        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /** 更多文件格式*/
    //{".3gp", "video/3gpp"},
    //{".apk", "application/vnd.android.package-archive"},
    //{".asf", "video/x-ms-asf"},
    //{".avi", "video/x-msvideo"},
    //{".bin", "application/octet-stream"},
    //{".bmp", "image/bmp"},
    //{".c", "text/plain"},
    //{".class", "application/octet-stream"},
    //{".conf", "text/plain"},
    //{".cpp", "text/plain"},
    //{".doc", "application/msword"},
    //{".exe", "application/octet-stream"},
    //{".gif", "image/gif"},
    //{".gtar", "application/x-gtar"},
    //{".gz", "application/x-gzip"},
    //{".h", "text/plain"},
    //{".htm", "text/html"},
    //{".html", "text/html"},
    //{".jar", "application/java-archive"},
    //{".java", "text/plain"},
    //{".jpeg", "image/jpeg"},
    //{".jpg", "image/jpeg"},
    //{".js", "application/x-javascript"},
    //{".log", "text/plain"},
    //{".m3u", "audio/x-mpegurl"},
    //{".m4a", "audio/mp4a-latm"},
    //{".m4b", "audio/mp4a-latm"},
    //{".m4p", "audio/mp4a-latm"},
    //{".m4u", "video/vnd.mpegurl"},
    //{".m4v", "video/x-m4v"},
    //{".mov", "video/quicktime"},
    //{".mp2", "audio/x-mpeg"},
    //{".mp3", "audio/x-mpeg"},
    //{".mp4", "video/mp4"},
    //{".mpc", "application/vnd.mpohun.certificate"},
    //{".mpe", "video/mpeg"},
    //{".mpeg", "video/mpeg"},
    //{".mpg", "video/mpeg"},
    //{".mpg4", "video/mp4"},
    //{".mpga", "audio/mpeg"},
    //{".msg", "application/vnd.ms-outlook"},
    //{".ogg", "audio/ogg"},
    //{".pdf", "application/pdf"},
    //{".png", "image/png"},
    //{".pps", "application/vnd.ms-powerpoint"},
    //{".ppt", "application/vnd.ms-powerpoint"},
    //{".prop", "text/plain"},
    //{".rar", "application/x-rar-compressed"},
    //{".rc", "text/plain"},
    //{".rmvb", "audio/x-pn-realaudio"},
    //{".rtf", "application/rtf"},
    //{".sh", "text/plain"},
    //{".tar", "application/x-tar"},
    //{".tgz", "application/x-compressed"},
    //{".txt", "text/plain"},
    //{".wav", "audio/x-wav"},
    //{".wma", "audio/x-ms-wma"},
    //{".wmv", "audio/x-ms-wmv"},
    //{".wps", "application/vnd.ms-works"},
    ////{".xml", "text/xml"},
    //{".xml", "text/plain"},
    //{".z", "application/x-compress"},
    //{".zip", "application/zip"},
    //{"", "*/*"}
}
