package com.canzhang.hb.luckyredpackage.utils;

import android.os.Build;
import android.util.Log;

import com.canzhang.hb.BuildConfig;


public class Logg {

    private static String TAG = "TAG ";

    public static void e(String childTag, String s) {

        if (BuildConfig.DEBUG){
            Log.e(TAG + childTag, s);
        }
    }

    public static void i(String childTag, String s) {
        if (BuildConfig.DEBUG){
            Log.i("TAG  " + childTag, s);
        }
    }
}