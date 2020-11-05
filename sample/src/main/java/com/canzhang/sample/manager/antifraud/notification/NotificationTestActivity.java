package com.canzhang.sample.manager.antifraud.notification;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import com.canzhang.sample.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;


import java.util.Set;

public class NotificationTestActivity extends Activity {

    public static final String TAG = "NotificationTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_notification);
        if (!isNotificationListenerEnabled(this)){
            openNotificationListenSettings();
        }
        toggleNotificationListenerService();


        findViewById(R.id.tv_read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//添加 删除短信已经搞不定了

                AndPermission.with(NotificationTestActivity.this)
                        .runtime()
                        .permission(Permission.WRITE_EXTERNAL_STORAGE,Permission.READ_SMS)
                        .onGranted(permissions -> {
                            Log.e(TAG, "权限获取成功");
                            Toast.makeText(NotificationTestActivity.this, "权限获取成功", Toast.LENGTH_SHORT).show();
                            //访问内容提供者获取短信
                            ContentResolver cr = getContentResolver();
                            //短信内容提供者的主机名
                            Cursor cursor = cr.query(Uri.parse("content://sms"),new String[]{"address","date","body","type"},null,null);
                            while(cursor.moveToNext()){
                                String address = cursor.getString(0);
                                long date = cursor.getLong(1);
                                String body = cursor.getString(2);
                                String type = cursor.getString(3);

                                Log.e(TAG,"address:"+address+" date:"+date+" body:"+body+" type:"+type);
                            }
                        })
                        .onDenied(permissions -> {
                            Log.e(TAG, "权限获取被拒绝");
                            Toast.makeText(NotificationTestActivity.this, "权限获取被拒绝", Toast.LENGTH_SHORT).show();
                        })
                        .start();

            }
        });


    }
    //检测通知监听服务是否被授权
    public boolean isNotificationListenerEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(this);
        if (packageNames.contains(context.getPackageName())) {
            return true;
        }
        return false;
    }
    //打开通知监听设置页面
    public void openNotificationListenSettings() {
        try {
            Intent intent;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            } else {
                intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //把应用的NotificationListenerService实现类disable再enable，即可触发系统rebind操作
    private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName(this, NotificationCollectorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(
                new ComponentName(this, NotificationCollectorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

}
