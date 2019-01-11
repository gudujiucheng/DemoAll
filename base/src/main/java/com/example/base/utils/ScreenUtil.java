package com.example.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;


/**
 * Created by Candice on 2015/10/22.
 */
public class ScreenUtil {

    private static int mScreenWidth;
    private static int mScreenHeight;

    private ScreenUtil() {

    }

    /**
     * reverse dp to px
     */
    public static float dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }

    /**
     * reverse px to dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String getScreenResolution(Context context) {
        return getWindowWidth(context) + "*" + getWindowHeight(context);
    }

    public static int getWindowWidth(Context context) {
        if (mScreenWidth <= 0) {
            WindowManager wm = (WindowManager) (context
                    .getSystemService(Context.WINDOW_SERVICE));
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            mScreenWidth = dm.widthPixels;
        }
        return mScreenWidth;
    }

    public static int getWindowHeight(Context context) {
        if (mScreenHeight <= 0) {
            WindowManager wm = (WindowManager) (context
                    .getSystemService(Context.WINDOW_SERVICE));
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            mScreenHeight = dm.heightPixels;
        }
        return mScreenHeight;
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0 && resources.getBoolean(id)) {
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            //获取NavigationBar的高度
            int height = resources.getDimensionPixelSize(resourceId);
            return height;
        } else {
            return 0;
        }
    }

    public static boolean isNavigationBarShow(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = context.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if (menu || back) {
                return false;
            } else {
                return true;
            }
        }
    }


}
