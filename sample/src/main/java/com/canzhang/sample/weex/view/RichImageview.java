package com.canzhang.sample.weex.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

public class RichImageview extends WXComponent<ImageView> {


    public RichImageview(WXSDKInstance instance, WXVContainer parent, String instanceId, boolean isLazy, BasicComponentData basicComponentData) {
        super(instance, parent, instanceId, isLazy, basicComponentData);
    }

    public RichImageview(WXSDKInstance instance, WXVContainer parent, boolean isLazy, BasicComponentData basicComponentData) {
        super(instance, parent, isLazy, basicComponentData);
    }

    public RichImageview(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
    }

    public RichImageview(WXSDKInstance instance, WXVContainer parent, int type, BasicComponentData basicComponentData) {
        super(instance, parent, type, basicComponentData);
    }

    @Override
    protected ImageView initComponentHostView(@NonNull Context context) {
        ImageView imageView = new ImageView(context);
        return imageView;
    }

    @WXComponentProp(name = "url")
    public void setImg(String url) {
        Glide.with(getContext()).load(url).into( getHostView());
    }
}