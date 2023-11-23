package com.canzhang.sample.shot2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.canzhang.sample.R;

import java.text.SimpleDateFormat;

/**
 * Author: darrenzeng
 * Date: 2021/6/30 3:10 PM
 * Description: target 30 ，手机版本号 > 30 的截图处理
 * Version: 1.0.0
 */
public class ShotScreenService extends Service {
    private int mResultCode;
    private Intent mResultData = null;
    private MediaProjection mMediaProjection = null;
    private VirtualDisplay mVirtualDisplay = null;
    private String filePathName = null;


    private int mScreenWidth;
    private int mScreenHeight;
    private int mScreenDensity;

    private ImageReader mImageReader;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        if ("sample_screenshot_new".equals(intent.getAction())) {
            startCapture();
        } else {
            createNotificationChannel();
            mResultCode = intent.getIntExtra("resultCode", -1);
            mResultData = intent.getParcelableExtra("resultData");
            mScreenWidth = intent.getIntExtra("mScreenWidth", 0);
            mScreenHeight = intent.getIntExtra("mScreenHeight", 0);
            mScreenDensity = intent.getIntExtra("mScreenDensity", 0);

            mImageReader = ImageReader
                    .newInstance(mScreenWidth, mScreenHeight,
                            PixelFormat.RGBA_8888, 2);
            mMediaProjection = createMediaProjection();
            mVirtualDisplay = createVirtualDisplay();


        }


        return super.onStartCommand(intent, flags, startId);
    }

    private void startCapture() {
        Image image = mImageReader.acquireLatestImage();
        if (image != null) {
            Log.d("NewScreenShot", "获取成功");
            image.close();
        } else {
            Log.d("NewScreenShot", "获取失败");
        }
    }

    private VirtualDisplay createVirtualDisplay() {
        return mMediaProjection.createVirtualDisplay("mediaProjection", mScreenWidth, mScreenHeight, mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mImageReader.getSurface(), null, null);
    }

    private MediaProjection createMediaProjection() {
        return ((MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE)).getMediaProjection(mResultCode, mResultData);
    }


    private void createNotificationChannel() {
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
        builder.setLargeIcon(
                        BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher)) // 设置下拉列表中的图标(大图标)
                .setSmallIcon(R.drawable.ic_launcher) // 设置状态栏内的小图标
                .setContentText("is running......") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

        /*以下是对Android 8.0的适配*/
        //普通notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("notification_id");
        }
        //前台服务notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("notification_id", "notification_name",
                    NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Notification notification = builder.build();
            notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
            startForeground(110, notification);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
            mVirtualDisplay = null;
        }

        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
        //通知媒体库更新内容，否则一开始录制完的视频文件在相册中是找不到的，只有在文件管理播放过后才能出现在相册页面
        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{filePathName}, null, null);
    }


}
