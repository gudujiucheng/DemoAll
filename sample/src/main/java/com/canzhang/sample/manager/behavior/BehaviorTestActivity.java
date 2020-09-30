package com.canzhang.sample.manager.behavior;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.behavior.phonestate.PhoneStateManager;
import com.canzhang.sample.manager.behavior.screenshot.ScreenShotUtil;
import com.example.base.base.AppProxy;
import com.example.base.base.BaseActivity;
import com.example.base.utils.ToastUtil;

/**
 * 用户行为测试
 */
public class BehaviorTestActivity extends BaseActivity {

    public static final String TAG = "BehaviorTestActivity";

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
        checkReadPermission();
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


    private void checkReadPermission() {
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

    /**
     * 开启剪贴板的监听（在android Q 版本，只有当当前应用处于前台的时候才能监听到剪贴板内容，处于后台是拿不到剪贴板内容的。）
     *
     * @param view
     */
    public void startClipBoardListener(View view) {
        Log.d(BehaviorTestActivity.TAG, "开启剪贴板监听");
        ClipboardManager clipBoardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipBoardManager != null) {
            clipBoardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    Log.d(BehaviorTestActivity.TAG, "剪贴板内容有变动");
                    ClipData clipData = clipBoardManager.getPrimaryClip();
                    if (clipData != null && clipData.getItemCount() > 0) {
                        CharSequence text = clipData.getItemAt(0).getText();
                        String pasteString = null;
                        if (text != null) {
                            pasteString = text.toString();
                        }
                        Log.d(BehaviorTestActivity.TAG, "getFromClipboard text=" + pasteString);
                    }

                }
            });
        }

// FIXME 取消注册逻辑       clipBoardManager.removePrimaryClipChangedListener();
    }


    /**
     * 监听电话的状态
     *
     * @param view
     */
    public void startPhoneStateListener(View view) {
//        this.startService(new Intent(this, BehaviorService.class));

        PhoneStateManager.getInstance().startPhoneStateListener(this);
    }
}
