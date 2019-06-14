package com.canzhang.sample.manager;

import android.app.Activity;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.component.debugdialog.DebugDialog;


import java.util.ArrayList;
import java.util.List;

/**
 * DebugDialog 用法展示
 */
public class DebugDemoManager extends BaseManager {


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        List<ComponentItem> list = new ArrayList<>();
        list.add(init(activity));
        list.add(getAlertPermission(activity));
        list.add(showDebugDialog());
        return list;
    }

    /**
     * 初始化
     *
     * @param activity
     * @return
     */
    private ComponentItem init(final Activity activity) {

        return new ComponentItem("初始化（使用之前需要初始化）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹窗在使用之前需要初始化
                DebugDialog.getInstance().init(activity);
                //需要设置调试模式为true 才会显示弹窗
                DebugDialog.setIsDebug(true);
            }
        });
    }


    /**
     * 获取弹窗权限
     *
     * @param activity
     * @return
     */
    private ComponentItem getAlertPermission(final Activity activity) {

        return new ComponentItem("获取浮窗权限", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //使用之前需要授予弹窗权限
                DebugDialog.getAlertPermission(activity);
            }
        });
    }

    /**
     * 展示弹窗
     *
     * @return
     */
    private ComponentItem showDebugDialog() {
        return new ComponentItem("展示弹窗", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //支持一次性发出多个弹出请求
                DebugDialog.getInstance().show("title", "我是第1个");
                DebugDialog.getInstance().show("title", "我是第2个");
                DebugDialog.getInstance().show("title", "我是第3个");
            }
        });

    }


}
