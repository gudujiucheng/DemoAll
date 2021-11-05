package com.canzhang.sample.manager.view.roundview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/** 用于需要圆角矩形框背景的LinearLayout的情况,减少直接使用LinearLayout时引入的shape资源文件 */
public class RoundLinearLayout extends LinearLayout {
    private RoundViewDelegate delegate;

    public RoundLinearLayout(Context context) {
        this(context, null);
    }

    public RoundLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        delegate = new RoundViewDelegate(this, context, attrs);
    }

    /** use delegate to set attr */
    public RoundViewDelegate getDelegate() {
        return delegate;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        if (delegate.isWidthHeightEqual() && getWidth() > 0 && getHeight() > 0) {
//            int max = Math.max(getWidth(), getHeight());
//            int measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY);
//            super.onMeasure(measureSpec, measureSpec);
//            return;
//        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        if (delegate.isRadiusHalfHeight()) {
//            delegate.setCornerRadius(getHeight() / 2);
//        }else {
//            delegate.setBgSelector();
//        }

        if(changed){
            mPath = new Path();
            int width = getWidth();
            int height = getHeight();
            int radius = Math.min(width/2, height/2);
            mPath.addCircle(getWidth()/2, getHeight()/2, radius, Path.Direction.CW);
        }

    }
    Path mPath;

    protected void onDraw(Canvas canvas) {
        canvas.clipPath(mPath);
        super.onDraw(canvas);
    }
}
