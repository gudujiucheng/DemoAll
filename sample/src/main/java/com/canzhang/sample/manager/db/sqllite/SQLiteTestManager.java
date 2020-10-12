package com.canzhang.sample.manager.db.sqllite;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.View;

import com.canzhang.sample.INotifyListener;
import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.canzhang.sample.manager.db.sqllite.BRL.db.BRLReportDBManager;
import com.canzhang.sample.manager.db.sqllite.BRL.db.bean.BaseBRLBean;
import com.canzhang.sample.manager.db.sqllite.fql.CommonDispatchThread;
import com.canzhang.sample.manager.db.sqllite.fql.CommonReportInfo;
import com.canzhang.sample.manager.db.sqllite.fql.CommonReportSQLiteOpenHelper;
import com.canzhang.sample.manager.db.sqllite.fql.CommonReportStorage;
import com.canzhang.sample.manager.thread.demo.fqlreport.LogUtils;
import com.example.simple_test_annotations.MarkManager;

import java.util.ArrayList;
import java.util.List;

@MarkManager(value = "sqlite")
public class SQLiteTestManager extends BaseManager {

    private Activity mActivity;


    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        mActivity = activity;
        List<ComponentItem> list = new ArrayList<>();

        /**
         * 基础上报库相关测试代码
         */
        list.add(addBRLData());
        list.add(queryBRLData());
        list.add(deleteBRLData());
        list.add(deleteBRLDataOr());
        list.add(queryBRLDataByIds());



        list.add(switchVersion());
        list.add(fql());
        list.add(addFqlData());


        list.add(createDB());
        list.add(addNewTable());
        list.add(insert());
        list.add(delete());
        list.add(update());
        list.add(query());
        list.add(transaction());



