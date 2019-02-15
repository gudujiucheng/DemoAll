package com.lxj.xrefreshlayout.loadinglayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.demoall.R;
import com.lxj.xrefreshlayout.drawable.DefaultLoadingCircleViewDrawable;
import com.lxj.xrefreshlayout.drawable.LoadingCircleViewDrawable;



public class FqlDefaultLoadingLayout implements ILoadingLayout {

    private ImageView ivHeaderRefresh;
    private TextView tvFooterState;


    private DefaultLoadingCircleViewDrawable pullToRefreshDrawable;
    private LoadingCircleViewDrawable refreshingDrawable;

    @Override
    public View createLoadingHeader(Context context, ViewGroup parent) {
        View headerView = LayoutInflater.from(context).inflate(R.layout.fql_default_refresh_header, parent, false);
        ivHeaderRefresh = headerView.findViewById(R.id.iv_header_refresh);

        pullToRefreshDrawable = new DefaultLoadingCircleViewDrawable(context);
        refreshingDrawable = new LoadingCircleViewDrawable(context);
        return headerView;
    }



    @Override
    public void initAndResetHeader() {
        refreshingDrawable.stop();
    }



    @Override
    public void onPullHeader(float percent) {//等于1的时候，可释放刷新
        if (pullToRefreshDrawable != null) {
            pullToRefreshDrawable.computeRender(percent - 0.5f < 0 ? 0 : (percent - 0.5f) * 2);
            ivHeaderRefresh.setImageDrawable(pullToRefreshDrawable);
        }
    }



    @Override
    public void onHeaderRefreshing() {
        ivHeaderRefresh.setImageDrawable(refreshingDrawable);
        refreshingDrawable.start();

    }



    @Override
    public void onHeaderCompleteRefresh() {
        if (refreshingDrawable != null) {
            refreshingDrawable.stop();
        }
    }



}
