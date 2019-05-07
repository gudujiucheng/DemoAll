package com.canzhang.sample.manager.activity_test;

import android.os.Bundle;

import com.canzhang.sample.R;
import com.example.base.base.BaseActivity;

public class TranslateActivity extends BaseActivity {


    /**
     *
     * @param savedInstanceState 有可能为空，一般使用onRestoreInstanceState 里面的来恢复
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sample_activity_translate);

        log("onCreate");
    }

    /**
     * 只有异常场景会用到（含旋转屏幕，因为立刻需要展示保存过的信息）
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    /**
     * 只有异常场景会用到（含旋转屏幕，因为立刻需要展示保存过的信息）
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        log("onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        log("onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        log("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        log("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        log("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        log("onDestroy");
    }


}
