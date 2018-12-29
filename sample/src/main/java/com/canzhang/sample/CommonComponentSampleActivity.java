package com.canzhang.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.canzhang.sample.adapter.ComponentAdapter;
import com.canzhang.sample.base.IManager;
import com.canzhang.sample.bean.ComponentItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


import java.util.ArrayList;
import java.util.List;


/**
 * 各组应用范例
 */


public class CommonComponentSampleActivity extends Activity {
    private RecyclerView mRecyclerView;
    private List<ComponentItem> mData = new ArrayList<>();
    private static IManager mIManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_component_list);
        mRecyclerView = findViewById(R.id.rv_test);
        initRecyclerView();
        if (mIManager != null) {
            setTitle(mIManager.getClass().getSimpleName());
            mData.clear();
            List<ComponentItem> sampleItem = mIManager.getSampleItem(this);
            if (sampleItem != null) {
                mData.addAll(sampleItem);
            }
        }


    }

    /**
     * 注意需要先设置数据源
     *
     * @param manager
     */
    public static void setManager(IManager manager) {
        mIManager = manager;
    }


    BaseQuickAdapter<ComponentItem, BaseViewHolder> adapter;

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter = new ComponentAdapter(R.layout.sample_list_item, mData));

    }


    private void start(Class clazz) {
        startActivity(new Intent(CommonComponentSampleActivity.this, clazz));
    }
}
