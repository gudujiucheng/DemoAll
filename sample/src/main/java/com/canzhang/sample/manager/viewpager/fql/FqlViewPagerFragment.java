package com.canzhang.sample.manager.viewpager.fql;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.recyclerView.bean.PageItem;
import com.canzhang.sample.manager.viewpager.fql.other.CustomViewPager;
import com.example.base.base.BaseFragment;
import com.example.base.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 分期乐项目使用的
 */
public class FqlViewPagerFragment extends BaseFragment {
    private CustomBannerVp mVp;

    private List<PageItem> mData = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sample_fragment_fql_view_pager, container, false);
        initData();
        initView(view);
        return view;
    }

    private void initView(View view) {
        mVp = view.findViewById(R.id.vp_header);
        mVp.setPointContainMargin(0, 0, 0, 0);
        mVp.addPointContainRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mVp.setPointHeightWidth((int) ScreenUtil.dip2px(mContext, 10), (int) ScreenUtil.dip2px(mContext, 4), (int) ScreenUtil.dip2px(mContext, 10));
//        mVp.setPointRes(R.drawable.consume_application_header_point);

        setAdapter();
    }

    private void setAdapter() {


        CustomBannerAdapter<PageItem> mBannerAdapter = new CustomBannerAdapter<PageItem>() {
            @Override
            protected View getViewGroupItemView(ViewGroup container, final PageItem item, int position) {

                return getItemView(container, item, mContext);
            }
        };


        mVp.setAdapterAndData(mBannerAdapter, mData);
        mBannerAdapter.setList(mData);
    }

    private View getItemView(ViewGroup container, final PageItem item, Context mContext) {
        ImageView iv = new ImageView(mContext);
        iv.setLayoutParams(new ViewGroup.LayoutParams(container.getWidth(), container.getHeight()));
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        iv.setImageResource(item.res);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), item.text, Toast.LENGTH_SHORT).show();
            }
        });
        return iv;
    }

    private void initData() {
        for (int i = 0; i < 4; i++) {
            mData.add(new PageItem(R.mipmap.ic_launcher, "xxxx" + i));
        }
    }


}
