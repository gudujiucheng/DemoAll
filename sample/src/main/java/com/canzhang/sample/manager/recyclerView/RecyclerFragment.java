package com.canzhang.sample.manager.recyclerView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.recyclerView.adapter.AppAdapter;
import com.canzhang.sample.manager.recyclerView.bean.AppItemBean;
import com.canzhang.sample.manager.recyclerView.bean.PageItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.base.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;


public class RecyclerFragment extends BaseFragment {
    private RecyclerView mRvApp;
    private List<AppItemBean> mData = new ArrayList<>();
    private AppAdapter mAdapter;

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
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);//屏幕分成两份

        //注意这里和原生设置有些差异
        mAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                switch (mAdapter.getItemViewType(position)) {
                    case AppItemBean.APP_ITEM_01://当前item占用几份
                        return 2;
                    default:
                    case AppItemBean.APP_ITEM_02:
                        return 1;
                }
            }
        });

        mRvApp.setLayoutManager(manager);
        mRvApp.setAdapter(mAdapter);
    }

    private void initData() {
        List<PageItem> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new PageItem(R.mipmap.ic_launcher, "xxxx"));
        }
        mData.add(new AppItemBean(list));

        for (int i = 0; i < 10; i++) {
            mData.add(new AppItemBean("", "哈哈哈哈哈" + i));
        }
    }

    private void initView(View view) {
        mRvApp = view.findViewById(R.id.rv_app);
        initData();
        setAdapter();
    }


}
