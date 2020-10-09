package com.canzhang.sample.manager.view.viewpager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.view.recyclerView.bean.PageItem;
import com.canzhang.sample.manager.view.viewpager.fql.banner.FixedSpeedScroller;
import com.canzhang.sample.manager.view.viewpager.fql.transformer.TranslationXPageTransformer;
import com.canzhang.sample.manager.view.viewpager.loop.LoopPagerAdapter;
import com.example.base.base.BaseFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


/**
 * 最新可参考项目：https://blog.csdn.net/jijinchao2015/article/details/53908298
 * <p>
 * TODO 无限轮播 平滑滚动是否有问题的
 */
public class ViewPagerFragment extends BaseFragment {
    private ViewPager mVp;
    private ViewPagerIndicator mIndicator;
    private List<PageItem> mData = new ArrayList<>();
    private CustomPagerAdapter<PageItem> mCustomPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sample_fragment_view_pager, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            mData.add(new PageItem(R.mipmap.ic_launcher, "xxxx" + i));
        }
        initNormal();
//        initLoop();
    }

    /**
     * 可循环的
     */
    private void initLoop() {
        setLoopAdapter();
        loop(1);
        setTouchListener();
    }

    /**
     * 常规的
     */
    private void initNormal() {
        setAdapter();
    }

    int i = 0;

    private void initView(View view) {
        mVp = view.findViewById(R.id.vp);
        mVp.setPageTransformer(true, new TranslationXPageTransformer());
        mIndicator = view.findViewById(R.id.vp_indicator);

        view.findViewById(R.id.bt_switch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //触发切换是有 onPageSelected 回调的
                mVp.setCurrentItem(i % mData.size());
                i++;
            }
        });
        view.findViewById(R.id.bt_switch_scroll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //触发切换是有 onPageSelected 回调的
                mVp.setCurrentItem(i % mData.size(), true);
                i++;
            }
        });
        view.findViewById(R.id.bt_set_anim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVp.setPageTransformer(i % 2 == 0, new TranslationXPageTransformer());
            }
        });
        final EditText etTime = view.findViewById(R.id.et_time);
        view.findViewById(R.id.bt_set_anim_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = etTime.getText().toString();
                int time = 0;
                if (!TextUtils.isEmpty(s)) {
                    time = Integer.parseInt(s);
                }
                setDurationTime(time);
            }
        });

    }


    public void setDurationTime(int time) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mVp.getContext(),
                    new DecelerateInterpolator());
            field.set(mVp, scroller);
            scroller.setmDuration(time);
        } catch (Exception e) {
            Log.e("CustomBannerVp", "setViewPagerDurationTime false");
        }
    }


    private void setAdapter() {
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                log("onPageSelected 当前position：" + position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mCustomPagerAdapter = new CustomPagerAdapter<PageItem>() {
            @Override
            protected View getViewGroupItemView(ViewGroup container, PageItem pageItem, int position) {
                return getRealView(container, pageItem, position);
            }
        };
        mCustomPagerAdapter.setList(mData);
        mVp.setAdapter(mCustomPagerAdapter);
        mIndicator.setViewPager(mVp);

    }


    @NonNull
    private View getRealView(ViewGroup container, final PageItem pageItem, int position) {
        View layout = LayoutInflater.from(container.getContext()).inflate(R.layout.sample_fql_vp_item, container, false);
        ImageView ivBgIng = layout.findViewById(R.id.iv_bg_img);
        ImageView ivImg = layout.findViewById(R.id.iv_img);
        layout.setTag("zc:" + position);
        ivBgIng.setImageResource(R.drawable.sample_ic_vp_banner_bg);
        ivImg.setImageResource(R.drawable.sample_ic_vp_banner);
        ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), pageItem.text, Toast.LENGTH_SHORT).show();
            }
        });

        return layout;
    }


    private void setLoopAdapter() {
        mVp.setAdapter(new LoopPagerAdapter() {
            @Override
            public View getView(ViewGroup container, int position) {
                return getRealView(container, mData.get(position), position);
            }

            @Override
            public int getSize() {
                return mData.size();
            }
        });
        mIndicator.setLoopViewPager(mVp);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener() {
        //通过mViewPager去设置触摸滑动的点击事件
        mVp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        stop();
                        log("ACTION_MOVE");
                        //移除回调函数和消息
                    case MotionEvent.ACTION_DOWN:
                        stop();
                        log("ACTION_DOWN");
                        break;
                    //当你触摸时停止自动滑动
                    default:
                    case MotionEvent.ACTION_UP:
                        loop(3);
                        log("ACTION_UP");
                        break;
                }
                return false;
            }
        });
    }

    Disposable subscribe;

    private void loop(int delay) {
        subscribe = Observable.interval(delay, 2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long value) throws Exception {
                        log("接收到了事件" + value + "  " + Thread.currentThread().getName());
                        if (mVp != null) {
                            mVp.setCurrentItem(mVp.getCurrentItem() + 1);
                        }

                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
    }

    private void stop() {
        if (subscribe != null) {
            subscribe.dispose();
        }

        log("停止------------------------停止--------------停止---");
    }
}
