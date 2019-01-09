package com.example.base.view.viewpager;

import android.content.Context;
import android.view.View;

/**
 * 提供数据和绑定布局
 * @param <T>
 */
public interface ViewPagerHolder<T> {

    /**
     * 创建view
     * @param context
     * @return
     */
    View createView(Context context);

    /**
     * 绑定数据
     * @param context
     * @param position
     * @param data
     */
    void onBind(Context context,int position,T data);

}
