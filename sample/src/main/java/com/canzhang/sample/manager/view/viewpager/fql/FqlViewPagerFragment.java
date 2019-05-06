package com.canzhang.sample.manager.view.viewpager.fql;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.view.recyclerView.bean.PageItem;
import com.canzhang.sample.manager.view.viewpager.fql.banner.Banner;
import com.canzhang.sample.manager.view.viewpager.fql.banner.BannerAdapter;
import com.canzhang.sample.manager.view.viewpager.fql.banner.NewBigBannerPoint;
import com.canzhang.sample.manager.view.viewpager.fql.transformer.TranslationXPageTransformer;
import com.example.base.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 分期乐项目使用的
 */
public class FqlViewPagerFragment extends BaseFragment {
    private Banner mVp;

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
        LinearLayout llPointContain = view.findViewById(R.id.ll_point_contain);
        mVp.setBannerPoint(new NewBigBannerPoint(view.getContext(), llPointContain, R.drawable.sample_consume_application_header_point));
        mVp.setDurationTimeAndInterpolator(700, new DecelerateInterpolator());
        mVp.setPageTransformer(true, new TranslationXPageTransformer());
        setAdapter();
    }

    private void setAdapter() {

        BannerAdapter<PageItem> mBannerAdapter = new BannerAdapter<PageItem>(mData) {
            @Override
            public View createView(ViewGroup container, PageItem pageItem, int position) {
                return getItemView(container, pageItem, mContext);
            }

            @Override
            public void bindData(View view, PageItem pageItem, int position) {

            }

        };
        mVp.setAdapter(mBannerAdapter);

    }

    private View getItemView(ViewGroup container, final PageItem item, Context context) {
        View layout = LayoutInflater.from(context).inflate(R.layout.sample_fql_vp_item, container, false);
        ImageView ivBgIng = layout.findViewById(R.id.iv_bg_img);
        ImageView ivImg = layout.findViewById(R.id.iv_img);
        ivBgIng.setImageResource(R.drawable.sample_ic_vp_banner_bg);
        ivImg.setImageResource(R.drawable.sample_ic_vp_banner);
        ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), item.text, Toast.LENGTH_SHORT).show();
            }
        });

        return layout;


//        ImageView iv = new ImageView(context);
//        iv.setLayoutParams(new ViewGroup.LayoutParams(container.getWidth(), container.getHeight()));
//        iv.setScaleType(ImageView.ScaleType.FIT_XY);
//        iv.setImageResource(item.res);
//        iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), item.text, Toast.LENGTH_SHORT).show();
//            }
//        });
//        return iv;
    }

    private void initData() {
        for (int i = 0; i < 4; i++) {
            mData.add(new PageItem(R.mipmap.ic_launcher, "xxxx" + i));
        }
    }


}
