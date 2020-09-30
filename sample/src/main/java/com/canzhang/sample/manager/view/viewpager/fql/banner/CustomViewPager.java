package com.canzhang.sample.manager.view.viewpager.fql.banner;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Carl on 2015/10/20.
 */
public class CustomViewPager extends ViewPager {

    private boolean unable = false;

    private OnViewPagerTouchEvent listener;

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



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("Test"," vp dispatchTouchEvent:"+ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (listener != null) {
                    listener.onTouchDown();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (listener != null) {
                    listener.onTouchUp();
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);

    }



    public void setOnViewPagerTouchEventListener(OnViewPagerTouchEvent l){
        listener = l;
    }


    //    原文：https://blog.csdn.net/tiantianshangcha/article/details/50805050
    public interface OnViewPagerTouchEvent{
        void onTouchDown();
        void onTouchUp();
    }




}
