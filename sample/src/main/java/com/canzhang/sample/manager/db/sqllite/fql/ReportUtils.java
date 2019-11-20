package com.canzhang.sample.manager.db.sqllite.fql;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;


import java.util.UUID;

/**
 * @Description: 上报工具类
 * @Author: canzhang
 * @CreateDate: 2019/5/22 17:14
 */
public class ReportUtils {
    private static final String EVENT_ID = "event_id";
    private static final String CHANNEL = "lexin_channel";


    /**
     * 是否是落地推广页
     *
     * @param url
     * @return
     */
    public static void checkIsAdUrl(String url, OnLoadAdUrlCallBack checkUrlCallBack) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        try {
            Uri parse = Uri.parse(url);
            String channel = parse.getQueryParameter(CHANNEL);
            String eventId = parse.getQueryParameter(EVENT_ID);
            if (!TextUtils.isEmpty(channel) && !TextUtils.isEmpty(eventId)) {
                if (checkUrlCallBack != null) {
                    checkUrlCallBack.callBack(channel, eventId);
                }

            }
        } catch (Throwable e) {

        }
    }


    public interface OnLoadAdUrlCallBack {
        void callBack(@NonNull String channel, @NonNull String eventId);
    }




    /**
     * 生成唯一上报id
     *
     * @return
     */
    public static String getReportId() {
        return UUID.randomUUID().toString();
    }

}
