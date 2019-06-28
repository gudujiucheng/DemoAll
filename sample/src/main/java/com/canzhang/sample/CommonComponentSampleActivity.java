package com.canzhang.sample;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.canzhang.sample.base.adapter.ComponentAdapter;
import com.canzhang.sample.base.IManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.base.base.BaseActivity;


import java.util.ArrayList;
import java.util.List;


/**
 * 各组应用范例
 */


public class CommonComponentSampleActivity extends BaseActivity implements INotifyListener {
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
        log("onCreate");

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


    @Override
    protected void onStart() {
        super.onStart();
        log("onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        log("onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        log("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        log("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        log("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        log("onDestroy");
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        showToast(newConfig.orientation == 1 ? "竖屏=1" : "横屏=2");
    }

    @Override
    public void onNotify() {
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }
}
