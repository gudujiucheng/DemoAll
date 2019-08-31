package com.canzhang.sample.manager.flutter_test;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.canzhang.sample.R;
import com.example.base.base.BaseActivity;

//import io.flutter.facade.Flutter;

public class FlutterTestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_flutter_test);
        findViewById(R.id.bt_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View flutterView = Flutter.createView(
                        FlutterTestActivity.this,
                        getLifecycle(),
                        "route1"
                );
                FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(600, 800);
                layout.leftMargin = 100;
                layout.topMargin = 200;
                addContentView(flutterView, layout);
            }
        });
    }
}
