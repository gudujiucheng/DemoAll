package com.canzhang.sample.manager.weex.view;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import android.widget.TextView;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

public class RichText extends WXComponent<TextView> {


    public RichText(WXSDKInstance instance, WXVContainer parent, String instanceId, boolean isLazy, BasicComponentData basicComponentData) {
        super(instance, parent, instanceId, isLazy, basicComponentData);
    }

    public RichText(WXSDKInstance instance, WXVContainer parent, boolean isLazy, BasicComponentData basicComponentData) {
        super(instance, parent, isLazy, basicComponentData);
    }

    public RichText(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
    }

    public RichText(WXSDKInstance instance, WXVContainer parent, int type, BasicComponentData basicComponentData) {
        super(instance, parent, type, basicComponentData);
    }

    @Override
    protected TextView initComponentHostView(@NonNull Context context) {
        TextView textView = new TextView(context);
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    @WXComponentProp(name = "tel")
    public void setTel(String telNumber) {
        getHostView().setText("tel: " + telNumber);
    }
}