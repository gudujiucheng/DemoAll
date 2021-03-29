package com.canzhang.sample.debug;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.canzhang.sample.BuildConfig;
import com.canzhang.sample.R;
import com.canzhang.sample.manager.appstatus.AppStatus;
import com.canzhang.sample.manager.appstatus.AppStatusChangeListener;
import com.canzhang.sample.manager.block.githup_test_use.AppBlockCanaryContext;
import com.canzhang.sample.manager.weex.ImageAdapter;
import com.canzhang.sample.manager.weex.view.FqlWeexFloatingAds;
import com.canzhang.sample.manager.weex.view.FqlWeexQRCodeView;
import com.canzhang.sample.manager.weex.view.RichImageview;
import com.canzhang.sample.manager.weex.view.RichText;
import com.component.debugdialog.DebugDialog;
import com.example.base.base.AppProxy;
import com.example.base.utils.ToastUtil;
import com.github.moduth.blockcanary.BlockCanary;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;


/**
 * 调试模式下的 app（simple作为单独工程运行时候使用）
 */
public class DebugBaseApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        setTheme(R.style.Sample_AppThemeSplash);
        AppProxy.getInstance().onApplicationCreate(this);
        //调试
        DebugDialog.getInstance().init(this);
        DebugDialog.setIsDebug(true);
        initWeex();
        AppStatus.getInstance().setActivityLifeCallBack(this);
        AppStatus.getInstance().addChangeListener(new AppStatusChangeListener() {
            @Override
            public void onAppToBackground() {
                ToastUtil.toastShort("APP后台");
            }

            @Override
            public void onAppToFront() {
                ToastUtil.toastShort("APP前台");
            }

            @Override
            public void onAppFirstCreate() {
                ToastUtil.toastShort("初次启动");
            }
        });


        initPush();

//        initBlockCanary();
    }

    private void initPush() {
        XGPushConfig.enableDebug(this, BuildConfig.DEBUG);
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                //token在设备卸载重装的时候有可能会变
                Log.e("TPush", "注册成功，设备token为：" + data);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.e("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });

        //打开第三方推送
        XGPushConfig.enableOtherPush(getApplicationContext(), true);
        //打开成功的话 会有如下日志提示（过滤标签 TPush）
        //I/XINGE: [XGOtherPush] other push token is : 0865551032618726300001294600CN01 other push type: huawei
        // I/XINGE: [a] binder other push token with accid = 2100274337  token = 17c32948df0346d5837d4748192e9d2f14c81e08 otherPushType = huawei otherPushToken = 0865551032618726300001294600CN01
    }


    private void initBlockCanary() {
        // 在主进程初始化调用哈
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
    }

    private void initWeex() {
        InitConfig config = new InitConfig.Builder().setImgAdapter(new ImageAdapter()).build();
        WXSDKEngine.initialize(this, config);
        try {
            WXSDKEngine.registerComponent("FqlWeexFloatingAds", FqlWeexFloatingAds.class);
            WXSDKEngine.registerComponent("richText", RichText.class);
            WXSDKEngine.registerComponent("RichImageview", RichImageview.class);
            WXSDKEngine.registerComponent("FqlWeexQRCodeView", FqlWeexQRCodeView.class);
        } catch (WXException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.e("Test", activity.getClass().getSimpleName());

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }


}
