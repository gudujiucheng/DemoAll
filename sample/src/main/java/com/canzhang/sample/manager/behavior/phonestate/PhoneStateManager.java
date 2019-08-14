package com.canzhang.sample.manager.behavior.phonestate;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.canzhang.sample.manager.behavior.BehaviorTestActivity;
import com.example.base.utils.ToastUtil;

/**
 * @Description:通话状态管理
 * @Author: canzhang
 * @CreateDate: 2019/8/12 11:13
 */
public class PhoneStateManager {

    private TelephonyManager telephonyManager;

    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            Log.d(BehaviorTestActivity.TAG, "state：" + state);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: // 待机，即无电话时，挂断时触发
                    ToastUtil.toastShort("待机状态");
                    Log.d(BehaviorTestActivity.TAG, "待机状态");
                    break;
                case TelephonyManager.CALL_STATE_RINGING: // 响铃，来电时触发
                    ToastUtil.toastShort("来电，响铃时候触发");
                    Log.d(BehaviorTestActivity.TAG, "来电，响铃时候触发" + incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK: // 摘机，接听或打电话时触发（区分不了，都叫接通）
                    ToastUtil.toastShort("接听或者打电话时候触发 属于通话状态");
                    Log.d(BehaviorTestActivity.TAG, "接听或者打电话时候触发 属于通话状态");
                    break;
                default:
                    break;
            }
        }
    };

    private PhoneStateManager() {
    }

    private static class SingletonClassInstance {
        private static final PhoneStateManager instance = new PhoneStateManager();
    }

    public static PhoneStateManager getInstance() {
        return SingletonClassInstance.instance;
    }


    /**
     * 监听电话的状态（有时候程序后台了  就好像获取不到了，这个可以开启一个服务测试下）
     */
    public void startPhoneStateListener(Context context) {
        if (context == null) {
            return;
        }

        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            // 设置来电监听
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }

    }


    /**
     * 取消监听
     */
    public void removePhoneStateListener() {
        if (telephonyManager != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
            telephonyManager = null;
        }
    }
}
