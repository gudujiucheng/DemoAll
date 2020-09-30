package com.example.base.utils;

import android.app.Application;
import androidx.annotation.NonNull;
import android.widget.Toast;

import com.example.base.base.AppProxy;

public class ToastUtil {

    public static void toastShort(String tips) {
        Application application = checkAppNonNull();
        Toast.makeText(application, tips, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(String tips) {
        Application application = checkAppNonNull();
        Toast.makeText(application, tips, Toast.LENGTH_LONG).show();
    }

    @NonNull
    private static Application checkAppNonNull() {
        Application application = AppProxy.getInstance().getApplication();
        if (application == null) {
            throw new RuntimeException("you must init AppProxy first");
        }
        return application;
    }

}
