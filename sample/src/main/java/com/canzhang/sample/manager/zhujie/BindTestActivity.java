package com.canzhang.sample.manager.zhujie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.canzhang.sample.R;
import com.example.simplebutterknife_annotations.BindView;

/**
 * 测试实际应用
 */
public class BindTestActivity extends AppCompatActivity {
    @BindView(R.id.bind_test)
    TextView mTitleTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_bind_test);
        SimpleButterKnife.bind(this);
        mTitleTextView.setText("哈哈哈哈哈哈哈");
    }
}