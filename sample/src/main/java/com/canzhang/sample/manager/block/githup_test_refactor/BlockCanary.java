package com.canzhang.sample.manager.block.githup_test_refactor;

import android.os.Looper;

/**
 * @Description:
 * @Author: canzhang
 * @CreateDate: 2019/9/29 15:40
 */
public class BlockCanary {
    private static final String TAG = "BlockCanary";

    private static BlockCanary sInstance;
    private BlockCanaryInternals mBlockCanaryCore;
    private boolean mMonitorStarted = false;

    private BlockCanary() {
        mBlockCanaryCore = BlockCanaryInternals.getInstance();
    }


    /**
     * Get {@link BlockCanary} singleton.
     *
     * @return {@link BlockCanary} instance
     */
    public static BlockCanary getInstance() {
        if (sInstance == null) {
            synchronized (BlockCanary.class) {
                if (sInstance == null) {
                    sInstance = new BlockCanary();
                }
            }
        }
        return sInstance;
    }

    /**
     * Start monitoring.
     */
    public void start() {
        if (!mMonitorStarted) {
            mMonitorStarted = true;
            Looper.getMainLooper().setMessageLogging(mBlockCanaryCore.mMonitor);
        }
    }

    /**
     * Stop monitoring.
     */
    public void stop() {
        if (mMonitorStarted) {
            mMonitorStarted = false;
            Looper.getMainLooper().setMessageLogging(null);
            mBlockCanaryCore.mStackSampler.stop();
        }
    }


}
