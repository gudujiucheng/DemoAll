package com.canzhang.sample.manager.antifraud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.canzhang.sample.manager.antifraud.notification.NotificationTestActivity;
import com.example.base.utils.ToastUtil;
import com.example.simple_test_annotations.MarkManager;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * 反欺诈数据采集测试
 */
@MarkManager(value = "反欺诈数据采集测试")
public class AntiFraudManager extends BaseManager {

    private Activity mActivity;


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {

        mActivity = activity;
        List<ComponentItem> list = new ArrayList<>();
        list.add(getWifiName());
        list.add(getWifiNameFql());
        list.add(notificationTest());
        return list;
    }

    private ComponentItem notificationTest() {
        return new ComponentItem("通知监听测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, NotificationTestActivity.class));
            }
        });
    }


    private ComponentItem getWifiName() {

        return new ComponentItem("获取wifi的名字", "9.0的机型，必须请求GPS权限并打开GPS才可以正确获取到WIFI名称", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //9.0以后需要动态申请权限（这个动态申请的居然是地理位置权限），10.0还不确定。
                AndPermission.with(mActivity)
                        .runtime()
                        .permission(Permission.Group.LOCATION)
                        .onGranted(permissions -> {
                            String wifissid = getWIFISSID(mActivity);
                            showToast("name:" + wifissid);
                        })
                        .onDenied(permissions -> {
                            // Storage permission are not allowed.
                        })
                        .start();

            }
        });
    }


    private ComponentItem getWifiNameFql() {

        return new ComponentItem("Fql代码 获取wifi的名字", "没有地理位置全选照样拿不到", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.with(mActivity)
                        .runtime()
                        .permission(Permission.READ_PHONE_STATE)
                        .onGranted(permissions -> {
                            showToast("name:" + getSSID(mActivity));
                        })
                        .onDenied(permissions -> {
                            // Storage permission are not allowed.
                        })
                        .start();


            }
        });
    }


    /**
     * 获取SSID
     *
     * @param activity 上下文
     * @return WIFI 的SSID
     * <p>
     * 原文链接：https://blog.csdn.net/GodnessIsMyMine/article/details/84400360
     */
    private String getWIFISSID(Activity activity) {
        String ssid = "unknown id";

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O || Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {

            WifiManager mWifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            assert mWifiManager != null;
            WifiInfo info = mWifiManager.getConnectionInfo();

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                return info.getSSID();
            } else {
                return info.getSSID().replace("\"", "");
            }
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1) {

            ConnectivityManager connManager = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connManager != null;
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo.isConnected()) {
                if (networkInfo.getExtraInfo() != null) {
                    return networkInfo.getExtraInfo().replace("\"", "");
                }
            }
        }
        return ssid;
    }






    public static String getSSID(Context context) {
        if (context == null) {
            return "";
        } else {
            WifiManager wifiManager = getWifiManager(context);
            if (wifiManager == null) {
                return "";
            } else {
                String ssid = "";
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo != null) {
                    ssid = wifiInfo.getSSID();
                }

                return TextUtils.isEmpty(ssid) ? "" : ssid.replace("\"", "");
            }
        }
    }

    @Nullable
    public static WifiManager getWifiManager(Context context) {
        boolean permission = ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_WIFI_STATE") == 0;
        return permission ? (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE) : null;
    }


}
