package com.example.administrator.demoall;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @Description:
 * @Author: canzhang
 * @CreateDate: 2019/3/1 11:05
 */
public class NoScrollTabView extends TabLayout {
    public NoScrollTabView(Context context) {
        super(context);
    }

    public NoScrollTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void scrollTo(int x, int y) {
//        super.scrollTo(x, y);
        //啥也不干
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e("Test",""+ev.getAction());

        if(ev.getAction()==MotionEvent.ACTION_MOVE){
            return true;
        }
        return super.onTouchEvent(ev);
    }
}
