package com.canzhang.sample.manager.view.viewpager.fql.transformer;

/**
 * @Description: 错位动画
 * @Author: canzhang
 * @CreateDate: 2019/4/26 15:03
 *
 * https://www.jianshu.com/p/11a819bc5973
 */

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.canzhang.sample.R;

public class TranslationXPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_PERCENT = 0.5f;

    @Override
    public void transformPage(@NonNull View page, float position) {


        View ivImg = page.findViewById(R.id.iv_img);
//        View ivBgImg = page.findViewById(R.id.iv_bg_img);
        if (ivImg == null) {
            return;
        }
        int pageWidth = ivImg.getWidth();
        if (pageWidth <= 0) {
            return;
        }

        Log.e("TAG", ivImg + " , " + position + "");

        //a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0

        //获取偏移比例，最小为 MIN_PERCENT
        float scaleFactor = Math.max(0, 1 - Math.abs(position));

        float translationX = pageWidth * (1 - scaleFactor) / 2;
        if (position < 0) {
            ivImg.setTranslationX(-translationX);
//            ivBgImg.setTranslationX(translationX);
        } else {
            ivImg.setTranslationX(translationX);
//            ivBgImg.setTranslationX(-translationX);
        }
    }
}
