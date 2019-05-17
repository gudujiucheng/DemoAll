package com.canzhang.permission;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.canzhang.aidlservice.R;


/**
 * 自定义权限
 * https://www.cnblogs.com/liuzhipenglove/p/7102889.html
 * https://blog.csdn.net/weixin_37077539/article/details/56279789
 */
public class DangerousActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_permission_test);
        TextView tv = findViewById(R.id.tv_permission);
        tv.setText(getClass().getName());
    }
}
