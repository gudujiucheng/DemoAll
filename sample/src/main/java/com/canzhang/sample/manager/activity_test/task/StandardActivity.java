package com.canzhang.sample.manager.activity_test.task;

import android.content.Context;
import android.content.Intent;

public class StandardActivity extends BaseTaskActivity {
    public static void start(Context context, String content) {
        Intent intent = new Intent(context, StandardActivity.class);
        intent.putExtra(CONTENT, content);
        context.startActivity(intent);
    }
}
