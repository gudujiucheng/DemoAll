package com.example.base.utils.shot;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;


/**
 * 2020, Tencent
 * Author: darrenzeng
 * Date: 2020/03/06 03:10 PM
 * Description: 屏幕截图权限处理的透明的 activity
 * Version: 1.0.0
 */
public class ScreenshotTranslucentActivity extends Activity {

    private static final int VERSION_CODE_29 = 29;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenshotUtil.get().init(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ScreenshotUtil.get().init(this);
    }

    /**
     * 该Activity启动后，在获得截屏权限前如果又被一个新的Activity覆盖了，会导致onActivityResult()不能及时执行，最终无法及时获取到权限
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScreenshotUtil.REQUEST_MEDIA_PROJECTION && data != null) {
            Log.e("TAG", "onActivityResult -> " + data);
            notifyScreenshotPermissionGranted();
            startShotScreenService(data);
            finish();
        } else {
            ScreenshotUtil.get().init(this);
        }
    }

    /**
     * targetSdkVersion 30 需要升级
     */
    private void startShotScreenService(final Intent data) {
        int targetSdkVersion = getApplicationInfo().targetSdkVersion;
        if (Build.VERSION.SDK_INT >= VERSION_CODE_29 && targetSdkVersion >= VERSION_CODE_29) {
            ServiceListenerManager.getManager().register(new ServiceListener() {
                @Override
                public void onServiceStart() {
                    ScreenshotUtil.get().setData(data);
                    // 只用一次，防止泄漏
                    ServiceListenerManager.getManager().unregister(this);
                }
            });
            Intent service = new Intent(this, ShotScreenService.class);
            startForegroundService(service);
        } else {
            ScreenshotUtil.get().setData(data);
        }
    }

    private void notifyScreenshotPermissionGranted() {
        Intent intent = new Intent("on_screenshot_permission_granted");
        intent.putExtra("pkgName", getPackageName());
        sendBroadcast(intent);
    }
}
