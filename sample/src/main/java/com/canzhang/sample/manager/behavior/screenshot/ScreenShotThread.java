package com.canzhang.sample.manager.behavior.screenshot;

import android.os.Handler;
import android.os.HandlerThread;

public class ScreenShotThread {

    private static Handler childHandler;
    private static HandlerThread handlerThread;
    private static ScreenShotThread instance;

    public static ScreenShotThread getInstance() {
        if (instance == null) {
            synchronized (ScreenShotThread.class) {
                if (instance == null) {
                    instance = new ScreenShotThread();
                }
            }
        }
        return instance;
    }

    public ScreenShotThread(){
        handlerThread = new HandlerThread("ScreenShot");
        //必须先开启线程
        handlerThread.start();
        //子线程Handler
        childHandler = new Handler(handlerThread.getLooper());
    }

    public Handler getChildHandler(){
        return childHandler;
    }
}
