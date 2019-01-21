package com.canzhang.sample.manager;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;

import java.util.ArrayList;
import java.util.List;

/**
 * http://www.cnblogs.com/lwbqqyumidi/p/4127012.html
 */
public class BrightnessDemoManager extends BaseManager {

    private int mBrightness = 0;

    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        List<ComponentItem> list = new ArrayList<>();
        list.add(getScreenMode(activity));
        list.add(addBrightness(activity));
        list.add(reduceBrightness(activity));
        list.add(getNowLight(activity));

        mBrightness = getSystemBrightness(activity);
        return list;
    }

    /**
     * 获取屏幕亮度模式
     * 2. SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * 3. SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度
     */
    private ComponentItem getScreenMode(final Activity activity) {

        return new ComponentItem("获取亮度模式", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int screenMode = 0;
                try {
                    screenMode = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
                } catch (Exception localException) {

                }
                showTipsDialog("屏幕亮度模式", "屏幕亮度模式：" + (screenMode == 1 ? "自动" : "手动"));
            }
        });
    }

    private ComponentItem addBrightness(final Activity activity) {

        return new ComponentItem("窗口亮度+20", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAppBrightness(activity, mBrightness += 20);
            }
        });
    }


    private ComponentItem reduceBrightness(final Activity activity) {

        return new ComponentItem("窗口亮度-20", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAppBrightness(activity, mBrightness -= 20);
            }
        });
    }

    private ComponentItem getNowLight(final Activity activity) {
        return new ComponentItem("当前系统亮度（非当前窗口亮度】", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int light = getSystemBrightness(activity);
                //这个值是手动模式下设置的亮度值，跟当前是自动模式还是手动模式，没有关系
                showTipsDialog("当前系统亮度（手动模式下设置的亮度）：" + light);

            }
        });

    }


    /**
     * 获得系统亮度（亮度值是处于0-255之间的整型数值）
     *
     * @return
     */
    private int getSystemBrightness(Context context) {
        int systemBrightness = 0;
        try {
            systemBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return systemBrightness;
    }

    /**
     * 改变App当前Window亮度
     *
     * @param brightness
     */
    public void changeAppBrightness(Activity activity, int brightness) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            //表示当前window 没有自己的亮度  随着系统变化而变化
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        showTipsDialog("要设置的亮度：" + lp.screenBrightness);
        window.setAttributes(lp);
    }


}
