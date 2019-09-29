package com.canzhang.sample.manager.block.githup_test_refactor;

import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;

/**
 * @Description:
 * @Author: canzhang
 * @CreateDate: 2019/9/29 15:10
 */

public final class BlockCanaryInternals {

    LooperMonitor mMonitor;
    StackSampler mStackSampler;
    private int mBlockThreshold;//卡顿阈值
    private int mDumpInterval;//采样间隔


    private static BlockCanaryInternals sInstance;


    private BlockCanaryInternals() {

        mStackSampler = new StackSampler(
                Looper.getMainLooper().getThread(),
                mDumpInterval);

        setMonitor(new LooperMonitor(new LooperMonitor.BlockListener() {

            @Override
            public void onBlockEvent(long realTimeStart, long realTimeEnd,
                                     long threadTimeStart, long threadTimeEnd) {
                // Get recent thread-stack entries and cpu usage
                ArrayList<String> threadStackEntries = mStackSampler
                        .getThreadStackEntries(realTimeStart, realTimeEnd);
                Log.e("Test","检测到卡顿，打印卡顿堆栈信息 size"+ threadStackEntries.size());
                for (String s: threadStackEntries) {
                    Log.e("Test","卡顿堆栈："+s);
                }

            }
        }, mBlockThreshold, false));//TODO 可以配置是否关闭

    }

    /**
     * Get BlockCanaryInternals singleton
     *
     * @return BlockCanaryInternals instance
     */
    static BlockCanaryInternals getInstance() {
        if (sInstance == null) {
            synchronized (BlockCanaryInternals.class) {
                if (sInstance == null) {
                    sInstance = new BlockCanaryInternals();
                }
            }
        }
        return sInstance;
    }


    private void setMonitor(LooperMonitor looperPrinter) {
        mMonitor = looperPrinter;
    }

    /**
     * 采样延迟事件
     * @return
     */
    long getSampleDelay() {
        return (long) (mBlockThreshold * 0.8f);
    }
}
