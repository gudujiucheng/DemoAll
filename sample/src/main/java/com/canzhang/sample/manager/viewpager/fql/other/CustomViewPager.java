package com.canzhang.sample.manager.viewpager.fql.other;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Carl on 2015/10/20.
 */
public class CustomViewPager extends ViewPager {

    private boolean unable = false;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollUnable(boolean unable) {
        this.unable = unable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (unable) {
            return false;
        } else {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (Exception e) {
                return false;
            }
        }
    }

}
