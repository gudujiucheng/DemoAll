package com.canzhang.sample.manager.statusbar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.canzhang.sample.R;
import com.jaeger.library.StatusBarUtil;


public class FullscreenActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
//        StatusBarUtil.setColor(this, getResources().getColor(android.R.color.transparent));
    }

    public void hideStatusBar(View view) {
        StatusBarUtil.setTranslucent(this);
//        StatusBarUtil.setTranslucentForImageView(this, 100, findViewById(R.id.iv));

    }

    public void changeStatusBarColor(View view) {

    }




}