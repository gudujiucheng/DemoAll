package com.canzhang.sample.manager.eventdispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * 原文：https://blog.csdn.net/a62321780/article/details/51986515
 */
public class MyViewGroup extends ViewGroup {
    private MyView mChildView;

    public MyViewGroup(Context context) {
        this(context, null);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            mChildView = (MyView) getChildAt(0);
            mChildView.layout(l, t, l + mChildView.getMeasuredWidth(), t + mChildView.getMeasuredHeight());
        }
    }


    /**
     * 首先走到事件分发这里
     * 返回结果受当前View的onTouchEvent()方法或者下一级View的dispatchTouchEvent()方法返回值影响。
     * @param ev
     * @return
     *
     *
     *
     *
     *
     *
     * public boolean dispatchTouchEvent(MotionEvent ev) {
     *         boolean consume = false;
     *         if (onInterceptTouchEvent(ev)) {//是否拦截，拦截了就自己处理
     *             consume = onTouchEvent(ev);
     *         } else {//不然就继续分发
     *             consume = child.dispatchTouchEvent(ev);
     *         }
     *         return consume;
     *     }
     */

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("Test","vp dispatchTouchEvent:"+ev.getAction());
        boolean dispatch = super.dispatchTouchEvent(ev);
        Log.d("Test", "MyViewGroup:dispatchTouchEvent " + dispatch);
        return dispatch;

//        return super.dispatchTouchEvent(ev);
    }


    /**
     * 这个方法是在dispatchTouchEvent()方法内部掉用的，返回值用来判断是否拦截当前事件。
     * @param event
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean isIntercept = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isIntercept = true;
        }
        Log.d("Test", "MyViewGroup:onInterceptTouchEvent " + isIntercept);
        return isIntercept;
    }


    /**
     * 也是在dispatchTouchEvent()方法中掉用，用来处理某一事件。
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean onTouchEvent = super.onTouchEvent(event);
        Log.d("Test", "MyViewGroup:OnTouchEvent " + onTouchEvent);
        return onTouchEvent;
    }


}
