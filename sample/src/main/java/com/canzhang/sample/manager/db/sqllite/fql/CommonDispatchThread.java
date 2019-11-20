package com.canzhang.sample.manager.db.sqllite.fql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;

import com.example.base.base.AppProxy;

import java.util.ArrayList;
import java.util.List;


public class CommonDispatchThread {


    private final Handler mHandler = new Handler(Looper.getMainLooper());


    public void reportOldAndSchedule(long delay) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
                    @Override
                    public boolean queueIdle() {
                        List<CommonReportInfo> commonReportInfos = reportOld();
                        if (commonReportInfos != null) {
                            Log.e("Test", "数据：" + commonReportInfos.size());
                        } else {
                            Log.e("Test", "木有数据");
                        }
                        return false;
                    }
                });
            }
        }, delay);
    }

    private List<CommonReportInfo> reportOld() {
        Log.e("Test", "reportOld");
        SQLiteDatabase database = new CommonReportSQLiteOpenHelper(AppProxy.getInstance().getApplication()).getDatabase();
        if (database == null) {
            return null;
        }

        List<CommonReportInfo> result = new ArrayList<>();
        Cursor c = database.query(CommonReportSQLiteOpenHelper.TABLE_NAME,
                null,
                null, null, null, null, null);
        if (c == null) {
            return null;
        }
        try {
            while (c.moveToNext()) {
                CommonReportInfo reportInfo = new CommonReportInfo();
                reportInfo.setId(c.getLong(c.getColumnIndex(CommonReportSQLiteOpenHelper.COLUMN_ID)));
                if (CommonReportSQLiteOpenHelper.DATABASE_VERSION == 2) {
                    reportInfo.setReportId(c.getString(c.getColumnIndex(CommonReportSQLiteOpenHelper.COLUMN_REPORT_ID)));
                }
                reportInfo.setType(c.getInt(c.getColumnIndex(CommonReportSQLiteOpenHelper.COLUMN_TYPE)));
                reportInfo.setData(c.getString(c.getColumnIndex(CommonReportSQLiteOpenHelper.COLUMN_DATA)));
                result.add(reportInfo);

                Log.e("Test", "读取的数据：" + reportInfo.toString());
            }
            return result;
        } catch (Exception e) {
            Log.e("Test", "不好有异常：" + e.getMessage());
            return null;
        } finally {
            c.close();
        }
    }


}
