package com.canzhang.sample.manager.view.viewpager.fql.other;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by Carl on 2015/8/21.
 */
public class FixedSpeedScroller extends Scroller {
    private int mDuration = 1500;

    public FixedSpeedScroller(Context context) {
        super(context);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void setmDuration(int time){
        this.mDuration = time;
    }

    public int getmDuration(){
        return mDuration;
    }
}
