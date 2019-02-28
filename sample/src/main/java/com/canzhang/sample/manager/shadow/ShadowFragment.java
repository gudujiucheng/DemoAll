package com.canzhang.sample.manager.shadow;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.canzhang.sample.R;
import com.example.base.base.BaseFragment;
import com.sxu.shadowdrawable.ShadowDrawable;

import static com.zhouwei.indicatorview.DisplayUtils.dpToPx;

/**
 * 阴影效果测试
 * 参考文章：https://mp.weixin.qq.com/s?__biz=MzIxNjc0ODExMA==&mid=2247484967&idx=1&sn=d3bf45f46dd83e5eaca2ffc7a90e9ee3&chksm=97851f06a0f296108abd0dffdbf0f0a749ec52d530aff2395fc467e6ad3eb9d3fe0e9c000feb&mpshare=1&scene=1&srcid=0226uULyzwPolyP0NUpUUpEw#rd
 */
public class ShadowFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sample_fragment_shadow, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {

    }

    private void initView(View view) {
        View viewTest = view.findViewById(R.id.v_test);
        ShadowDrawable.setShadowDrawable(viewTest, Color.parseColor("#3D5AFE"), dpToPx(8),
                Color.parseColor("#66000000"), dpToPx(10), 0, 0);

    }

}
