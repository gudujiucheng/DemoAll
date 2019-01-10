package com.canzhang.sample.viewpager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.canzhang.sample.R;
import com.canzhang.sample.recyclerView.bean.PageItem;
import com.canzhang.sample.viewpager.loop.LoopPagerAdapter;
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
//        setAdapter();
        setLoopAdapter();


    }




    private void initData() {
        for (int i = 0; i < 4; i++) {
            mData.add(new PageItem(R.mipmap.ic_launcher, "xxxx"+i));
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
                Toast.makeText(ViewPagerActivity.this,pageItem.text,Toast.LENGTH_SHORT).show();
            }
        });
        return iv;
    }


    private void setLoopAdapter() {
        mVp.setAdapter(new LoopPagerAdapter() {
            @Override
            public View getView(ViewGroup container, int position) {
                return getRealView(container,mData.get(position));
            }

            @Override
            public int getSize() {
                return mData.size();
            }
        });
        mIndicator.setLoopViewPager(mVp);
    }


}
