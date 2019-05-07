package com.canzhang.sample.manager.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.canzhang.sample.R;


/**
 * @Description: 虚线
 * @Author: canzhang
 * @CreateDate: 2019/2/12 11:28
 */
public class DashLineView extends View {


    private Paint paint = null;
    private Path path = null;
    private PathEffect effects = null;

    private int orientation;//方向
    private int lineColor;//线条颜色
    private int lineClearance;//线条间距

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private static final int DEFAULT_LINE_CLEARANCE = 2;
    private static final int DEFAULT_COLOR_THEME = Color
            .parseColor("#ff666666");

    public DashLineView(Context context) {
        this(context, null);
    }

    public DashLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomAttributes(attrs);
    }

    private void setCustomAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DashLineView);
        lineColor = a.getColor(R.styleable.DashLineView_dash_line_color,
                DEFAULT_COLOR_THEME);
        orientation = a.getInt(R.styleable.DashLineView_dash_line_orientation, HORIZONTAL);
        lineClearance = a.getDimensionPixelSize(
                R.styleable.DashLineView_dash_line_clearance, (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                DEFAULT_LINE_CLEARANCE, getResources()
                                        .getDisplayMetrics()));// 默认为10dp
        paint = new Paint();
        path = new Path();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(lineColor);
        paint.setAntiAlias(true);
        // PathEffect是用来控制绘制轮廓(线条)的方式
        // 代码中的float数组,必须是偶数长度,且>=2,指定了多少长度的实线之后再画多少长度的空白.如本代码中,绘制长度5的实线,再绘制长度5的空白,再绘制长度5的实线,再绘制长度5的空白,依次重复.1是偏移量,可以不用理会.

        effects = new DashPathEffect(new float[]{lineClearance, lineClearance, lineClearance, lineClearance}, 1);
        a.recycle();
    }


    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.moveTo(0, 0);
        if (orientation == VERTICAL) {
            path.lineTo(0, this.getHeight());
        } else {
            path.lineTo(this.getWidth(), 0);
        }

        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        paint.setStrokeWidth(getMeasuredHeight()*2);
    }
}
