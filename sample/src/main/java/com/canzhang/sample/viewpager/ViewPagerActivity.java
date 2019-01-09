package com.canzhang.sample.viewpager;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.canzhang.sample.R;
import com.canzhang.sample.recyclerView.adapter.AppAdapter;
import com.canzhang.sample.recyclerView.bean.AppItemBean;
import com.canzhang.sample.recyclerView.bean.PageItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.base.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

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
        setAdapter();
    }

    private void initData() {
        for (int i = 0; i < 4; i++) {
            mData.add(new PageItem(R.mipmap.ic_launcher, "xxxx"));
        }

    }

    private void setAdapter() {
        mVp.setAdapter(mCustomPagerAdapter = new CustomPagerAdapter<PageItem>() {
            @Override
            protected View getViewGroupItemView(ViewGroup container, PageItem pageItem, int position) {
                ImageView iv = new ImageView(ViewPagerActivity.this);
                iv.setLayoutParams(new ViewGroup.LayoutParams(container.getWidth(), container.getHeight()));
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
//                iv.setImageDrawable(getResources().getDrawable(pageItem.res));
                iv.setImageResource(pageItem.res);
                return iv;
            }
        });
        mCustomPagerAdapter.setList(mData);
        mIndicator.setViewPager(mVp);
    }


}
