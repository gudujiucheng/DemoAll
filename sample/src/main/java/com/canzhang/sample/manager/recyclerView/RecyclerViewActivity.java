package com.canzhang.sample.manager.recyclerView;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.recyclerView.adapter.AppAdapter;
import com.canzhang.sample.manager.recyclerView.bean.AppItemBean;
import com.canzhang.sample.manager.recyclerView.bean.PageItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.base.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends BaseActivity {

    private RecyclerView mRvApp;
    private List<AppItemBean> mData = new ArrayList<>();
    private AppAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_recyclerview);
        mRvApp = findViewById(R.id.rv_app);
        initData();
        setAdapter();
    }

    private void setAdapter() {
        mAdapter = new AppAdapter(mData);
        GridLayoutManager manager = new GridLayoutManager(this, 2);//屏幕分成两份

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
        for (int i = 0; i <10 ; i++) {
            list.add(new PageItem(R.mipmap.ic_launcher,"xxxx"));
        }
        mData.add(new AppItemBean(list));

        for (int i = 0; i < 10; i++) {
            mData.add(new AppItemBean("", "哈哈哈哈哈" + i));
        }
    }
}
