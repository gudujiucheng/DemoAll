package com.canzhang.sample.manager.db.sqllite.BRL.db;


import com.canzhang.sample.manager.thread.demo.fqlreport.LogUtils;

/**
 * Created by liveeili on 2017/7/25.
 */

public class BRLReportId {
    private long mId;

    private BRLReportId() {
        mId = BRLSp.get().getLong(BRLSp.ID, 0);
    }
    private static class SingletonClassInstance {
        private static final BRLReportId instance = new BRLReportId();
    }

    public static BRLReportId getInstance() {
        return SingletonClassInstance.instance;
    }

    public final synchronized long getAndIncrement() {
        if (mId == Long.MAX_VALUE) {
            mId = 0;
        } else {
            mId++;
        }
        BRLSp.get().edit().putLong(BRLSp.ID, mId).apply();
        return mId;
    }


    public synchronized long getCurrentId() {
        LogUtils.logE("getCurrentId ------------------------->>>>>mId："+mId);
        return mId;
    }
}
