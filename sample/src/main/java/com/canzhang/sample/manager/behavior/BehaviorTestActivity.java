package com.canzhang.sample.manager.behavior;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.behavior.screenshot.ScreenShotUtil;
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
     * 开启剪贴板的监听
     *
     * @param view
     */
    public void startClipBoardListener(View view) {
        ClipboardManager clipBoardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipBoardManager != null) {
            clipBoardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    ClipData clipData = clipBoardManager.getPrimaryClip();
                    if (clipData != null && clipData.getItemCount() > 0) {
                        CharSequence text = clipData.getItemAt(0).getText();
                        String pasteString = null;
                        if (text != null) {
                            pasteString = text.toString();
                        }
                        Log.d("ClipData", "getFromClipboard text=" + pasteString);
                    }

                }
            });
        }
    }


    /**
     * 监听电话的状态
     *
     * @param view
     */
    public void startPhoneStateListener(View view) {
        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);

                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE: // 待机，即无电话时，挂断时触发
                        ToastUtil.toastShort("挂断");
                        break;
                    case TelephonyManager.CALL_STATE_RINGING: // 响铃，来电时触发
                        ToastUtil.toastShort("来电");
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK: // 摘机，接听或打电话时触发
                        ToastUtil.toastShort("接听");
                        break;
                    default:
                        break;
                }
            }
        };
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            // 设置来电监听
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }
}
