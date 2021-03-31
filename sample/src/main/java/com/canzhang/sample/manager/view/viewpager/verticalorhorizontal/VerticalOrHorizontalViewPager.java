package com.canzhang.sample.manager.view.viewpager.verticalorhorizontal;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.IntDef;
import androidx.viewpager.widget.ViewPager;

import com.canzhang.sample.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 支持viewPager 竖直水平滚动效果，需配合{@link VerticalOrHorizontalTransformer}  使用
 */
public class VerticalOrHorizontalViewPager extends ViewPager {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private @Orientation int orientation;//方向
    private VerticalOrHorizontalTransformer verticalOrHorizontalTransformer;

    @IntDef({HORIZONTAL, VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {

    }
    public VerticalOrHorizontalViewPager(Context context) {
        this(context,null);
    }

    public VerticalOrHorizontalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomAttributes(attrs);
    }


    private void setCustomAttributes(AttributeSet attrs) {
        if(attrs==null){
            setPageTransformer(true, verticalOrHorizontalTransformer = new VerticalOrHorizontalTransformer(HORIZONTAL));
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.VerticalOrHorizontalViewPager);
        orientation = a.getInt(R.styleable.VerticalOrHorizontalViewPager_orientation, HORIZONTAL);
        setPageTransformer(true, verticalOrHorizontalTransformer = new VerticalOrHorizontalTransformer(HORIZONTAL));
        a.recycle();
    }


    /**
     * 把手势的左右滑动变成上下滑动（要使得 ViewPager 竖向滑动，将 MotionEvent 的 x 坐标转换成 y 坐标，将 y 坐标转换成 x 坐标。）
     * @param event
     * @return
     */
    private MotionEvent swapTouchEvent(MotionEvent event) {
        float width = getWidth();
        float height = getHeight();
        event.setLocation((event.getY() / height) * width, (event.getX() / width) * height);
        return event;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (orientation == HORIZONTAL) {//水平滚动
            return super.onInterceptTouchEvent(event);
        }else{//竖直滚动
            return super.onInterceptTouchEvent(swapTouchEvent(MotionEvent.obtain(event)));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (orientation == HORIZONTAL) {//水平滚动
            return super.onTouchEvent(ev);
        }else{//竖直滚动
            return super.onTouchEvent(swapTouchEvent(MotionEvent.obtain(ev)));
        }
    }


    /**
     * 设置view的动画transform,针对竖直方向设置竖直动作的 transform
     * @param orientation
     */
    public void setOrientation(@Orientation int orientation) {
        this.orientation = orientation;
       if(verticalOrHorizontalTransformer !=null){
           verticalOrHorizontalTransformer.setOrientation(orientation);
       }
    }
}