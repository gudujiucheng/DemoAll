package com.canzhang.sample.manager.log;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;


import com.canzhang.sample.manager.log.reader.LogcatReader;
import com.canzhang.sample.manager.log.reader.LogcatReaderLoader;
import com.canzhang.sample.manager.log.service.LogInfoService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by wanglikun on 2018/10/10.
 */

public class LogInfoManager {
    public static final String LOG_LIST = "log_list";
    private static final String TAG = "LogInfoManager";
    public static final int MESSAGE_PUBLISH_LOG = 1001;
    private LogCatchRunnable mLogCatchTask;


    private static class Holder {
        private static LogInfoManager INSTANCE = new LogInfoManager();
    }

    private LogInfoManager() {

    }

    public static LogInfoManager getInstance() {
        return Holder.INSTANCE;
    }

    public void start() {
        if (mLogCatchTask != null) {
            mLogCatchTask.stop();
        }
        mLogCatchTask = new LogCatchRunnable();
        ExecutorUtil.execute(mLogCatchTask);
    }

    public void stop() {
        if (mLogCatchTask != null) {
            mLogCatchTask.stop();
        }
    }


    /**
     * 接收log 的内部Handler
     */
    private static class InternalHandler extends Handler {
        private Messenger mService;
        private boolean isConn;

        private ServiceConnection mConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = new Messenger(service);
                isConn = true;

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
                isConn = false;
            }
        };

        public InternalHandler(Looper looper) {
            super(looper);
            if (Util.getApp() != null) {
                Intent intent = new Intent(Util.getApp(), LogInfoService.class);
                Util.getApp().bindService(intent, mConn, Context.BIND_AUTO_CREATE);
            }
        }


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_PUBLISH_LOG:
                    Message msgFromClient = Message.obtain(null, MESSAGE_PUBLISH_LOG);
                    Bundle bundle=new Bundle();
                    bundle.putParcelableArrayList(LOG_LIST,  (ArrayList<LogLine>) msg.obj);
                    msgFromClient.setData(bundle);
                    if (isConn) {
                        //往服务端发送消息
                        try {
                            mService.send(msgFromClient);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        public void stop() {
            if (Util.getApp() != null && mConn != null) {
                Util.getApp().unbindService(mConn);
            }

        }
    }


    /**
     * 获取日志的内部线程
     */
    private static class LogCatchRunnable implements Runnable {
        private boolean isRunning = true;
        private InternalHandler internalHandler;
        private LogcatReader mReader;
        private int mPid;

        private LogCatchRunnable() {
            internalHandler = new InternalHandler(Looper.getMainLooper());
            mPid = android.os.Process.myPid();
        }

        @Override
        public void run() {
            try {
                LogcatReaderLoader loader = LogcatReaderLoader.create(true);
                mReader = loader.loadReader();

                String line;
                int maxLines = 10000;
                LinkedList<LogLine> initialLines = new LinkedList<>();
                while ((line = mReader.readLine()) != null && isRunning) {
                    LogLine logLine = LogLine.newLogLine(line, false);
                    if (!mReader.readyToRecord()) {//还没准备好，输出的还是开始记录前的缓存日志
                        if (logLine.getProcessId() == mPid) {
                            initialLines.add(logLine);
                        }
                        if (initialLines.size() > maxLines) {
                            initialLines.removeFirst();
                        }
                    } else if (!initialLines.isEmpty()) {//准备好了，但是缓冲区日志不为空。则先把缓冲区日志输出
                        if (logLine.getProcessId() == mPid) {
                            initialLines.add(logLine);
                        }
                        Message message = Message.obtain();
                        message.what = MESSAGE_PUBLISH_LOG;
                        message.obj = new ArrayList<>(initialLines);
                        internalHandler.sendMessage(message);
                        initialLines.clear();
                    } else {
                        // just proceed as normal
                        if (logLine.getProcessId() == mPid) {//准备好了，从记录开始点 ，一行一行的读
                            Message message = Message.obtain();
                            message.what = MESSAGE_PUBLISH_LOG;
                            ArrayList<LogLine> lines = new ArrayList<>();
                            lines.add(logLine);
                            message.obj = lines;
                            internalHandler.sendMessage(message);
                        }
                    }
                }
                mReader.killQuietly();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }

        public void stop() {
            isRunning = false;
            if (internalHandler != null) {
                internalHandler.stop();
            }
        }
    }
}