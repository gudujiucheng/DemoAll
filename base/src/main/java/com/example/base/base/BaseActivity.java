package com.example.base.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void showToast(String tips) {
        Toast.makeText(this, tips, Toast.LENGTH_SHORT).show();
    }

    public void log(String msg) {
        Log.e("ZC:"+this.getClass().getSimpleName(), msg);
    }
}
