package com.canzhang.sample.base.bean;

import android.view.View;

import com.canzhang.sample.base.IManager;


public class ComponentItem {
    public String name;
    public String desc;
    public View.OnClickListener listener;
    public int priority;
    public IManager manager;


    /**
     * 通用模板
     *
     * @param name
     * @param manager 通用模板管理（提供一个list数据）
     */
    public ComponentItem(String name, IManager manager) {
        this.name = name;
        this.manager = manager;
    }

    public ComponentItem() {

    }

    public ComponentItem(String name,String desc, View.OnClickListener listener) {
        this(name, listener);
        this.desc = desc;
    }

    /**
     * 通用点击item
     *
     * @param name
     * @param listener
     */
    public ComponentItem(String name, View.OnClickListener listener) {
        this.name = name;
        this.listener = listener;
    }


    public ComponentItem setPriority(int priority) {
        this.priority = priority;
        return this;
    }
}