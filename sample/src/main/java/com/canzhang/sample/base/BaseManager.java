package com.canzhang.sample.base;


import android.util.Log;
import android.widget.Toast;

import com.example.base.base.AppProxy;
import com.lexinfintech.component.debugdialog.DebugDialog;

public abstract class BaseManager implements IManager {

    public BaseManager() {

    }
    public void showTipsDialog( String msg) {
        DebugDialog.getInstance().show("", msg);
    }
    public void showTipsDialog(String title, String msg) {
        DebugDialog.getInstance().show(title, msg);
    }

    public void log(String msg) {
        Log.e("Test", getClass().getSimpleName() + ":" + msg);
    }


    public void showToast(String msg){
        Toast.makeText(AppProxy.getInstance().getApplication(),msg,Toast.LENGTH_SHORT).show();
    }


}
