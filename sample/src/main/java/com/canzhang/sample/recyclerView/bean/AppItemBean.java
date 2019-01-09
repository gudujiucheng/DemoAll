package com.canzhang.sample.recyclerView.bean;

import android.support.annotation.IntDef;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class AppItemBean implements MultiItemEntity {
    private int type;
    private String imgUrl;
    private String text;
    private List<PageItem> pageItems;
    public @ItemType
    static final int APP_ITEM_01 = 1;
    public @ItemType
    static final int APP_ITEM_02 = 2;

    @IntDef({APP_ITEM_01, APP_ITEM_02})
    @interface ItemType {
    }

    //类型2
    public AppItemBean(String imgUrl, String text) {
        this.type = APP_ITEM_02;
        this.imgUrl = imgUrl;
        this.text = text;
    }

    //类型1
    public AppItemBean(List<PageItem> pageItems) {
        this.type = APP_ITEM_01;
        this.pageItems = pageItems;
    }

    @Override
    public int getItemType() {
        return type;
    }


    public String getImgUrl() {
        return imgUrl;
    }

    public String getText() {
        return text;
    }

    public List<PageItem> getPageItems() {
        return pageItems;
    }


}
