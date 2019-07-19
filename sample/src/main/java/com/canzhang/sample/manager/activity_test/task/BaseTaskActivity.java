package com.canzhang.sample.manager.activity_test.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.canzhang.sample.R;
import com.example.base.base.BaseActivity;

public class BaseTaskActivity extends BaseActivity {
    protected static final String CONTENT = "content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_task_test);
        TextView tvContent = findViewById(R.id.tv_content);
        tvContent.setText(getIntent().getStringExtra(CONTENT));
    }

}
