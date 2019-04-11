package com.canzhang.sample.manager.view.recyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.view.recyclerView.adapter.AppAdapter;
import com.canzhang.sample.manager.view.recyclerView.bean.AppItemBean;
import com.canzhang.sample.manager.view.recyclerView.fqlrefresh.xrefreshlayout.FqlRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.base.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewHeaderFooterFragment extends BaseFragment {
    private RecyclerView mRvApp;
    private List<AppItemBean> mData = new ArrayList<>();
    private AppAdapter mAdapter;
    int mCurrentCounter;
    int TOTAL_COUNTER = 50;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sample_fragment_recyclerview, container, false);
        initView(view);
        initData();
        return view;
    }

    private void setAdapter() {
        mAdapter = new AppAdapter(mData);
        mAdapter.bindToRecyclerView(mRvApp);
        mAdapter.setEnableLoadMore(true);
        mAdapter.disableLoadMoreIfNotFullPage();
        mRvApp.setLayoutManager(new LinearLayoutManager(mContext));


        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override public void onLoadMoreRequested() {
                mRvApp.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mCurrentCounter >= TOTAL_COUNTER) {
                            //数据全部加载完毕
                            mAdapter.loadMoreEnd();
                        } else {
                            if (true) {
                                //成功获取更多数据
                                mAdapter.addData(loadMoreData());
                                mCurrentCounter = mAdapter.getData().size();
                                mAdapter.loadMoreComplete();
                            } else {


                                mAdapter.loadMoreFail();

                            }
                        }
                    }

                }, 2000);
            }
        }, mRvApp);
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            mData.add(new AppItemBean("", "哈哈哈哈哈" + i));
        }
    }

    private  List<AppItemBean> loadMoreData() {
        List<AppItemBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new AppItemBean("", "哈哈哈哈哈" + i));
        }
        return list;
    }

    private void initView(View view) {
        mRvApp = view.findViewById(R.id.rv_app);
        final FqlRefreshLayout refreshLayout = view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new FqlRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showToast("刷新");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.completeRefresh();
                    }
                },2000);
            }
        });
        initData();
        setAdapter();
    }



}
