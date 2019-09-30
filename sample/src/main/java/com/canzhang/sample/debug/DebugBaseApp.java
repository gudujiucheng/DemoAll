package com.canzhang.sample.debug;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.canzhang.sample.manager.appstatus.AppStatus;
import com.canzhang.sample.manager.appstatus.AppStatusChangeListener;
import com.canzhang.sample.manager.block.githup_test_use.AppBlockCanaryContext;
import com.canzhang.sample.manager.block.tencent_use.DynamicConfigImplDemo;
import com.canzhang.sample.manager.block.tencent_use.TestPluginListener;
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
import com.tencent.matrix.Matrix;


/**
 * 调试模式下的 app（simple作为单独工程运行时候使用）
 */
public class DebugBaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
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

//        initBlockCanary();
        initTencentMatrix();
    }

    private void initTencentMatrix() {
        Matrix.Builder builder = new Matrix.Builder(this); // build matrix
        builder.patchListener(new TestPluginListener(this)); // add general pluginListener
        //init matrix
        Matrix.init(builder.build());
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


        this.getApplicationContext();

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
