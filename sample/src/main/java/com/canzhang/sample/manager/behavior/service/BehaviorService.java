package com.canzhang.sample.manager.behavior.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.View;

import com.canzhang.sample.manager.behavior.phonestate.PhoneStateManager;

/**
 * 如果怕被干掉，倒是可以开一个服务，暂不考虑
 */
public class BehaviorService extends Service {
    public BehaviorService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startPhoneStateListener();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startPhoneStateListener() {
        PhoneStateManager.getInstance().startPhoneStateListener(this.getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PhoneStateManager.getInstance().removePhoneStateListener();
    }
}
