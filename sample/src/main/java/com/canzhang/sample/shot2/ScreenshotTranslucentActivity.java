package com.canzhang.sample.shot2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.example.base.utils.PictureUtils;

import java.nio.ByteBuffer;


/**
 * 2020, Tencent
 * Author: darrenzeng
 * Date: 2020/03/06 03:10 PM
 * Description: 屏幕截图权限处理的透明的 activity
 * Version: 1.0.0
 */
public class ScreenshotTranslucentActivity extends Activity {

    private static final int REQUEST_CODE = 100;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mScreenDensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        start();
    }

    public void start() {
        MediaProjectionManager mediaProjectionManager =
                (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent permissionIntent = mediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(permissionIntent, REQUEST_CODE);
    }

    /**
     * 该Activity启动后，在获得截屏权限前如果又被一个新的Activity覆盖了，会导致onActivityResult()不能及时执行，最终无法及时获取到权限
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                return;
            }
            //获得录屏权限，启动Service进行录制
            Intent intent = new Intent(this, ShotScreenService.class);
            intent.putExtra("resultCode", resultCode);
            intent.putExtra("resultData", data);
            intent.putExtra("mScreenWidth", mScreenWidth);
            intent.putExtra("mScreenHeight", mScreenHeight);
            intent.putExtra("mScreenDensity", mScreenDensity);
            startService(intent);
        }
        finish();
    }

}
