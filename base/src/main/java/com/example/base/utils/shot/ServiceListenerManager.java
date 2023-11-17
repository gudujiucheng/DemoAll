package com.example.base.utils.shot;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: darrenzeng
 * Date: 2021/6/30 7:22 PM
 * Description: ShotScreenService 创建后来通知，兼容版本 30
 * Version: 1.0.0
 */
public class ServiceListenerManager {

    private static ServiceListenerManager mManager;

    private List<ServiceListener> mServiceListener = new ArrayList<>();

    public static ServiceListenerManager getManager() {
        if (mManager == null) {
            mManager = new ServiceListenerManager();
        }
        return mManager;
    }

    public void register(ServiceListener serviceListener) {
        mServiceListener.add(serviceListener);
    }

    public void unregister(ServiceListener serviceListener) {
        mServiceListener.remove(serviceListener);
    }

    public void notifyListener() {
        for (ServiceListener serviceListener : mServiceListener) {
            serviceListener.onServiceStart();
        }
    }
}
