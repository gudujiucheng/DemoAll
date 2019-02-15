package com.lxj.xrefreshlayout.loadinglayout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


public interface ILoadingLayout {
    /**
     * make your loading view!
     * @return
     */
    View createLoadingHeader(Context context, ViewGroup parent);


    /**
     * init your views or reset them.
     */
    void initAndResetHeader();
;

    /**
     * called when you are pulling the XRefreshLayout, you
     * can make your pulling animation!
     */
    void onPullHeader(float percent);


    /**
     * called when release a view form pull state!
     */
    void onHeaderRefreshing();

    /**
     * called when complete!
     */
    void onHeaderCompleteRefresh();

}
