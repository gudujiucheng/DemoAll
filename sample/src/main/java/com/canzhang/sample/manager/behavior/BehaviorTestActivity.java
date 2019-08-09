package com.canzhang.sample.manager.behavior;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import com.canzhang.sample.R;
import com.canzhang.sample.debug.DebugBaseApp;
import com.canzhang.sample.manager.behavior.ScreenShot.ScreenShotUtil;
import com.example.base.base.AppProxy;
import com.example.base.base.BaseActivity;
import com.example.base.utils.ToastUtil;

/**
 * 用户行为测试
 */
public class BehaviorTestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_behavior_test);
    }

    /**
     * 开始截屏监听
     *
     * @param view
     */
    public void openScreenShotListener(View view) {
        checkPermission();
    }

    private void startListen() {
        ToastUtil.toastShort("开启截屏监听");
        ScreenShotUtil.getInstance().setListener(new ScreenShotUtil.OnScreenShotListener() {
            @Override
            public void onShot(String imagePath) {
                ToastUtil.toastShort("检测到截屏：" + imagePath);

            }
        }).startListen(AppProxy.getInstance().getApplication());

    }

    /**
     * 停止截屏监听
     *
     * @param view
     */
    public void stopScreenShotListener(View view) {
        ScreenShotUtil.getInstance().stopListen();
        ToastUtil.toastShort("停止截屏监听");
    }


    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else {
            startListen();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startListen();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
