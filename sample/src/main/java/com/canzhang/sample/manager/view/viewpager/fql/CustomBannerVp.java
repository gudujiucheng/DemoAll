package com.canzhang.sample.manager.view.viewpager.fql;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.canzhang.sample.R;
import com.canzhang.sample.manager.view.viewpager.fql.other.CustomViewPager;
import com.canzhang.sample.manager.view.viewpager.fql.other.FixedSpeedScroller;
import com.example.base.utils.ScreenUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 分期乐vp，功能比较完善
 * <p>
 * <p>
 * <p>
 * Created by Carl on 2015/10/14.
 * 功能：1、设置该组件的大小
 * 2、设置vp跳转间隔时间、设置vp跳转过程时间
 * 3、设置组件的adapter
 * 4、黄点切换
 * 5、大于1和等于1的情景
 */
public class CustomBannerVp extends RelativeLayout implements ViewPager.OnPageChangeListener {

    //默认值
    private int width = (int) ScreenUtil.dip2px(getContext(), 8);
    private int height = (int) ScreenUtil.dip2px(getContext(), 8);
    private int space = (int) ScreenUtil.dip2px(getContext(), 10);

    private boolean isNeedPointLeftSpace = true;


    public static final int VIEWPAGER_TOTAL_NUMBER = 60 * 60;
    private final Context mContext;
    private CustomViewPager mVpCustom;
    private LinearLayout mLlPointContain;
    private View mView;
    private int mSwitchTime = 5000;
    private int mDurationTime = 400;
    private AutoSwitchTask mAutoSwitchTask;
    private List mList = new ArrayList();
    private boolean isStopSwitch = false;
    private int mPointRes = R.drawable.sample_default_point;
    private BannerScrolledListener mBannerScrolledListener;


    public CustomBannerVp(Context context) {
        this(context, null);
    }

    public CustomBannerVp(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomBannerVp(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mView = View.inflate(getContext(), R.layout.sample_custom_banner_vp, this);
        mVpCustom = (CustomViewPager) mView.findViewById(R.id.mVpCustom);
        mLlPointContain = (LinearLayout) mView.findViewById(R.id.mLlPointContain);
        mVpCustom.addOnPageChangeListener(this);
        mVpCustom.setOverScrollMode(OVER_SCROLL_NEVER);
        mVpCustom.setOnViewPagerTouchEventListener(new CustomViewPager.OnViewPagerTouchEvent() {
            @Override
            public void onTouchDown() {
                stopCustomBannerSwitch();
            }

            @Override
            public void onTouchUp() {
                startCustomBannerSwitch();
            }
        });

        //触摸时不能轮播    不好使  不能响应 down事件了，被item给消费掉了
//        mVpCustom.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
////                Log.e("Test","onTouch"+event.getAction());
//                //点击  移动  抬起
//                if (event.getAction()==MotionEvent.ACTION_UP||event.getAction()==MotionEvent.ACTION_CANCEL){
//                    //重新轮播
//                    startCustomBannerSwitch();
//                }else if(event.getAction()==MotionEvent.ACTION_DOWN){
//                    //停止轮播
//                    stopCustomBannerSwitch();
//                }
//                return false;
//            }
//        });

    }


    public void setAdapterAndData(PagerAdapter adapter, List list) {
        mVpCustom.setAdapter(adapter);
        this.mList = list;
        initBannerPoint(mPointRes);

        if (mList == null || mList.size() == 0) {
            return;
        }
        if (mList.size() > 1) {
            mVpCustom.setScrollUnable(false);
            setBannerSwitchable(true);
            initViewPagerSwitch();
        } else {
            mVpCustom.setScrollUnable(true);
            setBannerSwitchable(false);
            stopCustomBannerSwitch();
            mAutoSwitchTask = null;
        }
    }

    public void setPointRes(int pointRes) {
        mPointRes = pointRes;
    }

    public void setPointContainRightOfParent() {
        if (mLlPointContain == null) {
            return;
        }
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        int right = (int) ScreenUtil.dip2px(mContext, 12);
        lp.setMargins(0, 0, right, 0);
        mLlPointContain.setLayoutParams(lp);
    }

    /**
     * 设置指示器相对 margin
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setPointContainMargin(int left, int top, int right, int bottom) {
        LayoutParams lp = getPointContainLayoutParams();
        if (lp == null) {
            return;
        }
        lp.setMargins(left, top, right, bottom);
    }

    /**
     * 设置摆放规则
     * 参见RelativeLayout 的rule  例如：RelativeLayout.ALIGN_PARENT_RIGHT
     *
     * @param rule
     */
    public void addPointContainRule(int rule) {
        LayoutParams lp = getPointContainLayoutParams();
        if (lp == null) {
            return;
        }
        lp.addRule(rule);
    }

    private LayoutParams getPointContainLayoutParams() {
        if (mLlPointContain == null) {
            return null;
        }
        ViewGroup.LayoutParams layoutParams = mLlPointContain.getLayoutParams();
        if (layoutParams instanceof LayoutParams) {
            return (LayoutParams) layoutParams;
        }
        return null;
    }


//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        Log.e("vp", "当前动作" + ev.getAction());
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN://0
//            case MotionEvent.ACTION_MOVE://2
//            case MotionEvent.ACTION_SCROLL://8
//                stopCustomBannerSwitch();
//                isStopSwitch = true;
//                break;
//            case MotionEvent.ACTION_UP://1
//            case MotionEvent.ACTION_CANCEL://3
//                if (mList.size() > 1) {
//                    Log.e("vp", "手指离开动作" + ev.getAction());
//                    startCustomBannerSwitch();
//                }
//                break;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }

    public void setBannerScrolledListener(BannerScrolledListener listener) {
        mBannerScrolledListener = listener;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mBannerScrolledListener != null) {
            mBannerScrolledListener.onPageScrolledOffset(positionOffset, position % mList.size(), mList.size());
        }

    }

