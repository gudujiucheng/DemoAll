package com.canzhang.sample.manager.antifraud.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.zhujie.BindTestActivity;
import com.example.base.utils.ToastUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NotificationTestActivity extends Activity {

    public static final String TAG = "NotificationTest";
    public static int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_notification);
        if (!isNotificationListenerEnabled(this)) {
            openNotificationListenSettings();
        }
        toggleNotificationListenerService();
        //通知权限是否开启
        findViewById(R.id.bt_test_02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NotificationManagerCompat.from(NotificationTestActivity.this).areNotificationsEnabled()) {
                    ToastUtil.toastShort("开启了");
                } else {
                    ToastUtil.toastShort("未开启");
                }
            }
        });
        //暂未找到控制不折叠的方案   可以看出信鸽是支持不折叠方案的  是通过不同分组来实现的 setGroup(index+"")
        findViewById(R.id.bt_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder ncBuilder = null;
                NotificationManager manager = (NotificationManager) NotificationTestActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    //https://www.jianshu.com/p/4c51c071aa94
                    String channelId = "notify_channel_id" + index;//TODO 不同的channel_id也还是会折叠(pass)
                    String channelName = "notify_channel_name" + index;
                    NotificationChannel channel = new NotificationChannel(channelId,
                            channelName, NotificationManager.IMPORTANCE_HIGH);
                    manager.createNotificationChannel(channel);
                    ncBuilder = new NotificationCompat.Builder(NotificationTestActivity.this, channelId);
                } else {
                    ncBuilder = new NotificationCompat.Builder(NotificationTestActivity.this);
                }

                ncBuilder
                        .setTicker("Ticker状态栏标题头" + index)//是通知时在状态栏显示的通知内容，一般只是一段文字，例如在状态栏版显示“您有一条短信，待查权收”。
                        .setContentTitle("标题头" + index)//通知内容的标题头
                        .setContentText("内容xxxxx" + index)//通知内容部分
                        .setSmallIcon(R.drawable.block_canary_icon);//TODO 这个必须要设置 不设置会崩溃

                ncBuilder.setPriority(NotificationCompat.PRIORITY_MAX);//TODO 升级到最高级别也还是会折叠(pass)
                ncBuilder.setGroup(index + "");//这个可以 ，设置不同的通知组  ------------------------------------------------>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>ok
                NotificationManager nm = (NotificationManager) NotificationTestActivity.this
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify(index, ncBuilder.build());

                index++;
            }
        });
        findViewById(R.id.bt_generate_notify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bmp = BitmapFactory.decodeResource(
                        NotificationTestActivity.this.getResources(),
                        R.drawable.debug_icon_debug);
                //PendingIntent可选的主要有getActivity、getBroadcast、getService
                PendingIntent pendingIntent = PendingIntent
                        .getActivity(NotificationTestActivity.this, 0, new Intent(NotificationTestActivity.this, BindTestActivity.class),
                                PendingIntent.FLAG_UPDATE_CURRENT);


                NotificationCompat.Builder ncBuilder = null;
                NotificationManager manager = (NotificationManager) NotificationTestActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    //https://www.jianshu.com/p/4c51c071aa94

                    String channelId = "notify_channel_id";
                    String channelName = "notify_channel_name";
                    NotificationChannel channel = new NotificationChannel(channelId,
                            channelName, NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription("这是默认的chanel");
                    channel.enableLights(true); //设置开启指示灯，如果设备有的话
                    channel.setLightColor(Color.GREEN); //设置指示灯颜色

                    String channelId02 = "notify_channel_id_02";
                    String channelName02 = "notify_channel_name_02";
                    NotificationChannel channel02 = new NotificationChannel(channelId02,
                            channelName02, NotificationManager.IMPORTANCE_HIGH);
                    channel02.setDescription("这是级别高的的chanel");
                    channel02.enableLights(true); //设置开启指示灯，如果设备有的话
                    channel02.setLightColor(Color.GREEN); //设置指示灯颜色

                    String channelId03 = "notify_channel_id_03";
                    String channelName03 = "notify_channel_name_03";
                    NotificationChannel channel03 = new NotificationChannel(channelId03,
                            channelName03, NotificationManager.IMPORTANCE_LOW);//这个在设置页面（华为直接展示为静默通知，当然也可以选择开启铃声
                    channel03.setDescription("这是级别低的的chanel");
                    channel03.enableLights(true); //设置开启指示灯，如果设备有的话
                    channel03.setLightColor(Color.GREEN); //设置指示灯颜色

                    //如上面这面设置，在设置页面就会展示为
                    /**
                     * 类别：（展示如下三个类别，针对每个类别可点击进入设置是否允许通知、是否开启铃声等等子项功能）
                     * notify_channel_name
                     * notify_channel_name_02
                     * notify_channel_name_03
                     */

                    //此外还可以设置分组，分组就是可以根据传入的groupId在细分组，每组都可以有自己的类别。 具体参见 https://www.jianshu.com/p/4c51c071aa94
//                    manager.createNotificationChannelGroup(new NotificationChannelGroup("groupId_01", "用户" + "groupId_01"));
//                    channel.setGroup("groupId_01");


                    List<NotificationChannel> channels = new ArrayList<>();
                    channels.add(channel);
                    channels.add(channel02);
                    channels.add(channel03);

//                    manager.createNotificationChannel(channel);//注册到系统中去，这里可以接收多个channel
                    manager.createNotificationChannels(channels);//注册到系统中去，这里可以接收多个channel

                    String currentUseChannelId;
                    switch (index % 3) {
                        case 1:
                            currentUseChannelId = channelId02;
                            break;
                        case 2:
                            currentUseChannelId = channelId03;
                            break;
                        default:
                            currentUseChannelId = channelId;
                            break;


                    }

                    ncBuilder = new NotificationCompat.Builder(NotificationTestActivity.this, currentUseChannelId);
                } else {
                    ncBuilder = new NotificationCompat.Builder(NotificationTestActivity.this);
                }
                //TODO 如果需要展示横幅（悬浮窗），需要在通知管理打开对应应用的横幅权限。
                //FIXME 还可以设置自定义布局的  通知类型 setContent(RemoteView view)
                //TODO 还可以设置锁屏是否展示，这个也相应定制权限控制 setVisibility(int visibility)


                ncBuilder
                        .setSmallIcon(R.drawable.block_canary_icon)//左上角的小鼠标，一般是设置应用图标
                        .setLargeIcon(bmp)//内容部分的大图标，可以根据内容设置不同的图标
                        .setTicker("Ticker状态栏标题头" + index)//是通知时在状态栏显示的通知内容，一般只是一段文字，例如在状态栏版显示“您有一条短信，待查权收”。
                        .setContentTitle("标题头" + index)//通知内容的标题头
                        .setContentText("内容xxxxx" + index)//通知内容部分
                        .setDefaults(NotificationCompat.DEFAULT_SOUND
                                | Notification.DEFAULT_VIBRATE)//响铃震动
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)//点击移除通知栏 true：点击移除，false：点击不移除，可自行侧滑移除。
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置通知重要程度
                        .setContentIntent(pendingIntent);//设置点击事件


                NotificationManager nm = (NotificationManager) NotificationTestActivity.this
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify(index, ncBuilder.build());

                index++;
            }
        });


        //这里测试个短信
        findViewById(R.id.bt_read).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {//添加 删除短信已经搞不定了

                AndPermission.with(NotificationTestActivity.this)
                        .runtime()
                        .permission(Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_SMS)
                        .onGranted(permissions -> {
                            Log.e(TAG, "权限获取成功");
                            Toast.makeText(NotificationTestActivity.this, "权限获取成功", Toast.LENGTH_SHORT).show();
                            //访问内容提供者获取短信
                            ContentResolver cr = getContentResolver();
                            //短信内容提供者的主机名
                            Cursor cursor = cr.query(Uri.parse("content://sms"), new String[]{"address", "date", "body", "type"}, null, null);
                            while (cursor.moveToNext()) {
                                String address = cursor.getString(0);
                                long date = cursor.getLong(1);
                                String body = cursor.getString(2);
                                String type = cursor.getString(3);

                                Log.e(TAG, "address:" + address + " date:" + date + " body:" + body + " type:" + type);
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

    //检测通知监听服务是否被授权（注意这个非通知权限，和通知权限没有关系）
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
