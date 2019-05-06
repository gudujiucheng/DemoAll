package com.canzhang.sample.manager.view.viewpager.fql.banner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.canzhang.sample.R;
import com.example.base.utils.ScreenUtil;


/**
 * @Author:Owenli
 * @Create:2019/3/5
 */
public class NewBigBannerPoint implements BannerPointInterface {

    private LinearLayout mLlPointContain;

    private int mPointRes = R.drawable.sample_consume_application_header_point;


    private LinearLayout.LayoutParams mNormalParams;
    private LinearLayout.LayoutParams mSelectedParams;

    public NewBigBannerPoint(Context context, LinearLayout pointContain) {
        this(context, pointContain, -1);
    }

    public NewBigBannerPoint(Context context, LinearLayout pointContain, int pointRes) {
        this(context, pointContain, 6, 4, 8, 5, 3, pointRes);
    }

    public NewBigBannerPoint(Context context, LinearLayout pointContain, int width, int height, int selectWidth, int selectHeight, int space, int pointRes) {
        if (pointRes != -1) {
            this.mPointRes = pointRes;
        }
        width = (int) ScreenUtil.dip2px(context, width > 0 ? width : 6);
        height = (int) ScreenUtil.dip2px(context, height > 0 ? height : 4);
        space = (int) ScreenUtil.dip2px(context, space > 0 ? space : 3);
        selectWidth = (int) ScreenUtil.dip2px(context, selectWidth > 0 ? selectWidth : 8);
        selectHeight = (int) ScreenUtil.dip2px(context, selectHeight > 0 ? selectHeight : 5);
        mLlPointContain = pointContain;
        mNormalParams = new LinearLayout.LayoutParams(width, height);
        mNormalParams.rightMargin = space;
        mSelectedParams = new LinearLayout.LayoutParams(selectWidth, selectHeight);
        mSelectedParams.rightMargin = space;
    }

    @Override
    public void initPoint(int size) {
        mLlPointContain.removeAllViews();
        for (int i = 0; i < size; i++) {
            View point = new View(mLlPointContain.getContext());
            point.setBackgroundResource(mPointRes);
            ViewGroup.LayoutParams params;
            if (i == 0) {
                point.setSelected(true);
                params = mSelectedParams;
            } else {
                point.setSelected(false);
                params = mNormalParams;
            }
            mLlPointContain.addView(point, params);
        }
    }

    @Override
    public void onSelected(int position) {
        for (int i = 0; i < mLlPointContain.getChildCount(); i++) {
            View view = mLlPointContain.getChildAt(i);
            ViewGroup.LayoutParams params;
            if (position == i) {
                params = mSelectedParams;
                view.setSelected(true);
                view.setLayoutParams(params);
            } else if (view.isSelected()) {
                params = mNormalParams;
                view.setSelected(false);
                view.setLayoutParams(params);
            }
        }
    }
}
