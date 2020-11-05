package com.canzhang.sample.manager.zhujie;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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


        //其他：测试接收参数
        Uri uri = getIntent().getData();
        if (uri != null) {
            String url = uri.toString();
            String p1= uri.getQueryParameter("param1");
            String p2= uri.getQueryParameter("param2");

            Log.e("TPush","url:"+url+"\n param1:"+p1+" param2:"+p2);
        }
    }
}