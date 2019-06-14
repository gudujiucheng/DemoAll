package com.canzhang.sample.manager.rxjava;

import android.app.Activity;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.component.debugdialog.DebugDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * rxjava   ﻿1、rxjava的学习记录
 */
public class RxJavaBaseDemoManager extends BaseManager {


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        List<ComponentItem> list = new ArrayList<>();
        list.add(init(activity));
        return list;
    }


    private ComponentItem init(final Activity activity) {

        return new ComponentItem("基础订阅", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }




}
