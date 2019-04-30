package com.canzhang.sample.manager.thread.demo.fqlreport;

import android.text.TextUtils;

import com.canzhang.sample.manager.thread.demo.fqlreport.bean.AppEventBean;
import com.canzhang.sample.manager.thread.demo.fqlreport.bean.ReportWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @Description: 数据上报
 * @Author: canzhang
 * @CreateDate: 2019/4/28 15:38
 */
public class AppEvent {

    public static void report(final String eventId, final String label, JSONObject extendInfo,
                              final boolean isImmediately, final boolean isBackup) {

        if (TextUtils.isEmpty(eventId)) {
            return;
        }
        extendInfo = AppEvent.jointExtendInfo(extendInfo, "zc", "66666");
        DispatchThread thread = UniversalReport.getDispatchThread();
        if (thread != null) {
            // 必须实时生成 保证数据有效性
            final AppEventBean eventBean = new AppEventBean(eventId, label, extendInfo);
            LogUtils.log("上报第一步 AppEventBean："+eventBean.toString());
            thread.execute(new Runnable() {
                @Override
                public void run() {
                    offer(eventBean, isImmediately, isBackup);
                }
            });
        }

    }


    private static void offer(AppEventBean bean, boolean isImmediately, boolean isBackup) {
        if (bean == null) {
            return;
        }
        JSONObject object = bean.convert2JSONObject();
        if (object == null) {
            return;
        }

        JSONArray recordList = new JSONArray();
        recordList.put(object);
        ReportWrapper reportWrapper;
        if (isImmediately) {
            reportWrapper = new ReportWrapper(0,recordList);
        } else {
            reportWrapper = new ReportWrapper(0,recordList);
        }
        LogUtils.log("上报第二步 ReportWrapper："+reportWrapper.toString());
        UniversalReport.offer(reportWrapper);
    }


    public static JSONObject jointExtendInfo(JSONObject object, String key, String value) {
        if (object == null) {
            object = new JSONObject();
        }
        if (TextUtils.isEmpty(key)) {
            return object;
        }
        if (TextUtils.isEmpty(value)) {
            value = "";
        }
        try {
            object.put(key, value);
        } catch (Exception e) {

        }
        return object;
    }
}
