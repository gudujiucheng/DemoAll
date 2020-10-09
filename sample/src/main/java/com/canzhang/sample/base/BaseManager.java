package com.canzhang.sample.base;


import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.canzhang.sample.base.bean.ComponentItem;
import com.canzhang.sample.debug.DebugBaseApp;
import com.example.base.base.AppProxy;
import com.component.debugdialog.DebugDialog;

import java.util.List;

public abstract class BaseManager implements IManager {
    public Activity  mActivity;

    public BaseManager() {

    }

    public void showTipsDialog(String msg) {
        DebugDialog.getInstance().show("", msg);
    }

    public void showTipsDialog(String title, String msg) {
        DebugDialog.getInstance().show(title, msg);
    }

    public void log(String msg) {
        Log.e("Test", getClass().getSimpleName() + ":" + msg);
    }


    public void showToast(String msg) {
        Application application = AppProxy.getInstance().getApplication();
        if (application != null) {
            Toast.makeText(application, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        mActivity = activity;
        return null;
    }
}
