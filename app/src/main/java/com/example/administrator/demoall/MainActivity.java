package com.example.administrator.demoall;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.demoall.filemanager.FileTestActivity;
import com.meituan.android.walle.WalleChannelReader;

/**
 * TODO 这里需要改造，添加上来Arouter
 * TODO 以后多一些大模块的分支 相互解耦，方便不需要的时候可以屏蔽掉，提高编译速度
 * TODO 添加base模块
 */
public class MainActivity extends AppCompatActivity {
    public static String TAG = "Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,ChannelActivity.class));
//                startActivity(new Intent(MainActivity.this, ViewTestActivity.class));
//                startActivity(new Intent(MainActivity.this,WeexTestActivity.class));
//              TestActivity.startTestActivity(MainActivity.this);
//                startActivity(new Intent(MainActivity.this,RxjavaTestActivity.class));
//                startActivity(new Intent(MainActivity.this,StorageActivity.class));
//                showTipsDialog(MainActivity.this, "xxxxxxx");
//                showOpenSettingTipsDialog(MainActivity.this,"xxxxxxx");
//                startActivity(new Intent(MainActivity.this,MMKVActivity.class));
                startActivity(new Intent(MainActivity.this,FileTestActivity.class));
//                startActivity(new Intent(MainActivity.this,WebviewActivity.class));
//                startService(new Intent(MainActivity.this,PreLoadService.class));
            }
        });

    }


    public static void showTipsDialog(final Activity mActivity, String tips) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.AppThemeDialog);
        builder.setMessage(tips)
                .setPositiveButton("确定", null)
                .setCancelable(true);
        builder.create().show();
    }

    public static void showOpenSettingTipsDialog(final Activity mActivity, String tips) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.AppThemeDialog);
        builder.setMessage(tips)
                .setPositiveButton("h", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("d", null).setCancelable(false);
        builder.create().show();
    }

    public void showChannel(View view) {
        //获取渠道
        String channel = WalleChannelReader.getChannel(this.getApplicationContext());
        Toast.makeText(this,"当前包的渠道是："+channel,Toast.LENGTH_LONG).show();
    }
}

