package com.canzhang.sample.manager.activity_test;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Messenger;
import android.view.View;
import android.view.WindowManager;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 小测试
 */
public class ActivityTestDemoManager extends BaseManager {

    private Activity mActivity;


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        mActivity = activity;
        List<ComponentItem> list = new ArrayList<>();
        list.add(lifeTest());
        list.add(landTest());
        list.add(portpaitTest());
        list.add(fullTest());
        return list;
    }

    private ComponentItem lifeTest() {
        return new ComponentItem("启动一个透明activity（生命周期）", "透明主题的activity，前一个activity只会走onPause，不会走onStop" +
                "\n2、关掉透明activity，前一个activity也只会走onResume,而不是onRestart-onStart-onResume", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, TranslateActivity.class));
            }
        });
    }


    /**
     * 横竖屏参考文章：https://www.cnblogs.com/yishujun/p/5395266.html
     *
     * @return
     */
    private ComponentItem landTest() {
        return new ComponentItem("切换横屏（并隐藏状态栏）", "由于设置了android:configChanges=orientation|screenSize，所以不会重新走生命周期" +
                "\n* orientation：屏幕方向发生了变化" +
                "\n* screenSize：屏幕尺寸信息发生了变化（旋转屏幕的时候这个是会变化的）" +
                "\n更多还有横竖屏切换布局，保存信息等等，请参考上方链接", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivity != null) {
                    //校验方法1
//                    if(mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
//                        showToast("当前已经是横屏状态");
//                        return;
//                    }

                    //校验方法2
                    if (mActivity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                        showToast("当前已经是横屏状态");
                        return;
                    }
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                    mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
                }
            }
        });
    }

    private ComponentItem portpaitTest() {
        return new ComponentItem("恢复竖屏（并显示状态栏）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivity != null) {
//                    if(mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
//                        showToast("当前已经是竖屏状态");
//                        return;
//                    }

                    if (mActivity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                        showToast("当前已经是竖屏状态");
                        return;
                    }
                    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//                    mActivity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
                }
            }
        });
    }


    private ComponentItem fullTest() {
        return new ComponentItem("全屏", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomUIMenu();
            }
        });
    }


    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = mActivity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = mActivity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


}
