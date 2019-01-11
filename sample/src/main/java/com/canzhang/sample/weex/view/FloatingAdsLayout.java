package com.canzhang.sample.weex.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.bumptech.glide.Glide;
import com.example.base.utils.ScreenUtil;

import static com.example.base.utils.StringUtil.parseIntOfString;


/**
 * Created by owenli on 20171109.
 * 小浮动窗口视图
 */
public class FloatingAdsLayout extends RelativeLayout {

    private final static long CLICK_LIMIT = 200;

    private Scroller mScroller;
    private int mTouchSlop;
    public int layoutWidth, layoutHeight;
    public ImageView mIvFloatView;
    private ClickTagListener mTagListener;
    private float mDownX;
    private float mDownY;
    private float mLastPositionX;
    private float mLastPositionY;
    private long mDownTime;
    private int mFloatViewWidth = 0;
    private int mFloatViewHeight = 0;
    private int mDefaultFloatViewWidth = 0;
    private int mDefaultFloatViewHeight = 0;
    private DefaultXY mDefaultX;
    private DefaultXY mDefaultY;
    private boolean isAbsorb = true;
    private boolean isFirstInitInMeasure = true;
    private String mImageUrl;

    public FloatingAdsLayout(Context context) {
        this(context, null);
    }

    public FloatingAdsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingAdsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mScroller = new Scroller(getContext());
        setWillNotDraw(true);
        setFitsSystemWindows(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (layoutHeight != height || layoutWidth != width) {
            initFloatView(mDefaultFloatViewWidth, mDefaultFloatViewHeight, mDefaultX, mDefaultY, isFirstInitInMeasure);
            if (isFirstInitInMeasure) {
                isFirstInitInMeasure = false;
            }
        }
    }

    private void initFloatView(int width, int height, DefaultXY defaultX, DefaultXY defaultY, boolean scrollToDefaultXY) {
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();

        if (w != 0 && h != 0) {
            layoutWidth = w;
            layoutHeight = h;

            int floatViewWidth = Math.max(0, Math.min(width, layoutWidth));
            int floatViewHeight = Math.max(0, Math.min(height, layoutHeight));
            createFloatView();
            if (mFloatViewHeight != floatViewHeight || mFloatViewWidth != floatViewWidth) {
                mFloatViewHeight = floatViewHeight;
                mFloatViewWidth = floatViewWidth;
                LayoutParams params = new LayoutParams(mFloatViewWidth, mFloatViewHeight);
                mIvFloatView.setLayoutParams(params);
            }
            Glide.with(getContext()).load(mImageUrl).into(mIvFloatView);
            if (scrollToDefaultXY) {
                int x = getRealValue(defaultX, layoutWidth, mFloatViewWidth);
                int y = getRealValue(defaultY, layoutHeight, mFloatViewHeight);
                scrollTo(-x, -y);
            }
        }
    }

