package com.canzhang.sample.manager.viewpager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.recyclerView.bean.PageItem;
import com.canzhang.sample.manager.viewpager.loop.LoopPagerAdapter;
import com.example.base.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 最新可参考项目：https://blog.csdn.net/jijinchao2015/article/details/53908298
 *
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
        for (int i = 0; i < 4; i++) {
            mData.add(new PageItem(R.mipmap.ic_launcher, "xxxx" + i));
        }
//        initNormal();
        initLoop();
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

    private void initView(View view) {
        mVp = view.findViewById(R.id.vp);
        mIndicator = view.findViewById(R.id.vp_indicator);
    }


    private void setAdapter() {
        mVp.setAdapter(mCustomPagerAdapter = new CustomPagerAdapter<PageItem>() {
            @Override
            protected View getViewGroupItemView(ViewGroup container, PageItem pageItem, int position) {
                return getRealView(container, pageItem);
            }
        });

        mCustomPagerAdapter.setList(mData);
        mIndicator.setViewPager(mVp);

    }


    @NonNull
    private View getRealView(ViewGroup container, final PageItem pageItem) {
        ImageView iv = new ImageView(getContext());
        iv.setLayoutParams(new ViewGroup.LayoutParams(container.getWidth(), container.getHeight()));
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
//                iv.setImageDrawable(getResources().getDrawable(pageItem.res));
        iv.setImageResource(pageItem.res);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), pageItem.text, Toast.LENGTH_SHORT).show();
            }
        });
        return iv;
    }


    private void setLoopAdapter() {
        mVp.setAdapter(new LoopPagerAdapter() {
            @Override
            public View getView(ViewGroup container, int position) {
                return getRealView(container, mData.get(position));
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
