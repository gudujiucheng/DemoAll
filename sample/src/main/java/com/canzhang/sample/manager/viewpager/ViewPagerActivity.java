package com.canzhang.sample.manager.viewpager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.recyclerView.bean.PageItem;
import com.canzhang.sample.manager.viewpager.loop.LoopPagerAdapter;
import com.example.base.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ViewPagerActivity extends BaseActivity {

    private ViewPager mVp;
    private ViewPagerIndicator mIndicator;
    private List<PageItem> mData = new ArrayList<>();
    private CustomPagerAdapter<PageItem> mCustomPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_viewpager);
        mVp = findViewById(R.id.vp);
        mIndicator = findViewById(R.id.vp_indicator);

        initData();
//        setAdapter();
        setLoopAdapter();


    }


    private void initData() {
        for (int i = 0; i < 4; i++) {
            mData.add(new PageItem(R.mipmap.ic_launcher, "xxxx" + i));
        }
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
        ImageView iv = new ImageView(ViewPagerActivity.this);
        iv.setLayoutParams(new ViewGroup.LayoutParams(container.getWidth(), container.getHeight()));
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
//                iv.setImageDrawable(getResources().getDrawable(pageItem.res));
        iv.setImageResource(pageItem.res);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewPagerActivity.this, pageItem.text, Toast.LENGTH_SHORT).show();
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

        loop();
    }

    Disposable subscribe;

    private void loop() {
        subscribe = Observable.interval(1, 1, TimeUnit.SECONDS)
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
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null) {
            subscribe.dispose();
        }
    }
}
