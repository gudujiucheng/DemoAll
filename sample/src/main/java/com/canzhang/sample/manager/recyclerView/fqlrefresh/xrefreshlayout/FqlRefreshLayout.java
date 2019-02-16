package com.canzhang.sample.manager.recyclerView.fqlrefresh.xrefreshlayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import com.canzhang.sample.manager.recyclerView.fqlrefresh.xrefreshlayout.loadinglayout.FqlDefaultLoadingLayout;
import com.canzhang.sample.manager.recyclerView.fqlrefresh.xrefreshlayout.loadinglayout.ILoadingLayout;
import com.example.base.utils.ScreenUtil;


public class FqlRefreshLayout extends FrameLayout implements NestedScrollingParent {


    private int MIN_LOADING_LAYOUG_HEIGHT = 40;
    private int MAX_LOADING_LAYOUG_HEIGHT;
    private int MAX_DURATION = 2000;
    //range when overscroll header or footer
    private int OVERSCROLL_RANGE = 200;

    private ILoadingLayout loadingLayout;
    private View header;
    private View refreshView;
    private OverScroller scroller;
    private boolean isNeedInitLoadingLayout = false;

    boolean isRelease = false;
    private boolean isSmoothScrolling = false;

    public FqlRefreshLayout(Context context) {
        this(context, null);
    }

    public FqlRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FqlRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        MIN_LOADING_LAYOUG_HEIGHT = (int)ScreenUtil.dip2px(context, MIN_LOADING_LAYOUG_HEIGHT);
        OVERSCROLL_RANGE = (int)ScreenUtil.dip2px(context, OVERSCROLL_RANGE);

        scroller = new OverScroller(getContext());
        //默认选项
        loadingLayout = new FqlDefaultLoadingLayout();
    }


    protected void initLoadingLayout() {
        if (header != null) removeView(header);


        header = loadingLayout.createLoadingHeader(getContext(), this);


        addView(header);


        //init header and footer view.
        loadingLayout.initAndResetHeader();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 1) {
            throw new IllegalArgumentException("FqlRefreshLayout must have only 1 child to pull!");
        }
        refreshView = getChildAt(0);

        //create loading layout:  header and footer
        initLoadingLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        MAX_LOADING_LAYOUG_HEIGHT = getMeasuredHeight() / 2;
        measureHeader(widthMeasureSpec, header);
        refreshView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
    }

    /**
     * limit header  min-height, max-height
     *
     * @param widthMeasureSpec
     * @param view
     */
    private void measureHeader(int widthMeasureSpec, View view) {
        int height = Math.max(MIN_LOADING_LAYOUG_HEIGHT, view.getMeasuredHeight());
        height = Math.min(height, MAX_LOADING_LAYOUG_HEIGHT);
        view.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        header.layout(0, -header.getMeasuredHeight(), header.getMeasuredWidth(), 0);
        refreshView.layout(0, 0, refreshView.getMeasuredWidth(), refreshView.getMeasuredHeight());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isSmoothScrolling) return true;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes) {
        isRelease = false;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int nestedScrollAxes) {
        loadingLayout.initAndResetHeader();
        isPullHeader = false;
        return true;
    }

    /**
     * when release from XRefreshLayout!
     *
     * @param child
     */
    @Override
    public void onStopNestedScroll(@NonNull View child) {
        isRelease = true;
        if (isPullHeader) {
            if (getScrollY() <= -header.getMeasuredHeight()) {
                //header shown fully.
                int dy = -header.getMeasuredHeight() - getScrollY();
                smoothScroll(dy);
                loadingLayout.onHeaderRefreshing();
                if (listener != null) {
                    listener.onRefresh();
                }
            } else {
                //hide header smoothly.
                isNeedInitLoadingLayout = true;
                int dy = 0 - getScrollY();
                smoothScroll(dy);

            }
        } else {
            //hide footer smoothly.
            isNeedInitLoadingLayout = true;
            int dy = 0 - getScrollY();
            smoothScroll(dy);
        }
    }


    /**
     * smooth scroll to target val.
     *
     * @param dy
     */
    private void smoothScroll(int dy) {
        int duration = calculateDuration(Math.abs(dy));
        scroller.startScroll(0, getScrollY(), 0, dy, duration);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * calculate the duration for animation by dy.
     *
     * @param dy
     * @return
     */
    private int calculateDuration(int dy) {
        float fraction = dy * 1F / MAX_LOADING_LAYOUG_HEIGHT;
        return (int) (fraction * MAX_DURATION);
    }

    boolean isPullHeader;
    float dy;

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, @NonNull int[] consumed) {
        this.dy = dy;

        isPullHeader = (dy < 0 && getScrollY() <= 0 && !ViewCompat.canScrollVertically(refreshView, -1))
                || (dy >= 0 && getScrollY() < 0);


        if (isPullHeader) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }

    }

    private int getHeaderScrollRange() {
        return header.getMeasuredHeight() + OVERSCROLL_RANGE;
    }



    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void scrollTo(@Px int x, @Px int y) {
        if (isPullHeader) {
            if (y < -getHeaderScrollRange()) {
                y = -getHeaderScrollRange();
            } else if (y > 0) {
                y = 0;
            }

            //call percent
            float percent = Math.abs(y) * 1f / header.getMeasuredHeight();
            percent = Math.min(percent, 1f);
            if (!isRelease) {
                loadingLayout.onPullHeader(percent);
            }

        }
        super.scrollTo(x, y);
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        isSmoothScrolling = isRelease && Math.abs(scroller.getCurrY()) > 8;
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            //animation finish.
            if (isNeedInitLoadingLayout) {
                Log.d("Test","scroll finish, call initAndReset Header!");
                isNeedInitLoadingLayout = false;
                loadingLayout.initAndResetHeader();
            }
        }
    }


    /**
     * set your custom loadinglayout.
     *
     * @param loadingLayout
     */
    public void setLoadingLayout(ILoadingLayout loadingLayout) {
        if (isRelease && isSmoothScrolling) return;
        this.loadingLayout = loadingLayout;
        initLoadingLayout();
        requestLayout();
    }

    /**
     * complete the refresh state!
     */
    public void completeRefresh() {
        isNeedInitLoadingLayout = true;
        smoothScroll(0 - getScrollY());
        if (loadingLayout != null) {
            loadingLayout.onHeaderCompleteRefresh();
        }

    }

    private OnRefreshListener listener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }


}
