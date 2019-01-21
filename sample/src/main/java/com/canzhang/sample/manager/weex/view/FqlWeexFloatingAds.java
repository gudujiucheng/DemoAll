package com.canzhang.sample.manager.weex.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

/**
 * Weex 浮动广告View
 * Created by owenli on 2017/11/6.
 */
@Component(lazyload = false)
public class FqlWeexFloatingAds extends WXComponent<FloatingAdsLayout> {

    FloatingAdsLayout mFloatLayout;

    public FqlWeexFloatingAds(WXSDKInstance instance, WXVContainer parent, String instanceId, boolean isLazy, BasicComponentData basicComponentData) {
        super(instance, parent, instanceId, isLazy, basicComponentData);
    }

    public FqlWeexFloatingAds(WXSDKInstance instance, WXVContainer parent, boolean isLazy, BasicComponentData basicComponentData) {
        super(instance, parent, isLazy, basicComponentData);
    }

    public FqlWeexFloatingAds(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
    }

    public FqlWeexFloatingAds(WXSDKInstance instance, WXVContainer parent, int type, BasicComponentData basicComponentData) {
        super(instance, parent, type, basicComponentData);
    }


    @Override
    protected FloatingAdsLayout initComponentHostView(@NonNull Context context) {
        mFloatLayout = new FloatingAdsLayout(context);
        mFloatLayout.setClickListener(new FloatingAdsLayout.ClickTagListener() {
            @Override
            public void onClick() {
                fireEvent("onClickImage");
            }
        });
        return mFloatLayout;
    }

    @WXComponentProp(name = "x")
    public void setX(String x) {
        if (mFloatLayout != null) {
            mFloatLayout.setX(x);
        }
    }

    @WXComponentProp(name = "y")
    public void setY(String y) {
        if (mFloatLayout != null) {
            mFloatLayout.setY(y);
        }
    }

    @WXComponentProp(name = "imageWidth")
    public void setImageWidth(int imageWidth) {
        if (mFloatLayout != null) {
            mFloatLayout.setImageWidth(imageWidth);
        }
    }

    @WXComponentProp(name = "imageHeight")
    public void setImageHeight(int imageHeigth) {
        if (mFloatLayout != null) {
            mFloatLayout.setImageHeight(imageHeigth);
        }
    }

    @WXComponentProp(name = "absorb")
    public void setAbsorb(int absorb) {
        if (mFloatLayout != null) {
            mFloatLayout.isAbsorb(absorb != 0);
        }
    }

    @WXComponentProp(name = "visible")
    public void setVisible(int visible) {
        if (mFloatLayout != null) {
            mFloatLayout.setVisibility(visible != 0 ? View.VISIBLE : View.GONE);
        }

    }

    @WXComponentProp(name = "imageUrl")
    public void setImageUrl(String imageUrl) {
        if (mFloatLayout != null) {
            mFloatLayout.setImageUrl(imageUrl);
        }
    }
}