        return list;
    }

    private ComponentItem deleteBRLData() {
        return new ComponentItem("BRL 批量删除数据 in 方式", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long start = System.currentTimeMillis();
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 999; i++) {
                    list.add(i + "");
                }
                new BRLReportDBManager(mActivity).delete(list);
                LogUtils.log("删除完毕 总耗时：" + (System.currentTimeMillis() - start) + "ms");
                showToast("删除完毕");
            }
        });
    }

    private ComponentItem deleteBRLDataOr() {
        return new ComponentItem("BRL 批量删除数据 or 方式", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long start = System.currentTimeMillis();
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 999; i++) {
                    list.add(i + "");
                }
                new BRLReportDBManager(mActivity).deleteOr(list);
                LogUtils.log("删除完毕 总耗时：" + (System.currentTimeMillis() - start) + "ms");
                showToast("删除完毕");
            }
        });
    }

    private ComponentItem queryBRLData() {
        return new ComponentItem("BRL  查询当前基础库数据（all）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long start = System.currentTimeMillis();
                List<BaseBRLBean> all = new BRLReportDBManager(mActivity).getAll();
                StringBuilder builder = new StringBuilder();
                for (BaseBRLBean item : all) {
                    builder.append(item.getId()).append("_");
                }
                LogUtils.log("查询结果 size:" + all.size() + " " + builder.toString() + " 总耗时：" + (System.currentTimeMillis() - start) + "ms");
                showToast("查询完毕");
            }
        });
    }

    private ComponentItem queryBRLDataByIds() {
        return new ComponentItem("BRL  查询当前基础库数据（部分id查询）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long start = System.currentTimeMillis();
                List<String> list = new ArrayList<>();
                for (int i = 100; i < 199; i++) {
                    list.add(i + "");
                }
                LongSparseArray<BaseBRLBean> dataWithIds = new BRLReportDBManager(mActivity).getDataWithIds(list);
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i <dataWithIds.size() ; i++) {
                    BaseBRLBean item = dataWithIds.valueAt(i);
                    builder.append(item.getId()).append("_");
                }
                LogUtils.log("查询结果 size:" + dataWithIds.size() + " " + builder.toString() + " 总耗时：" + (System.currentTimeMillis() - start) + "ms");
                showToast("查询完毕");
            }
        });
    }

    private ComponentItem addBRLData() {
        return new ComponentItem("BRL 向基础库中添加", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long start = System.currentTimeMillis();
                for (int i = 0; i < 1000; i++) {
                    new BRLReportDBManager(mActivity).save(new BaseBRLBean.BaseCRLBeanBuilder()
                            .setId(i)
                            .setData("" + i)
                            .createCRLBean());
                }
                LogUtils.log("存储完毕 总耗时：" + (System.currentTimeMillis() - start) + "ms");
                showToast("存储完毕");

            }
        });
    }

    private ComponentItem switchVersion() {
        final ComponentItem item = new ComponentItem();
        item.name = "切换版本";
        item.listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonReportSQLiteOpenHelper.DATABASE_VERSION = CommonReportSQLiteOpenHelper.DATABASE_VERSION == 1 ? 2 : 1;
                item.name = "当前版本：" + CommonReportSQLiteOpenHelper.DATABASE_VERSION;
                if (mActivity instanceof INotifyListener) {
                    ((INotifyListener) mActivity).onNotify();
                }
            }
        };
        return item;
    }

    private ComponentItem fql() {
        return new ComponentItem("查询fql表", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonDispatchThread().reportOldAndSchedule(3);
            }
        });
    }

    private ComponentItem addFqlData() {
        return new ComponentItem("添加fql数据", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonReportStorage storage = new CommonReportStorage(mActivity);
                for (int i = 0; i < 10; i++) {
                    CommonReportInfo info = new CommonReportInfo();
                    info.setData("data" + i);
//                    info.setId((long) i);
                    info.setType(i);
                    info.setReportId("xxxxxx report id " + i);
                    storage.save(info);
                }

                Log.e("Test", "添加数据完毕");

            }
        });
    }


    private ComponentItem createDB() {

        return new ComponentItem("创建数据库、建表", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(mActivity, "BookStore.db", null, 1);
                dbHelper.getWritableDatabase();
            }
        });
    }

    private ComponentItem addNewTable() {

        return new ComponentItem("新增表", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(mActivity, "BookStore.db", null, 2);//升级到2
                dbHelper.getWritableDatabase();
            }
        });
    }

    private ComponentItem insert() {

        return new ComponentItem("表内添加数据(两条)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(mActivity, "BookStore.db", null, 1);//升级到2
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //没有赋值id，因为建表语句里面设置了自增id
                // 开始组装第一条数据
                values.put("name", "The Da Vinci Code");
                values.put("author", "Dan Brown");
                values.put("pages", 454);
                values.put("price", 16.96);
                db.insert("Book", null, values); // 插入第一条数据
                values.clear();
                // 开始组装第二条数据
                values.put("name", "The Lost Symbol");
                values.put("author", "Dan Brown");
                values.put("pages", 510);
                values.put("price", 19.95);
                db.insert("Book", null, values); // 插入第二条数据
            }
        });
    }

    private ComponentItem update() {

        return new ComponentItem("更新数据", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(mActivity, "BookStore.db", null, 2);//升级到2
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price", 10.99);
                //将名字是The Da Vinci Code的这本书的价格改成10.99
                db.update("Book", values, "name = ?", new String[]{"The Da Vinci Code"});
            }
        });
    }

    private ComponentItem delete() {

        return new ComponentItem("删除数据", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(mActivity, "BookStore.db", null, 2);//升级到2
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Book", "pages > ?", new String[]{"500"});
            }
        });
    }

    private ComponentItem query() {

        return new ComponentItem("查询数据", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(mActivity, "BookStore.db", null, 1);//升级到2
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                // 查询Book表中所有的数据(后面的参数全部为null。这就表示希望查询这张表中的所有数据)
                Cursor cursor = db.query("Book", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        // 遍历Cursor对象，取出数据并打印
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));

                        log("book name is " + name);
                        log("book author is " + author);
                        log("book pages is " + pages);
                        log("book price is " + price);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        });
    }

    /**
     * 事务的特性可以保证让某一系列的操作要么全部完成，要么一个都不会完成。
     *
     * @return
     */
    private ComponentItem transaction() {

        //删除旧数据和添加新数据的操作必须一起完成，否则就还要继续保留原来的旧数据。
        return new ComponentItem("开启事务", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(mActivity, "BookStore.db", null, 2);//升级到2
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.beginTransaction(); // 开启事务
                try {
                    db.delete("Book", null, null);
                    if (true) {
                        // 在这里手动抛出一个异常，让事务失败
                        throw new NullPointerException();
                    }
                    ContentValues values = new ContentValues();
                    values.put("name", "Game of Thrones");
                    values.put("author", "George Martin");
                    values.put("pages", 720);
                    values.put("price", 20.85);
                    db.insert("Book", null, values);
                    db.setTransactionSuccessful(); // 事务已经执行成功
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction(); // 结束事务
                }
            }
        });
    }


}
