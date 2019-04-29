package com.canzhang.sample.manager.thread.demo.fqlreport;

import android.annotation.SuppressLint;
import android.content.Context;

import com.canzhang.sample.manager.thread.demo.fqlreport.bean.ReportWrapper;

/**
 * 通用统一上报类
 * Created by liveeili on 2017/7/25.
 */
public class UniversalReport {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;
    @SuppressLint("StaticFieldLeak")
    private static DispatchThread sDispatchThread;



    /**
     * @param context        APP上下文
     */
    public static void init(Context context) {
        if (sContext == null) {
            sContext = context.getApplicationContext();
        }
        if (sDispatchThread == null) {
            sDispatchThread = new DispatchThread(sContext);
        }

    }


    /**
     * 数据入口
     *
     * @param wrappers 可传单条数据及多条数据，当传多条数据时，缓存及延时判定遵照最后一条数据的参数，
     *                 <p>同时传入的多条数据会保证一同上报，每条数据都会收到成功或失败的回调(有设置ReportCallback的情况下)，
     *                 <p>从数据库恢复上报的数据将丧失回调（APP重启）
     */
    public static void offer(ReportWrapper... wrappers) {
        if (wrappers != null && sDispatchThread != null) {
            sDispatchThread.offer(wrappers);
        }
    }


    public static DispatchThread getDispatchThread() {
        return sDispatchThread;
    }


}
