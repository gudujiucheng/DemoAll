package com.canzhang.sample.manager.view.viewpager.fql.temp.banner;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;


import com.canzhang.sample.manager.view.viewpager.fql.other.CustomViewPager;
import com.canzhang.sample.manager.view.viewpager.fql.other.FixedSpeedScroller;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @Author:Owenli
 * @Create:2019/3/5
 */
public class Banner extends CustomViewPager {

    public static final int VIEWPAGER_TOTAL_NUMBER = Integer.MAX_VALUE;
    private int mSwitchTime = 5000;
    private int mDurationTime = 400;
    private AutoSwitchTask mAutoSwitchTask;
    private BannerScrolledListener mBannerScrolledListener;
    private BannerAdapter mAdapter;
    private BannerPointInterface mBannerPoint;
    private boolean rearrach = false;

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
        addOnPageChangeListener(onPageChangeListener);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setOnViewPagerTouchEventListener(onViewPagerTouchEvent);
        setDurationTime(mDurationTime);
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
                if (mBannerScrolledListener != null) {
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
            initViewPagerSwitch(list);
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

    public void setDurationTime(int time) {
        this.mDurationTime = time;
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(getContext(),
                    new AccelerateInterpolator());
            field.set(this, scroller);
            scroller.setmDuration(mDurationTime);
        } catch (Exception e) {

        }
    }

    public void setBannerSwitchTime(int time) {
        this.mSwitchTime = time;
    }

    public void initViewPagerSwitch(List list) {

        if (list != null && list.size() > 1) {
            int middle = VIEWPAGER_TOTAL_NUMBER / 2;
            int extra = middle % list.size();
            int destItem = middle - extra;
            setCurrentItem(destItem, false);
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
            setCurrentItem(getCurrentItem() + 1, false);
            setCurrentItem(getCurrentItem() - 1, false);
        }
    }

    public void stopCustomBannerSwitch() {
        if (mAutoSwitchTask != null) {
            mAutoSwitchTask.stop();
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
        rearrach = true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //解决再次attach切换无动画问题
        startCustomBannerSwitch(rearrach);
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
                doSwitch(true);
            }
        };

    }
}
