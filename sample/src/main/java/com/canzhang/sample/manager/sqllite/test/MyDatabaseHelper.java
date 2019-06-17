package com.canzhang.sample.manager.sqllite.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * @Description:
 * @Author: canzhang
 * @CreateDate: 2019/6/17 11:28
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    //建表语句
    public static final String CREATE_BOOK = "create table book ("
            + "id integer primary key autoincrement, "
            + "author text, "
            + "price real, "
            + "pages integer, "
            + "name text)";

    /**
     * 版本2 新增类别
     */
    public static final String CREATE_CATEGORY = "create table Category ("
            + "id integer primary key autoincrement, "
            + "category_name text, "
            + "category_code integer)";

    private Context mContext;

    /**
     * @param context
     * @param name    数据库名（创建数据库时候使用的名字）
     * @param factory 允许我们在查询数据的时候返回一个自定义的Cursor，一般都是传入null
     * @param version 当前数据库的版本号，可用于对数据库进行升级操作
     */
    public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//创建数据库、建表(只走一次)
        db.execSQL(CREATE_BOOK);
        //版本2 新增(这样的话，新安装走这个方法，同时创建两张表。)
        db.execSQL(CREATE_CATEGORY);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//升级数据库
        Toast.makeText(mContext, "onUpgrade：" + oldVersion + " to " + newVersion, Toast.LENGTH_SHORT).show();
        //而之前已经安装过app的，已经存在了一张表的用户会走这个方法，在创建一张新的表
        switch (oldVersion) {
            case 1:
                db.execSQL(CREATE_CATEGORY);
            case 2://突然表又要求新增一列，这个时候就可以这么操作（不过要注意建表语句也需要同步更新，因为有可能存在用户直接安装最新版app）
                db.execSQL("alter table Book add column category_id integer");
            default:
        }

        /**
         * 这里请注意一个非常重要的细节，switch中每一个case的最后都是没有使用break的，为什么要这么做呢？
         * 这是为了保证在跨版本升级的时候，每一次的数据库修改都能被全部执行到。
         * 比如用户当前是从第二版程序升级到第三版程序的，那么case 2中的逻辑就会执行。
         * 而如果用户是直接从第一版程序升级到第三版程序的，那么case 1和case 2中的逻辑都会执行。
         * 使用这种方式来维护数据库的升级，不管版本怎样更新，都可以保证数据库的表结构是最新的，而且表中的数据也完全不会丢失了。
         */
    }
}
