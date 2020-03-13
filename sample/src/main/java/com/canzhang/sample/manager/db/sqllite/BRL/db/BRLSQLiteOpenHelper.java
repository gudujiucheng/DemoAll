package com.canzhang.sample.manager.db.sqllite.BRL.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.canzhang.sample.manager.thread.demo.fqlreport.LogUtils;


/**
 * Created by owenli on 2018/6/27.
 */
public class BRLSQLiteOpenHelper extends SQLiteOpenHelper {

    private static BRLSQLiteOpenHelper instance;

    static final String DB_NAME = "CRL_DB.db";
    private static final int DATABASE_VERSION = 1;

    private static final long DEFAULT_DB_SIZE = 10 * 1024 * 1024L;//10mb
    private long mMaximumDatabaseSize;
    private Context mContext;
    private SQLiteDatabase mDb;

    static final String TABLE_NAME = "Report";
    static final String COLUMN_ID = "id";
    static final String COLUMN_TYPE = "type";
    static final String COLUMN_DATA = "data";

    private static final int SLEEP_TIME_MS = 30;

    private static final String STATEMENT_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID
            + " INTEGER PRIMARY KEY,"
            + COLUMN_TYPE
            + " INTEGER DEFAULT 0,"
            + COLUMN_DATA
            + " TEXT NOT NULL"
            + ")";


    private BRLSQLiteOpenHelper(Context context) {
        this(context, DEFAULT_DB_SIZE);
    }

    private BRLSQLiteOpenHelper(Context context, long maxSize) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        mMaximumDatabaseSize = maxSize > 0 ? maxSize : DEFAULT_DB_SIZE;
    }

    public static BRLSQLiteOpenHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (BRLSQLiteOpenHelper.class) {
                if (instance == null) {
                    instance = new BRLSQLiteOpenHelper(context.getApplicationContext());
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
        db.execSQL(STATEMENT_CREATE_TABLE);
        LogUtils.logD("sqlite onCreate");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtils.logD("sqlite oldVersion:" + oldVersion + " newVersion:" + newVersion);
        deleteDB();
        onCreate(db);
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
                    deleteDB();
                }
                mDb = getWritableDatabase();
                break;
            } catch (SQLiteException e) {

                LogUtils.logE("ensureDatabase has exception:" + e.getMessage());
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
            LogUtils.logE("获取可写db 连续失败");
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
                return;
            }
            db.execSQL(STATEMENT_CREATE_TABLE);
        } catch (Exception e) {
            LogUtils.logE("createTableIfNotExists has exception:" + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
