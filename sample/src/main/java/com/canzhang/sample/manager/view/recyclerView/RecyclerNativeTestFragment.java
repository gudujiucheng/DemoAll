package com.canzhang.sample.manager.view.recyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.view.recyclerView.adapter.AppAdapter;
import com.canzhang.sample.manager.view.recyclerView.adapter.MoreTypeAdapter;
import com.canzhang.sample.manager.view.recyclerView.bean.AppItemBean;
import com.canzhang.sample.manager.view.recyclerView.bean.PageItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.base.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 原生双列测试
 */
public class RecyclerNativeTestFragment extends BaseFragment {
    private RecyclerView mRvApp;
    private List<AppItemBean> mData = new ArrayList<>();
    private MoreTypeAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sample_fragment_recyclerview, container, false);
        initView(view);
        initData();
        return view;
    }

    private void setAdapter() {
        mAdapter = new MoreTypeAdapter(mData, new MoreTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showToast("点击："+position);
            }
        });
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);//屏幕分成两份

        //初始化2表示将屏幕均分为2份，下方return 1表示屏幕的二分之一，返回2表示占全屏
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize( int position) {
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
        mData.add(new AppItemBean(list));//类型1

        for (int i = 0; i < 10; i++) {
            mData.add(new AppItemBean("", "哈哈哈哈哈" + i));//类型2
        }
    }

    private void initView(View view) {
        mRvApp = view.findViewById(R.id.rv_app);
        initData();
        setAdapter();
    }


}