    private DefaultXY parseXY(String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        } else {
            DefaultXY defaultXY = new DefaultXY();
            int valueDP = Math.abs(parseIntOfString(value));
            defaultXY.value = (int) ScreenUtil.dip2px(getContext(), (float) valueDP);
            defaultXY.isPositiveNumber = ('-' == value.charAt(0)) ? false : true;
            return defaultXY;
        }
    }




    private int getRealValue(DefaultXY size, int maxSize, int imgSize) {
        if (size == null) {
            return 0;
        } else {
            int result = 0;
            if (!size.isPositiveNumber) {
                result = maxSize - imgSize - size.value;
            } else {
                result = size.value;
            }
            return result;
        }
    }

    private void createFloatView() {
        if (mIvFloatView == null) {
            mIvFloatView = new ImageView(getContext());
            addView(mIvFloatView);
            mIvFloatView.setOnTouchListener(new AdScrollViewOnTouchListener());
            mIvFloatView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }

    public void setX(final String x) {
        mDefaultX = parseXY(x);
        initFloatView(mDefaultFloatViewWidth, mDefaultFloatViewHeight, mDefaultX, mDefaultY, true);
    }

    public void setY(final String y) {
        mDefaultY = parseXY(y);
        initFloatView(mDefaultFloatViewWidth, mDefaultFloatViewHeight, mDefaultX, mDefaultY, true);
    }

    public void setImageWidth(final int imageWidth) {
        mDefaultFloatViewWidth = (int) ScreenUtil.dip2px(getContext(), (float) imageWidth);
        initFloatView(mDefaultFloatViewWidth, mDefaultFloatViewHeight, mDefaultX, mDefaultY, true);
    }

    public void setImageHeight(final int imageHeight) {
        mDefaultFloatViewHeight = (int) ScreenUtil.dip2px(getContext(), (float) imageHeight);
        initFloatView(mDefaultFloatViewWidth, mDefaultFloatViewHeight, mDefaultX, mDefaultY, true);
    }

    public void init(int width, int height, int defaultX, int defaultY) {
        mDefaultX = new DefaultXY();
        mDefaultX.isPositiveNumber = true;
        mDefaultX.value = defaultX;

        mDefaultY = new DefaultXY();
        mDefaultY.isPositiveNumber = true;
        mDefaultY.value = defaultY;

        mDefaultFloatViewHeight = height;
        mDefaultFloatViewWidth = width;
        initFloatView(mDefaultFloatViewWidth, mDefaultFloatViewHeight, mDefaultX, mDefaultY, true);
    }

    public void setImageUrl(String imgUrl) {
        mImageUrl = imgUrl;
        if (mIvFloatView != null) {
            Glide.with(getContext()).load(imgUrl).into(mIvFloatView);
        }

    }


    private class AdScrollViewOnTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    mDownX = event.getRawX();
                    mDownY = event.getRawY();
                    mLastPositionX = mDownX;
                    mLastPositionY = mDownY;
                    mDownTime = System.currentTimeMillis();
                    break;

                case MotionEvent.ACTION_MOVE:
                    float currentX = event.getRawX();
                    float currentY = event.getRawY();
                    //过滤距离过近的情况
                    if (Math.abs(mDownX - currentX) > mTouchSlop || Math.abs(mDownY - currentY) > mTouchSlop) {
                        scrollTo((int) (getScrollX() + mLastPositionX - currentX), (int) (getScrollY() + mLastPositionY - currentY));

                        mLastPositionX = currentX;
                        mLastPositionY = currentY;
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    if (isClick(event)) {
                        if (mTagListener != null) {
                            mTagListener.onClick();
                        }
                        break;
                    }
                    if (isAbsorb) {
                        doScrollByCrtOffset();
                    }
                    mDownX = 0;
                    mDownY = 0;
                    mLastPositionX = 0;
                    mLastPositionY = 0;
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        int scrollToX = Math.max(x, -layoutWidth + mFloatViewWidth);
        scrollToX = Math.min(scrollToX, 0);

        int scrollToY = Math.max(y, -layoutHeight + mFloatViewHeight);
        scrollToY = Math.min(scrollToY, 0);

        super.scrollTo(scrollToX, scrollToY);
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     * 判断是否是单击
     *
     * @param event
     * @return
     */
    private boolean isClick(MotionEvent event) {
        float offsetX = Math.abs(event.getRawX() - mDownX);
        float offsetY = Math.abs(event.getRawY() - mDownY);
        long time = System.currentTimeMillis() - mDownTime;

        if (offsetX < mTouchSlop * 2 && offsetY < mTouchSlop * 2 && time < CLICK_LIMIT) {
            return true;
        } else {
            return false;
        }
    }

    private void doScrollByCrtOffset() {
        if (Math.abs(getScrollX() - mFloatViewWidth / 2) >= getWidth() / 2) {
            scrollRight();
        } else {
            scrollLeft();
        }

    }

    /**
     * 滚动到右边
     */
    private void scrollRight() {

        final int delta = (getWidth() + getScrollX());
        mScroller.startScroll(getScrollX(), getScrollY(), -delta + mFloatViewWidth, 0,
                Math.abs(delta));
        postInvalidate();
    }

    /**
     * 滚动到左边
     */
    private void scrollLeft() {
        int delta = getScrollX();
        mScroller.startScroll(getScrollX(), getScrollY(), -delta, 0,
                Math.abs(delta));
        postInvalidate();
    }

    public void setClickListener(ClickTagListener listener) {
        mTagListener = listener;
    }

    public interface ClickTagListener {
        void onClick();
    }

    public void isAbsorb(boolean isAbsorb) {
        this.isAbsorb = isAbsorb;
    }

    private class DefaultXY {
        boolean isPositiveNumber = true;
        int value = 0;
    }
}
