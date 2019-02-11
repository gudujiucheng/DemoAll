package com.example.administrator.demoall.myadapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.SparseIntArray;
import android.view.ViewGroup;

import java.util.List;


public abstract class BaseTypeAdapter<T extends TypeEntity, K extends BaseViewHolder> extends BaseAdapter<T, K> {


    private SparseIntArray layouts;

    private static final int DEFAULT_VIEW_TYPE = -0xff;
    public static final int TYPE_NOT_FOUND = -404;


    public BaseTypeAdapter(List<T> data) {
        super(data);
    }

    @Override
    protected int getDefItemViewType(int position) {
        Object item = mData.get(position);
        if (item != null) {
            return ((TypeEntity) item).getItemType();
        }
        return DEFAULT_VIEW_TYPE;
    }


    @NonNull
    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据type 类型返回holder
        return super.onCreateViewHolder(parent, getLayoutId(viewType));
    }


    private int getLayoutId(int viewType) {
        return layouts.get(viewType, TYPE_NOT_FOUND);
    }

    protected void addItemType(int type, @LayoutRes int layoutResId) {
        if (layouts == null) {
            layouts = new SparseIntArray();
        }
        layouts.put(type, layoutResId);
    }


}