    @Override
    public void onPageSelected(int position) {
        if (mList != null && mList.size() > 0) {
            position = position % mList.size();
            if (mBannerScrolledListener != null) {
                mBannerScrolledListener.onPageSelected(position);
            }
            for (int i = 0; i < mLlPointContain.getChildCount(); i++) {
                View view = mLlPointContain.getChildAt(i);
                view.setEnabled(position != i);
            }
            isStopSwitch = false;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
//        if (state == 2) {
//            if (isStopSwitch) {
//                if (mList.size() > 1) {
//                    Log.e("vp","onPageScrollStateChanged 触发重新启动");
//                    startCustomBannerSwitch();
//                }
//                isStopSwitch = false;
//            }
//        }
    }

    public void setDurationTime(int time) {
        this.mDurationTime = time;
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mVpCustom.getContext(),
                    new AccelerateInterpolator());
            field.set(mVpCustom, scroller);
            scroller.setmDuration(mDurationTime);
        } catch (Exception e) {
            Log.e("CustomBannerVp", "setViewPagerDurationTime false");
        }
    }

    public void setBannerSwitchTime(int time) {
        this.mSwitchTime = time;
    }

    public void initViewPagerSwitch() {

        if (mList != null && mList.size() > 1) {
            int middle = VIEWPAGER_TOTAL_NUMBER / 2;
            int extra = middle % mList.size();
            int destItem = middle - extra;
            mVpCustom.setCurrentItem(destItem, false);
            if (mAutoSwitchTask == null) {
                mAutoSwitchTask = new AutoSwitchTask(mVpCustom);
            }
            mAutoSwitchTask.start();
        }
    }

    public void startCustomBannerSwitch() {
        //Modify by denny pager的个数大于1时，才转换
        if (mVpCustom == null) {
            return;
        }
        if (mList.size() <= 1) {
            return;
        }
        if (mAutoSwitchTask == null) {
            mAutoSwitchTask = new AutoSwitchTask(mVpCustom);
        }
        mAutoSwitchTask.start();
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

    private void initBannerPoint(int pointRes) {
        mLlPointContain.removeAllViews();
        if (mList == null || mList.size() <= 1) {
            return;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        for (int i = 0; i < mList.size(); i++) {
            View point = new View(getContext());
            point.setBackgroundResource(pointRes);
            point.setEnabled(true);
            if (i != 0) {
                if (isNeedPointLeftSpace) {
                    params.leftMargin = space;
                }

            } else {
                point.setEnabled(false);
            }
            mLlPointContain.addView(point, params);
        }
    }

    /**
     * 设置宽高以及间距
     *
     * @param width
     * @param height
     * @param space  点间距
     */
    public void setPointHeightWidth(int width, int height, int space) {
        this.width = width;
        this.height = height;
        this.space = space;

    }

    public void isNeedLeftMargin(boolean isNeed) {
        isNeedPointLeftSpace = isNeed;
    }

    public void setPintsVisible(int visible) {
        mLlPointContain.setVisibility(visible);
    }

    public void setCurrentItem(int item) {
        mVpCustom.setCurrentItem(item);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopCustomBannerSwitch();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startCustomBannerSwitch();
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
                stopCustomBannerSwitch();
                break;
        }
    }

    public interface BannerScrolledListener {
        void onPageScrolledOffset(float positionOffset, int position, int size);

        void onPageSelected(int position);
    }

    class AutoSwitchTask extends Handler implements Runnable {
        private ViewPager viewPager;
        private boolean canSwitch = true;
        private boolean isStart = true;

        AutoSwitchTask(ViewPager viewPager) {
            this.viewPager = viewPager;
        }

        public void start() {
            if (isStart) {
                stop();
                postDelayed(this, mSwitchTime);
                isStart = false;
            }
        }

        public void stop() {
            isStart = true;
            removeCallbacks(this);
        }

        @Override
        public void run() {
            doSwitch(true);
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
                        postDelayed(this, mSwitchTime);
                    }
                }
            }
        }

        public void setSwitchable(boolean switchable) {
            canSwitch = switchable;
            removeCallbacks(this);
            doSwitch(true);
        }

        public boolean getSwitchable() {
            return canSwitch;
        }

    }

    public CustomViewPager getViewPager() {
        return mVpCustom;
    }

}
