package com.canzhang.sample.manager.android_11;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtil {

    private static volatile Handler mHandler;
    private static volatile Handler mSingleHandler;
    private static volatile ExecutorService mExecutor;
    private static volatile Handler hookManagerHandler;

    static {
        mHandler = new Handler(Looper.getMainLooper());

        HandlerThread handlerThread = new HandlerThread("replay_singleThread");
        handlerThread.start();
        mSingleHandler = new Handler(handlerThread.getLooper());

        // 修改线程创建策略，防止创建的线程过多，导致线程数超标的OOM。
        // 建立线程池，默认线程为1，当线程不足自动增加。暂时将最大线程数定位50观察效果
        mExecutor = Executors.newCachedThreadPool();
    }

    public static void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }

    public static void runOnUiThread(Runnable runnable, long delay) {
        mHandler.postDelayed(runnable, delay);
    }

    //提高执行优先级
    public static void runOnUiThreadImmediately(Runnable runnable) {
        mHandler.postAtFrontOfQueue(runnable);
    }

    public static void removeOnUiThread(Runnable runnable) {
        mHandler.removeCallbacks(runnable);
    }

    public static void runQueue(Runnable runnable) {
        mSingleHandler.post(runnable);
    }

    public static void runQueue(Runnable runnable, long delay) {
        mSingleHandler.postDelayed(runnable, delay);
    }

    public static void removeQueue(Runnable runnable) {
        mSingleHandler.removeCallbacks(runnable);
    }

    public static void run(Runnable r) {
        mExecutor.execute(r);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void runHookQueue(Runnable runnable) {
        // 出现多次调用情况，为了防止多次调用runnable,造成多个死循环，先进行remove操作
        runHookQueue(runnable, 0);
    }

    public static synchronized void runHookQueue(Runnable runnable, long delay) {
        // 出现多次调用情况，为了防止多次调用runnable，造成多个死循环，先进行remove操作
        if (hookManagerHandler == null) {
            // ThreadUtil HandlerThread 处理任务太多导致，切回页面时存在Hook失败，所以此线程单独Hook使用
            HandlerThread hookManagerThread = new HandlerThread("replay_HookManagerThread");
            hookManagerThread.start();
            hookManagerHandler = new Handler(hookManagerThread.getLooper());
        }
        hookManagerHandler.removeCallbacks(runnable);
        hookManagerHandler.postDelayed(runnable, delay);
    }

    public static void removeHookQueue() {
        hookManagerHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 原因：多次出现Runnable在回放结束之后，依旧会偏移时间执行runnable。
     * 处理办法：回放结束后，所有的runnable事件进行释放
     */
    public static void releaseSingleHandlerQueue() {
        if (mSingleHandler != null) {
            mSingleHandler.removeCallbacksAndMessages(null);
        }
    }

    public static Handler getMainHandler() {
        return mHandler;
    }
}
