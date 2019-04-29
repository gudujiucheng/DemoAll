package com.canzhang.sample.manager.thread.demo.fqlreport.bean;

import android.annotation.SuppressLint;


import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class AppEventBean {

    /**
     * 事件ID,约定统一通过UUID生成
     * 必填
     */
    private String mEventId;

    /**
     * 事件标签
     * 非必填
     */

    private String mLabel;

    /**
     * 当前时间（格式化后的）
     * 必填
     * 2018-05-28 11:22:41.5850
     */
    private String mTime;


    /**
     * 时间戳
     */
    private long mTimeStamp;


    /**
     * 事件附属信息
     */
    private JSONObject mExtendInfo;


    @SuppressLint("SimpleDateFormat")
    public AppEventBean(String eventId, String label, JSONObject extendInfo) {
        mTimeStamp = System.currentTimeMillis();

        mEventId = eventId;
        mLabel = label;
        mExtendInfo = extendInfo;
    }

    @SuppressLint("SimpleDateFormat")
    private AppEventBean completeArguments() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        mTime = simpleDateFormat.format(new Date(mTimeStamp));
        return this;
    }

    public JSONObject convert2JSONObject() {
        completeArguments();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("event_id", mEventId);
            jsonObject.put("label", mLabel);
            jsonObject.put("time", mTime);
            if (mExtendInfo != null) {
                jsonObject.put("extend_info", mExtendInfo);
            }
        } catch (Exception e) {

        }
        return jsonObject;
    }

    @Override
    public String toString() {
        return "AppEventBean{" +
                "mEventId='" + mEventId + '\'' +
                ", mLabel='" + mLabel + '\'' +
                ", mTime='" + mTime + '\'' +
                ", mTimeStamp=" + mTimeStamp +
                ", mExtendInfo=" + mExtendInfo +
                '}';
    }
}
