package com.example.administrator.demoall;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
                showTipsDialog(MainActivity.this, "xxxxxxx");
//                showOpenSettingTipsDialog(MainActivity.this,"xxxxxxx");
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

}

