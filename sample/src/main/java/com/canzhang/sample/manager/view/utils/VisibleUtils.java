package com.canzhang.sample.manager.view.utils;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.WindowManager;

/**
 * @Description: 监测当前控件在屏幕是否可见
 * @Author: canzhang
 * @CreateDate: 2019/6/5 15:31
 */
public class VisibleUtils {


    /**
     * https://blog.csdn.net/qq_26420489/article/details/51203088
     *
     * @return
     */
    public static boolean getCurrentViewIsVisible(View view) {
        if (view == null) {
            return false;
        }
        // 监听屏幕滑动状态-当控件可见时，执行动画
        Point p = new Point();
        WindowManager wm = (WindowManager) (view.getContext()
                .getSystemService(Context.WINDOW_SERVICE));
        wm.getDefaultDisplay().getSize(p);
        int screenWidth = p.x;
        int screenHeight = p.y;
        Rect rect = new Rect(0, 0, screenWidth, screenHeight);
        int[] location = new int[2];
        view.getLocationInWindow(location);
        if (view.getLocalVisibleRect(rect)) {
            // 控件在屏幕可见区域
            return true;
        } else {
            // 控件已不在屏幕可见区域（已滑出屏幕）
            return false;
        }

    }

}
