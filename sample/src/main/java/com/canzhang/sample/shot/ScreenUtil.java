package com.canzhang.sample.shot;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class ScreenUtil {
    public static DisplayMetrics mDisplayMetrics = null;

    public static DisplayMetrics getScreenDisplayMetrics(Context context) {
        if (mDisplayMetrics != null) {
            return mDisplayMetrics;
        }
        mDisplayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getMetrics(mDisplayMetrics);
        } else {
            display.getRealMetrics(mDisplayMetrics);
        }
        return mDisplayMetrics;
    }


    /**
     * 获取屏幕相关参数
     *
     * @param context context
     * @return DisplayMetrics 屏幕高(PX)
     */
    public static int getScreenHeight(Context context) {
        return getScreenDisplayMetrics(context).heightPixels;
    }

    /**
     * 获取屏幕相关参数
     *
     * @return DisplayMetrics 屏幕宽(PX)
     */
    public static int getScreenWidth(Context context) {
        return getScreenDisplayMetrics(context).widthPixels;
    }

    /**
     * 获取屏幕density
     *
     * @param context context
     * @return density 屏幕density
     */
    public static float getScreenDensity(Context context) {
        return getScreenDisplayMetrics(context).density;
    }

    /**
     * 获取屏幕density
     *
     * @param context context
     * @return density 屏幕density
     */
    public static int getScreenDensityDpi(Context context) {
        return mDisplayMetrics.densityDpi;
    }
}
