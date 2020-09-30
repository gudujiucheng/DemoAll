package com.canzhang.sample.manager.weex;

import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;

/**
 * Created by zc on 2019年1月10日17:23:47
 */
public class ImageAdapter implements IWXImgLoaderAdapter {


    @Override
    public void setImage(final String url, final ImageView view, final WXImageQuality quality, final WXImageStrategy strategy) {
        Log.e("Test","下载图片："+url);


        WXSDKManager.getInstance().postOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (view == null || view.getLayoutParams() == null) {
                    return;
                }
                if (TextUtils.isEmpty(url)) {
                    view.setImageBitmap(null);
                    return;
                }
                if (view.getLayoutParams().width <= 0 || view.getLayoutParams().height <= 0) {
                    return;
                }

                Glide.with(view.getContext())
                        .load(url)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(new DrawableImageViewTarget(view) {
                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                                if (strategy.getImageListener() != null) {
                                    strategy.getImageListener().onImageFinish(url, view, false, null);
                                }
                            }

                            @Override
                            public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                super.onResourceReady(resource, transition);
                                if (strategy.getImageListener() != null) {
                                    strategy.getImageListener().onImageFinish(url, view, true, null);
                                }
                            }
                        });


            }
        }, 0);
    }

}