package com.canzhang.sample.manager.activity_test.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.NonNull;
import android.util.Log;

public class StandardActivity extends BaseTaskActivity {
    public static void start(Context context, String content) {
        Intent intent = new Intent(context, StandardActivity.class);
        intent.putExtra(CONTENT, content);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("LIFE_TEST_CAN","----------------------->>>>>>>onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("LIFE_TEST_CAN","----------------------->>>>>>>onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
       Log.e("LIFE_TEST_CAN","----------------------->>>>>>>onRestart");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
       Log.e("LIFE_TEST_CAN","----------------------->>>>>>>onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
       Log.e("LIFE_TEST_CAN","----------------------->>>>>>>onRestoreInstanceState");
    }
    @Override
    public void finish() {//一般常规操作这个是在 onResume 之后 onPause 之前调用的
        super.finish();
       Log.e("LIFE_TEST_CAN","----------------------->>>>>>>finish");
    }
    @Override
    protected void onPause() {
        super.onPause();
       Log.e("LIFE_TEST_CAN","----------------------->>>>>>>onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
       Log.e("LIFE_TEST_CAN","----------------------->>>>>>>onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       Log.e("LIFE_TEST_CAN","----------------------->>>>>>>onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
       Log.e("LIFE_TEST_CAN","----------------------->>>>>>>onResume");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
       Log.e("LIFE_TEST_CAN","----------------------->>>>>>>onNewIntent");
    }
}
