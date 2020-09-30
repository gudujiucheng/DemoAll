package com.example.administrator.demoall.MMKV;

import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.administrator.demoall.R;

public class ProcessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("Test",MODE_PRIVATE);
        setContentView(R.layout.activity_process);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putString("Test","我是来自进程2的数据").apply();
                Log.e("Test","ProcessActivity :插入数据成功");

            }
        });
        findViewById(R.id.tv_02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = sharedPreferences.getString("Test1","没获取到");
                Log.e("Test","ProcessActivity :"+s);
            }
        });
    }
}
