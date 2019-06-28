package com.canzhang.sample.manager.sqllite.fql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteFullException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.canzhang.sample.manager.sqllite.fql.CommonReportSQLiteOpenHelper.COLUMN_DATA;
import static com.canzhang.sample.manager.sqllite.fql.CommonReportSQLiteOpenHelper.COLUMN_REPORT_ID;
import static com.canzhang.sample.manager.sqllite.fql.CommonReportSQLiteOpenHelper.COLUMN_TYPE;


public class CommonReportStorage {

    private CommonReportSQLiteOpenHelper mDatabaseSupplier;

    public CommonReportStorage(Context context) {
        this.mDatabaseSupplier = CommonReportSQLiteOpenHelper.getInstance(context);
    }

    public boolean save(CommonReportInfo reportInfo) {
        return save(reportInfo, true);
    }

    public boolean save(CommonReportInfo reportInfo, boolean allowRetryWhenFull) {
        SQLiteDatabase database = mDatabaseSupplier.getDatabase();
        if (database == null || reportInfo == null) {
            return false;
        }
        String sql;
        if (CommonReportSQLiteOpenHelper.DATABASE_VERSION == 1) {//用来方便测试切换，不用来回装包
            sql = "INSERT OR REPLACE INTO " + CommonReportSQLiteOpenHelper.TABLE_NAME + " VALUES (?,?,?);";
        } else {//新增一列之后的查询语句
            sql = "INSERT OR REPLACE INTO " + CommonReportSQLiteOpenHelper.TABLE_NAME + " (" + COLUMN_REPORT_ID + "," + COLUMN_TYPE + "," + COLUMN_DATA + ") " + " VALUES (?,?,?);";
        }

        SQLiteStatement statement = null;
        try {
            statement = database.compileStatement(sql);
            statement.clearBindings();
            if (CommonReportSQLiteOpenHelper.DATABASE_VERSION == 1) {
                statement.bindLong(1, reportInfo.getId());
                statement.bindLong(2, reportInfo.getType());
                statement.bindString(3, reportInfo.getData());
            } else {//插入的数据不同
                statement.bindString(1, reportInfo.getReportId());
                statement.bindLong(2, reportInfo.getType());
                statement.bindString(3, reportInfo.getData());
            }

            statement.execute();
            Log.e("Test", "添加数据:" + reportInfo.toString());
            return true;
        } catch (Exception e) {
            Log.e("Test", "我擦有异常" + e.getMessage());
            if (e instanceof SQLiteFullException) {
                if (allowRetryWhenFull && deleteAll()) {
                    return save(reportInfo, false);
                }
            }
            return false;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public List<CommonReportInfo> get(long id) {
        SQLiteDatabase database = mDatabaseSupplier.getDatabase();
        if (database == null) {
            return null;
        }

        Cursor c = database.query(CommonReportSQLiteOpenHelper.TABLE_NAME,
                new String[]{CommonReportSQLiteOpenHelper.COLUMN_ID,
                        COLUMN_TYPE,
                        COLUMN_DATA},
                CommonReportSQLiteOpenHelper.COLUMN_ID + "=?",
                new String[]{"" + id},
                null, null, null);
        if (c == null) {
            return null;
        }
        try {
            List<CommonReportInfo> resultList = new ArrayList<>();
            while (c.moveToNext()) {
                CommonReportInfo reportInfo = new CommonReportInfo();
                reportInfo.setId(c.getLong(c.getColumnIndex(CommonReportSQLiteOpenHelper.COLUMN_ID)));
                reportInfo.setType(c.getInt(c.getColumnIndex(COLUMN_TYPE)));
                reportInfo.setData(c.getString(c.getColumnIndex(COLUMN_DATA)));
                resultList.add(reportInfo);
            }
            return resultList;
        } catch (Exception e) {
            return null;
        } finally {
            c.close();
        }
    }

    public List<CommonReportInfo> getAll() {
        SQLiteDatabase database = mDatabaseSupplier.getDatabase();
        if (database == null) {
            return null;
        }

        List<CommonReportInfo> result = new ArrayList<>();
        Cursor c = database.query(CommonReportSQLiteOpenHelper.TABLE_NAME,
                new String[]{CommonReportSQLiteOpenHelper.COLUMN_ID,
                        COLUMN_TYPE,
                        COLUMN_DATA,
                },
                null, null, null, null, null);
        if (c == null) {
            return null;
        }
        try {
            while (c.moveToNext()) {
                CommonReportInfo reportInfo = new CommonReportInfo();
                reportInfo.setId(c.getLong(c.getColumnIndex(CommonReportSQLiteOpenHelper.COLUMN_ID)));
                reportInfo.setType(c.getInt(c.getColumnIndex(COLUMN_TYPE)));
                reportInfo.setData(c.getString(c.getColumnIndex(COLUMN_DATA)));
                result.add(reportInfo);
            }
            return result;
        } catch (Exception e) {
            return null;
        } finally {
            c.close();
        }
    }

    public boolean delete(long id) {
        SQLiteDatabase database = mDatabaseSupplier.getDatabase();
        if (database == null) {
            return false;
        }

        int count = 0;
        try {
            count = database.delete(CommonReportSQLiteOpenHelper.TABLE_NAME,
                    CommonReportSQLiteOpenHelper.COLUMN_ID + "=?",
                    new String[]{"" + id});
        } catch (Exception e) {
            return false;
        }
        return count == 1;
    }

    public boolean deleteAll() {
        SQLiteDatabase database = mDatabaseSupplier.getDatabase();
        if (database == null) {
            return false;
        }

        try {
            database.execSQL("DELETE FROM " + CommonReportSQLiteOpenHelper.TABLE_NAME);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
