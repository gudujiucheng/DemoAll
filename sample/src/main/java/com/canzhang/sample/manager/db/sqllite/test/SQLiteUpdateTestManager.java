package com.canzhang.sample.manager.db.sqllite.test;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.example.simple_test_annotations.MarkManager;

import java.util.ArrayList;
import java.util.List;

@MarkManager(value = "sqlite 升级专项测试")
public class SQLiteUpdateTestManager extends BaseManager {

    private Activity mActivity;


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        mActivity = activity;
        List<ComponentItem> list = new ArrayList<>();
        list.add(insert());
        list.add(addNewColumn());
        list.add(query());
        list.add(query02());


        return list;
    }

    private ComponentItem insert() {

        return new ComponentItem("表内添加数据（不含类别信息 1）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestDatabaseHelper dbHelper = new TestDatabaseHelper(mActivity, "BookStore.db", null, 1);
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


    private ComponentItem addNewColumn() {

        return new ComponentItem("升级到版本：2（添加列 ）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestDatabaseHelper dbHelper = new TestDatabaseHelper(mActivity, "BookStore.db", null, 2);//升级到2
                dbHelper.getWritableDatabase();
            }
        });
    }


    private ComponentItem query() {

        return new ComponentItem("查询数据（版本1）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestDatabaseHelper dbHelper = new TestDatabaseHelper(mActivity, "BookStore.db", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                // 查询Book表中所有的数据(后面的参数全部为null。这就表示希望查询这张表中的所有数据)
                Cursor cursor = db.query("book", null, null, null, null, null, null);
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


    private ComponentItem query02() {

        return new ComponentItem("查询数据（版本2）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestDatabaseHelper dbHelper = new TestDatabaseHelper(mActivity, "BookStore.db", null, 2);
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


    private ComponentItem queryWithSql() {

        return new ComponentItem("查询数据（版本2）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestDatabaseHelper dbHelper = new TestDatabaseHelper(mActivity, "BookStore.db", null, 2);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                // 查询Book表中所有的数据(后面的参数全部为null。这就表示希望查询这张表中的所有数据)
                Cursor cursor = db.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '" + "Book" + "'", null);
//                Cursor cursor = db.query("Book", null, null, null, null, null, null);
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
                } else {
                    showToast("木有数据");
                }
                cursor.close();
            }
        });
    }


}
