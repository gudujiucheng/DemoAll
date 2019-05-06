package com.canzhang.sample.manager.view.viewpager.fql.banner;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;


import com.canzhang.sample.BuildConfig;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 2019年5月6日17:38:02
 * 修订版（支持自定义延时和插值器、自动轮播时候使用自定义延时和插值器，手动轮播场景恢复原状（防止手动滑动卡顿现象））
 */
public class Banner extends CustomViewPager {

    public static final int VIEWPAGER_TOTAL_NUMBER = Integer.MAX_VALUE;
    private int mSwitchTime = 5000;
    private int mDurationTime = 400;
    private Interpolator mInterpolator;
    private AutoSwitchTask mAutoSwitchTask;
    private BannerScrolledListener mBannerScrolledListener;
    private BannerAdapter mAdapter;
    private BannerPointInterface mBannerPoint;
    private boolean isStop = false;
    private boolean isFromAttachRefresh = false;
    private Field mScrollerField;
    Scroller mDefaultScroller = new Scroller(getContext(),
            new Interpolator() {
                @Override
                public float getInterpolation(float t) {
                    t -= 1.0f;
                    return t * t * t * t * t + 1.0f;
                }
            });

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
        addOnPageChangeListener(onPageChangeListener);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setOnViewPagerTouchEventListener(onViewPagerTouchEvent);
        setDurationTimeAndInterpolator(mDurationTime, null);
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mBannerScrolledListener != null) {
                List list = mAdapter.getData();
                if (list != null && list.size() > 0) {
                    mBannerScrolledListener.onPageScrolledOffset(positionOffset, position % list.size(), list.size());
                } else {
                    mBannerScrolledListener.onPageScrolledOffset(positionOffset, 0, 0);
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mAdapter == null) {
                return;
            }

            List list = mAdapter.getData();
            if (list != null && list.size() > 0) {
                position = position % list.size();
                if (mBannerScrolledListener != null && !isFromAttachRefresh) {
                    mBannerScrolledListener.onPageSelected(position);
                }
                if (mBannerPoint != null) {
                    mBannerPoint.onSelected(position);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private OnViewPagerTouchEvent onViewPagerTouchEvent = new CustomViewPager.OnViewPagerTouchEvent() {
        @Override
        public void onTouchDown() {
            stopCustomBannerSwitch();
        }

        @Override
        public void onTouchUp() {
            if (mAdapter != null
                    && mAdapter.getData() != null
                    && mAdapter.getData().size() > 1) {
                startCustomBannerSwitch();
            }
        }
    };

    public void setBannerPoint(BannerPointInterface bannerPoint) {
        mBannerPoint = bannerPoint;
    }

    public void setAdapter(BannerAdapter adapter) {
        setAdapter(adapter, true);
    }

    /**
     * @param adapter
     * @param isNeedStartFormCenter 是否需要从中间开启轮播
     */
    public void setAdapter(BannerAdapter adapter, boolean isNeedStartFormCenter) {
        if (adapter == null) {
            return;
        }
        super.setAdapter(adapter);
        mAdapter = adapter;
        initBannerPoint();
        List list = adapter.getData();

        if (list == null || list.size() == 0) {
            return;
        }
        if (list.size() > 1) {
            setScrollUnable(false);
            setBannerSwitchable(true);
            initViewPagerSwitch(list, isNeedStartFormCenter);
        } else {
            setScrollUnable(true);
            setBannerSwitchable(false);
            stopCustomBannerSwitch();
            mAutoSwitchTask = null;
        }
    }

    public void setBannerScrolledListener(BannerScrolledListener listener) {
        mBannerScrolledListener = listener;
    }


    /**
     * 设置动画延时时间 和插值器
     *
     * @param durationTime
     * @param interpolator
     */
    public void setDurationTimeAndInterpolator(int durationTime, Interpolator interpolator) {
        this.mInterpolator = interpolator;
        this.mDurationTime = durationTime;
        if (mInterpolator == null) {
            mInterpolator = new AccelerateInterpolator();
        }
        if (mDurationTime == 0) {
            mDurationTime = 400;
        }
        try {
            Field field = getField();
            FixedSpeedScroller scroller = new FixedSpeedScroller(getContext(),
                    mInterpolator);
            field.set(this, scroller);
            scroller.setmDuration(mDurationTime);
        } catch (Exception e) {
            Log.e("Banner", "setViewPagerDurationTime false");
        }
    }

    @NonNull
    private Field getField() throws NoSuchFieldException {
        if(mScrollerField==null){
            mScrollerField = ViewPager.class.getDeclaredField("mScroller");
            mScrollerField.setAccessible(true);
        }
        return mScrollerField;
    }


    /**
     * 设置自定义动画效果
     */
    private void setCustomAnim() {
        log("设置自定义动画效果");
        setDurationTimeAndInterpolator(mDurationTime, mInterpolator);
    }

    /**
     * 恢复默认动画效果
     */
    private void restoreDefaultAnim() {
        log("恢复默认动画效果");
        try {
            Field field = getField();
            field.set(this, mDefaultScroller);
        } catch (Exception e) {
            Log.e("Banner", "revertDurationTime false");
        }
    }

    public void setBannerSwitchTime(int time) {
        this.mSwitchTime = time;
    }

    public void initViewPagerSwitch(List list, boolean isNeedStartFormCenter) {

        if (list != null && list.size() > 1) {
            if (isNeedStartFormCenter) {
                int middle = VIEWPAGER_TOTAL_NUMBER / 2;
                int extra = middle % list.size();
                int destItem = middle - extra;
                setCurrentItem(destItem, false);
            }
            if (mAutoSwitchTask == null) {
                mAutoSwitchTask = new AutoSwitchTask(this);
            }
            mAutoSwitchTask.start();
        }
    }

    public void startCustomBannerSwitch() {
        startCustomBannerSwitch(false);
    }

    public void startCustomBannerSwitch(boolean refresh) {
        //Modify by denny pager的个数大于1时，才转换
        if (mAdapter == null) {
            return;
        }
        if (mAdapter.getData() == null || mAdapter.getData().size() <= 1) {
            return;
        }
        if (mAutoSwitchTask == null) {
            mAutoSwitchTask = new AutoSwitchTask(this);
        }
        mAutoSwitchTask.start();
        if (refresh) {
            isFromAttachRefresh = true;
            setCurrentItem(getCurrentItem() + 1, false);
            setCurrentItem(getCurrentItem() - 1, false);
            isFromAttachRefresh = false;
        }
    }

    public void stopCustomBannerSwitch() {
        if (mAutoSwitchTask != null) {
            mAutoSwitchTask.stop();
        }
        if (!isStop) {
            restoreDefaultAnim();
            isStop = true;
        }
    }

    public boolean setBannerSwitchable(boolean switchable) {
        if (mAutoSwitchTask != null && mAutoSwitchTask.getSwitchable() != switchable) {
            mAutoSwitchTask.setSwitchable(switchable);
            return true;
        }
        return false;
    }

    public boolean getBannerSwitchable() {
        return mAutoSwitchTask != null && mAutoSwitchTask.getSwitchable();
    }

    private void initBannerPoint() {
        int size = 0;
        List list = mAdapter.getData();
        if (list != null && list.size() > 1) {
            size = list.size();
        }

        if (mBannerPoint != null) {
            mBannerPoint.initPoint(size);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopCustomBannerSwitch();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //解决再次attach切换无动画问题
        startCustomBannerSwitch(true);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        switch (visibility) {
            case VISIBLE:
                if (getVisibility() == View.VISIBLE) {
                    startCustomBannerSwitch();
                }
                break;
            case GONE:
                stopCustomBannerSwitch();
                break;
            case INVISIBLE:
            default:
                stopCustomBannerSwitch();
                break;
        }
    }

    public interface BannerScrolledListener {
        void onPageScrolledOffset(float positionOffset, int position, int size);

        void onPageSelected(int position);
    }

    class AutoSwitchTask extends Handler {
        private ViewPager viewPager;
        private boolean canSwitch = true;
        private boolean isStart = true;

        AutoSwitchTask(ViewPager viewPager) {
            this.viewPager = viewPager;
        }

        public void start() {
            if (isStart) {
                stop();
                postDelayed(runnable, mSwitchTime);
                isStart = false;
            }
        }

        public void stop() {
            isStart = true;
            removeCallbacks(runnable);
        }

        private void doSwitch(boolean isContinue) {
            if (canSwitch) {
                int position = viewPager.getCurrentItem();
                PagerAdapter adapter = viewPager.getAdapter();
                if (adapter != null) {
                    if (position != adapter.getCount() - 1) {
                        viewPager.setCurrentItem(++position);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                    if (isContinue) {
                        postDelayed(runnable, mSwitchTime);
                    }
                }
            }
        }

        public void setSwitchable(boolean switchable) {
            canSwitch = switchable;
            removeCallbacks(runnable);
            doSwitch(true);
        }

        public boolean getSwitchable() {
            return canSwitch;
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isStop) {
                    setCustomAnim();
                    isStop = false;
                }

                doSwitch(true);
            }
        };

    }

    private void log(String tips) {
        if (BuildConfig.DEBUG) {
            Log.e("Banner", tips);
        }
    }
}
