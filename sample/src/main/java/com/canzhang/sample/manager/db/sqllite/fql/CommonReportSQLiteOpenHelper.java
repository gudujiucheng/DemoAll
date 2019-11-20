package com.canzhang.sample.manager.db.sqllite.fql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class CommonReportSQLiteOpenHelper extends SQLiteOpenHelper {


    private static CommonReportSQLiteOpenHelper instance;

    static final String DB_NAME = "Report.db";
    public static int DATABASE_VERSION = 2;

    static final long DEFAULT_DB_SIZE = 10 * 1024 * 1024L;//10mb
    private long mMaximumDatabaseSize;
    private Context mContext;
    private SQLiteDatabase mDb;

    static final String TABLE_NAME = "Report";
    static final String COLUMN_ID = "id";
    static final String COLUMN_TYPE = "type";
    static final String COLUMN_DATA = "data";

    private static final int SLEEP_TIME_MS = 30;
    /**
     * 升级表
     */
    static final String COLUMN_REPORT_ID = "reportId";

    static String s1 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID
            + " INTEGER PRIMARY KEY,"
            + COLUMN_TYPE
            + " INTEGER DEFAULT 0,"
            + COLUMN_DATA
            + " TEXT NOT NULL"
            + ")";
    static String s2 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_REPORT_ID
            + " TEXT DEFAULT NULL UNIQUE,"
            + COLUMN_TYPE
            + " INTEGER DEFAULT 0,"
            + COLUMN_DATA
            + " TEXT NOT NULL"
            + ")";


    public CommonReportSQLiteOpenHelper(Context context) {
        this(context, DEFAULT_DB_SIZE);
    }

    private CommonReportSQLiteOpenHelper(Context context, long maxSize) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        mMaximumDatabaseSize = maxSize > 0 ? maxSize : DEFAULT_DB_SIZE;
    }

    public static CommonReportSQLiteOpenHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (CommonReportSQLiteOpenHelper.class) {
                if (instance == null) {
                    instance = new CommonReportSQLiteOpenHelper(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    /**
     * retrieve sqlite database
     *
     * @return a {@link SQLiteDatabase} instance or null if retrieve fails.
     */
    public SQLiteDatabase getDatabase() {
        ensureDatabase();
        return mDb;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getTableSql());
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("Test", "sqlite oldVersion:" + oldVersion + " newVersion:" + newVersion);

        switch (oldVersion) {
            case 1://数据库升级，新增一列
                Log.e("Test", "add new column reportId");
                //这里有坑  UNIQUE 字符不能随便使用貌似，升级走到这里会抛异常（no such dic）
                db.execSQL("alter table " + TABLE_NAME + " add column " + COLUMN_REPORT_ID + " TEXT DEFAULT NULL UNIQUE");
                setDefaultReportId(db);
            default: {

            }
        }
    }

    private void setDefaultReportId(SQLiteDatabase db) {
        Log.e("Test", "setDefaultReportId");
        if (db == null) {
            return;
        }

        List<Long> result = new ArrayList<>();
        Cursor c = db.query(TABLE_NAME,
                null,
                null, null, null, null, null);
        if (c == null) {
            return;
        }
        try {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndex(COLUMN_ID));
//                String reportID = c.getString(c.getColumnIndex(COLUMN_REPORT_ID));
                result.add(id);
            }
            Log.e("Test", "setDefaultReportId  开始更新数据");
            for (int i = 0; i < result.size(); i++) {
                long id = result.get(i);
                Log.e("Test", "setDefaultReportId  当前更新的id:" + id);
                db.execSQL("update " + TABLE_NAME + " set " + COLUMN_REPORT_ID + " = ? where " + COLUMN_ID + " = ?", new String[]{ReportUtils.getReportId(), id + ""});
            }

            throw new NullPointerException("xxxxx");//测试异常的抛出

        } catch (Exception e) {
            Log.e("Test", "靠靠靠，有异常" + e.getMessage());//FIXME 可以删除db 重新建表
            db.execSQL("drop table if exists " + TABLE_NAME);

        } finally {
            c.close();
        }

    }

    synchronized void ensureDatabase() {
        if (mDb != null && mDb.isOpen()) {
            return;
        }
        // Sometimes retrieving the database fails. We do 2 retries: first without database deletion
        // and then with deletion.
        for (int tries = 0; tries < 2; tries++) {
            try {
                if (tries > 0) {
                    //delete db and recreate
                    Log.e("Test", "删除数据库 tries:" + tries);
                    deleteDB();//FIXME 走了删除数据库操作  为何
                }
                mDb = getWritableDatabase();
                break;
            } catch (SQLiteException e) {
                Log.e("Test", "getWritableDatabase 出现异常:" + e.getMessage());
                e.printStackTrace();
            }
            // Wait before retrying.
            try {
                Thread.sleep(SLEEP_TIME_MS);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        if (mDb == null) {
            return;
        }

        createTableIfNotExists(mDb);
        mDb.setMaximumSize(mMaximumDatabaseSize);
    }

    public synchronized void setMaximumSize(long size) {
        mMaximumDatabaseSize = size;
        if (mDb != null) {
            mDb.setMaximumSize(mMaximumDatabaseSize);
        }
    }

    private boolean deleteDB() {
        Log.e("Test", "删除数据库");
        closeDatabase();
        return mContext.deleteDatabase(DB_NAME);
    }

    public void closeDatabase() {
        if (mDb != null && mDb.isOpen()) {
            mDb.close();
            mDb = null;
        }
    }

    private void createTableIfNotExists(SQLiteDatabase db) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '" + TABLE_NAME + "'", null);
            if (cursor != null && cursor.getCount() > 0) {
                Log.e("Test", "表存在");
                return;
            }
            Log.e("Test", "表不存在");
            db.execSQL(getTableSql());
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    private static String getTableSql() {
        return DATABASE_VERSION == 1 ? s1 : s2;
    }
}
