package com.canzhang.sample.manager.view.viewpager.verticalorhorizontal;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

/**
 * 支持viewPager 竖直水平滚动效果，需配合{@link VerticalOrHorizontalViewPager}  使用
 */
public class VerticalOrHorizontalTransformer implements ViewPager.PageTransformer {
    private @VerticalOrHorizontalViewPager.Orientation
    int orientation;//方向


    public VerticalOrHorizontalTransformer(@VerticalOrHorizontalViewPager.Orientation int orientation) {
        this.orientation = orientation;
    }

    public void setOrientation(@VerticalOrHorizontalViewPager.Orientation int orientation) {
        this.orientation = orientation;
    }

    /**
     * position为0代表View处于中间，为1代表View完全处于右边，为-1代表View完全处于左边。
     *
     * @param view
     * @param position
     */
    @Override
    public void transformPage(View view, float position) {
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 1) { // [-1,1]
            view.setAlpha(1);
            if (orientation == VerticalOrHorizontalViewPager.VERTICAL) {
                // Counteract the default slide transition
                view.setTranslationX(view.getWidth() * -position);//跟手势逆向，从而保证竖向模式下，横向保持不
                //set Y position to swipe in from top
                view.setTranslationY(position * view.getHeight());//竖向按照手势比例进行纵向偏移，达到竖向滑动效果
            }else{
                //归位，避免横竖屏切换还保留之前状态的位移
                view.setTranslationX(0);
                view.setTranslationY(0);
            }
            //水平方向不需要变更，跟着手势走就好了
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }

    }
}

