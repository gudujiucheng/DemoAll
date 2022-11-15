package com.canzhang.sample.manager.log.service;



import static com.canzhang.sample.manager.log.LogInfoManager.LOG_LIST;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.canzhang.sample.manager.log.LogInfoManager;
import com.canzhang.sample.manager.log.LogLine;

import java.util.ArrayList;


public class LogInfoService extends Service {
    private static final Messenger mMessenger = new Messenger(new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == LogInfoManager.MESSAGE_PUBLISH_LOG) {
                Bundle bundle = msg.getData();
                if (bundle != null) {
                    //避免找不到类 参见：https://blog.csdn.net/bettarwang/article/details/45315091
                    bundle.setClassLoader(getClass().getClassLoader());
                    ArrayList<LogLine> list = bundle.getParcelableArrayList(LOG_LIST);
                    if(list==null){
                        return;
                    }
                    for (LogLine logLine : list) {
                        Log.println(logLine.getLogLevelWithDefault(),logLine.getTag()+"_MATI", logLine.getTimestamp()+"-"+logLine.getLogOutput());
                    }
                }

            }
            super.handleMessage(msg);
        }
    });

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
