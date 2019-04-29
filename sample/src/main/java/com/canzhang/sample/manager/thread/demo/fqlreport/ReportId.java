package com.canzhang.sample.manager.thread.demo.fqlreport;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by liveeili on 2017/7/25.
 */

public class ReportId {
    private static final String SP_KEY = "universal_report_id";
    private static final String VALUE_KEY = "value";
    private final SharedPreferences mSp;
    private long mId;
    private static ReportId sReportId;

    private ReportId(Context context) {
        mSp = context.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE);
        mId = mSp.getLong(VALUE_KEY, 0);
    }

    public static synchronized ReportId instance(Context context) {
        if (sReportId == null) {
            sReportId = new ReportId(context);
        }
        return sReportId;
    }

    public final long get() {
        return mId;
    }

    public final synchronized long getAndIncrement() {
        if (mId == Long.MAX_VALUE) {
            mId = 0;
        } else {
            mId++;
        }
        mSp.edit().putLong(VALUE_KEY, mId).apply();
        return mId;
    }

}
