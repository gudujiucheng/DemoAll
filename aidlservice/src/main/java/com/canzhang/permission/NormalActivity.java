package com.canzhang.permission;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.canzhang.aidlservice.R;


public class NormalActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_permission_test);
        TextView tv = findViewById(R.id.tv_permission);
        tv.setText(getClass().getName());
    }
}
