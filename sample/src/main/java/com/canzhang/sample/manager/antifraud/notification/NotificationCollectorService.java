package com.canzhang.sample.manager.antifraud.notification;

import android.app.Notification;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * @Description: 监听通知状态
 * @Author: canzhang
 * @CreateDate: 2019/9/30 11:42
 */
public class NotificationCollectorService extends NotificationListenerService {

    private final String TAG = "NotificationTest";

    //来通知时的调用
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Notification notification = sbn.getNotification();
        if (notification == null) {
            return;
        }

        Bundle extras = notification.extras;
        String content = "";
        if (extras != null) {
            // 获取通知标题
            String title = extras.getString(Notification.EXTRA_TITLE, "");
            // 获取通知内容
            content = extras.getString(Notification.EXTRA_TEXT, "");
            Log.i(TAG, "包名：" + sbn.getPackageName() + "标题:" + title + "内容:" + content+" id:"+sbn.getId());

            if (content != null && content.contains("验证码")) {//移除通知栏（这个可以实现静默移除效果  还挺好用的 ）
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cancelNotification(sbn.getKey());
                } else {
                    cancelNotification(sbn.getPackageName(), sbn.getTag(), sbn.getId());
                }
                Log.i(TAG, "匹配移除规则  移除 内容------->>>>>>包名：" + sbn.getPackageName() + "标题:" + title + "内容:" + content+" id:"+sbn.getId());
            }

        }
        switch (sbn.getPackageName()) {
            case "com.tencent.mm":
                Log.i(TAG, "微信" + content);
                break;
            case "com.android.mms":
                Log.i(TAG, "短信信" + content);
                break;
            case "com.tencent.mqq":
                Log.i(TAG, "qq" + content);
                break;
            case "com.tencent.tim":
                Log.i(TAG, "tim" + content);
                break;
            case "com.android.incallui":
                Log.i(TAG, "电话" + content);
                break;
        }

    }

    //删除通知时的调用
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Notification notification = sbn.getNotification();
        if (notification == null) {
            return;
        }
        Bundle extras = notification.extras;
        String content = "";
        if (extras != null) {
            // 获取通知标题
            String title = extras.getString(Notification.EXTRA_TITLE, "");
            // 获取通知内容
            content = extras.getString(Notification.EXTRA_TEXT, "");
            Log.i(TAG, "删包名：" + sbn.getPackageName() + "标题:" + title + "内容:" + content);
        }
        switch (sbn.getPackageName()) {
            case "com.tencent.mm":
                Log.i(TAG, "删微信" + content);
                break;
            case "com.android.mms":
                Log.i(TAG, "删短信" + content);
                break;
            case "com.tencent.mqq":
                Log.i(TAG, "删qq" + content);
                break;
            case "com.tencent.tim":
                Log.i(TAG, "删tim" + content);
                break;
            case "com.android.incallui":
                Log.i(TAG, "删电话" + content);
                break;
        }

    }

}