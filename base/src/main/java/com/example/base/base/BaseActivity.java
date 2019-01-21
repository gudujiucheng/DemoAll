package com.example.base.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Test",this.getClass().getSimpleName());
    }


    public  void log(String msg){
        Log.e(this.getClass().getSimpleName(),msg);
    }
}
