package com.canzhang.sample.manager.view.viewpager.fql.transformer;

/**
 * @Description: 错位动画
 * @Author: canzhang
 * @CreateDate: 2019/4/26 15:03
 * <p>
 * https://www.jianshu.com/p/11a819bc5973
 */

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.canzhang.sample.R;

public class TranslationXPageTransformer implements ViewPager.PageTransformer {
    private static final float LEFT_PERCENT = 0.95f;
    private static final float RIGHT_PERCENT = 0.50f;

    @Override
    public void transformPage(@NonNull View page, float position) {

        View ivImg = page.findViewById(R.id.iv_img);
        if (ivImg == null) {
            return;
        }
        int pageWidth = ivImg.getWidth();
        if (pageWidth <= 0) {
            return;
        }


        float scalePercent = Math.max(position < 0 ? LEFT_PERCENT : RIGHT_PERCENT, 1 - Math.abs(position));

        float translationX = pageWidth * (1 - scalePercent) / 2;
        if (position < 0) {
            ivImg.setTranslationX(-translationX);
        } else {
            ivImg.setTranslationX(translationX);
        }
    }

}
