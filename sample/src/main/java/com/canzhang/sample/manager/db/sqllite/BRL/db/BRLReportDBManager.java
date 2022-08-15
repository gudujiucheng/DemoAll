package com.canzhang.sample.manager.db.sqllite.BRL.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteFullException;
import android.database.sqlite.SQLiteStatement;
import android.util.LongSparseArray;

import com.canzhang.sample.manager.db.sqllite.BRL.db.bean.BaseBRLBean;
import com.canzhang.sample.manager.thread.demo.fqlreport.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by owenli on 2018/6/27.
 */
public class BRLReportDBManager {

    private BRLSQLiteOpenHelper mDatabaseSupplier;


    public BRLReportDBManager(Context context) {
        this.mDatabaseSupplier = BRLSQLiteOpenHelper.getInstance(context);
    }

    public boolean save(BaseBRLBean reportInfo) {
        return save(reportInfo, true);
    }

    public boolean save(long id, int type, String data, boolean allowRetryWhenFull) {
        SQLiteDatabase database = mDatabaseSupplier.getDatabase();
        if (database == null) {
            return false;
        }
        LogUtils.logD("------------------------------------>>>save to db " + "id:" + id + " type:" + " data:" + data);
        String sql = "INSERT OR REPLACE INTO " + BRLSQLiteOpenHelper.TABLE_NAME + " VALUES (?,?,?);";
        SQLiteStatement statement = null;
        try {
            statement = database.compileStatement(sql);
            statement.clearBindings();
            statement.bindLong(1, id);
            statement.bindLong(2, type);
            statement.bindString(3, data);
            statement.execute();
            return true;
        } catch (Exception e) {
            LogUtils.logE("------------------------------------>>>save reportInfo has exception:" + e.getMessage());

            if (e instanceof SQLiteFullException) {
                if (allowRetryWhenFull && deleteAll()) {
                    return save(id, type, data, false);
                }

            }
            return false;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public boolean save(BaseBRLBean reportInfo, boolean allowRetryWhenFull) {
        if (reportInfo == null) {
            return false;
        }
        return save(reportInfo.getId(), reportInfo.getType(), reportInfo.getData(), allowRetryWhenFull);
    }

    public List<BaseBRLBean> get(long id) {
        SQLiteDatabase database = mDatabaseSupplier.getDatabase();
        if (database == null) {
            return null;
        }
        HashMap<String, List<String>> map  = new HashMap<>();
        Cursor c = database.query(BRLSQLiteOpenHelper.TABLE_NAME,
                new String[]{BRLSQLiteOpenHelper.COLUMN_ID,
                        BRLSQLiteOpenHelper.COLUMN_TYPE,
                        BRLSQLiteOpenHelper.COLUMN_DATA},
                BRLSQLiteOpenHelper.COLUMN_ID + "=?",
                new String[]{"" + id},
                null, null, null);
        if (c == null) {
            return null;
        }
        try {
            List<BaseBRLBean> resultList = new ArrayList<>();
            while (c.moveToNext()) {
                BaseBRLBean reportInfo = new BaseBRLBean.BaseCRLBeanBuilder()
                        .setId(c.getLong(c.getColumnIndex(BRLSQLiteOpenHelper.COLUMN_ID)))
                        .setType(c.getInt(c.getColumnIndex(BRLSQLiteOpenHelper.COLUMN_TYPE)))
                        .setData(c.getString(c.getColumnIndex(BRLSQLiteOpenHelper.COLUMN_DATA)))
                        .createCRLBean();
                resultList.add(reportInfo);
                LogUtils.logD("get with id:" + id + "reportInfo: " + reportInfo.toString());
            }
            return resultList;
        } catch (Exception e) {
            LogUtils.logE("------------------------------------>>>get id：" + id + " has exception:" + e.getMessage());
            return null;
        } finally {
            c.close();
        }
    }


    public List<BaseBRLBean> getAll() {
        SQLiteDatabase database = mDatabaseSupplier.getDatabase();
        if (database == null) {
            return null;
        }

        List<BaseBRLBean> result = new ArrayList<>();
        Cursor c = database.query(BRLSQLiteOpenHelper.TABLE_NAME,
                new String[]{BRLSQLiteOpenHelper.COLUMN_ID,
                        BRLSQLiteOpenHelper.COLUMN_TYPE,
                        BRLSQLiteOpenHelper.COLUMN_DATA,
                },
                null, null, null, null, null);
        if (c == null) {
            return null;
        }
        try {
            while (c.moveToNext()) {
                BaseBRLBean reportInfo = new BaseBRLBean.BaseCRLBeanBuilder()
                        .setId(c.getLong(c.getColumnIndex(BRLSQLiteOpenHelper.COLUMN_ID)))
                        .setNeedSave(false)
                        .setRealTimeReport(true)
                        .setData(c.getString(c.getColumnIndex(BRLSQLiteOpenHelper.COLUMN_DATA)))
                        .setType(c.getInt(c.getColumnIndex(BRLSQLiteOpenHelper.COLUMN_TYPE)))
                        .createCRLBean();
                result.add(reportInfo);
            }
            return result;
        } catch (Exception e) {
            return null;
        } finally {
            c.close();
        }
    }


    /**
     * 读取区间数据(这里要限制区间，防止不同线程读取同一份数据)
     *
     * @param startId 开始id
     * @param endId   结束id
     * @param limit   每次取出的数量
     * @return
     */
    public LongSparseArray<BaseBRLBean> getDataWithRegionId(int limit, long startId, long endId) {
        return getDataFromDb(limit, BRLSQLiteOpenHelper.COLUMN_ID + " between ? and ?", new String[]{startId + "", endId + ""});
    }

    /**
     * 读取数据
     *
     * @return
     */
    public LongSparseArray<BaseBRLBean> getDataWithIds(List<String> idList) {
        if (idList == null || idList.size() == 0) {
            return null;
        }
        String[] ids = new String[idList.size()];

        return getDataFromDb(0, BRLSQLiteOpenHelper.COLUMN_ID + " in (+" + makePlaceholders(idList.size()) + ")", idList.toArray(ids));
    }


    public LongSparseArray<BaseBRLBean> getDataWithLastId(int limit, long lastId) {
        return getDataFromDb(limit, BRLSQLiteOpenHelper.COLUMN_ID + "<=?", new String[]{lastId + ""});
    }

    private LongSparseArray<BaseBRLBean> getDataFromDb(int limit, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDatabaseSupplier.getDatabase();
        if (database == null) {
            return null;
        }
        LongSparseArray<BaseBRLBean> dataArr = new LongSparseArray<>(limit);
        Cursor c = database.query(BRLSQLiteOpenHelper.TABLE_NAME,
                new String[]{BRLSQLiteOpenHelper.COLUMN_ID,
                        BRLSQLiteOpenHelper.COLUMN_TYPE,
                        BRLSQLiteOpenHelper.COLUMN_DATA,
                },
                selection, selectionArgs, null, null, null, limit == 0 ? null : String.valueOf(limit));
        if (c == null) {
            return null;
        }
        try {
            while (c.moveToNext()) {

                long id = c.getLong(c.getColumnIndex(BRLSQLiteOpenHelper.COLUMN_ID));
                int type = c.getInt(c.getColumnIndex(BRLSQLiteOpenHelper.COLUMN_TYPE));
                String data = c.getString(c.getColumnIndex(BRLSQLiteOpenHelper.COLUMN_DATA));
                //数据库中取出的数据全默认为非立即上报,切不要备份
                BaseBRLBean bean = new BaseBRLBean.BaseCRLBeanBuilder()
                        .setId(id)
                        .setType(type)
                        .setData(data)
                        .setRealTimeReport(true)
                        .setNeedSave(false)
                        .createCRLBean();

                if (bean != null) {
                    dataArr.put(id, bean);
                }
            }
            LogUtils.logE("get data from db size :" + dataArr.size() + " limit:" + limit);
            return dataArr;
        } catch (Exception e) {
            LogUtils.logE("------------------------------------>>>get data from db  has exception:" + e.getMessage());
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
            count = database.delete(BRLSQLiteOpenHelper.TABLE_NAME,
                    BRLSQLiteOpenHelper.COLUMN_ID + "=?",
                    new String[]{"" + id});
            LogUtils.logD("------------------------------------>>>delete with id:" + id + " count:" + count);

        } catch (Exception e) {
            LogUtils.logE("------------------------------------>>>delete id：" + id + " has exception:" + e.getMessage());
            return false;
        }
        return count == 1;
    }


    public boolean delete(List<String> idList) {//FIXME 注意不能一次太多
        if (idList == null || idList.size() == 0) {
            return false;
        }
        SQLiteDatabase database = mDatabaseSupplier.getDatabase();
        if (database == null) {
            return false;
        }
        int count = 0;
        try {
            String[] ids = new String[idList.size()];
            count = database.delete(BRLSQLiteOpenHelper.TABLE_NAME,
                    BRLSQLiteOpenHelper.COLUMN_ID + " in (+" + makePlaceholders(idList.size()) + ")",
                    idList.toArray(ids));
            LogUtils.logD("------------------------------------>>>delete  count:" + count);

        } catch (Exception e) {
            LogUtils.logE("------------------------------------>>>delete idList has exception:" + e.getMessage());
            return false;
        }
        return count == 1;
    }

    public void deleteOr(List<String> list) {

        if (list != null && list.size() > 0) {
            SQLiteDatabase database = mDatabaseSupplier.getDatabase();
            if (database == null) {
                return;
            }
            try {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {
                    sb.append(" or id='" + list.get(i) + "'");

                }
                String strWhere = sb.toString().substring(3, sb.toString().length());
                String sqlStr = "delete  from " + BRLSQLiteOpenHelper.TABLE_NAME + "  where " + strWhere;
                database.execSQL(sqlStr);

            } catch (Throwable throwable) {

                LogUtils.log("异常：" + throwable.getMessage());
            }
        }

    }


    private String makePlaceholders(int len) {
        if (len < 1) {
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }


    public boolean deleteAll() {
        SQLiteDatabase database = mDatabaseSupplier.getDatabase();
        if (database == null) {
            return false;
        }
        LogUtils.logD("deleteAll");
        try {
            database.execSQL("DELETE FROM " + BRLSQLiteOpenHelper.TABLE_NAME);
        } catch (Exception e) {
            LogUtils.logE("------------------------------------>>>deleteAll data from table has exception:" + e.getMessage());
            return false;
        }
        return true;
    }

}
