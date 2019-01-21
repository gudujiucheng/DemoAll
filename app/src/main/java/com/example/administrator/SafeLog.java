package com.example.administrator;

import android.util.Log;

public class SafeLog {
    public static void log(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void log(String msg) {
        Log.e("Test", msg);
    }
}
