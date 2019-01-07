package com.canzhang.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.canzhang.sample.adapter.ComponentAdapter;
import com.canzhang.sample.bean.ComponentItem;
import com.canzhang.sample.manager.BrightnessDemoManager;
import com.canzhang.sample.manager.DebugDemoManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


import java.util.ArrayList;
import java.util.List;


/**
 * 各组件应用范例
 */

//@Route(path = "/sample/sampleList")
public class ComponentListActivity extends Activity {
    private RecyclerView mRecyclerView;
    private List<ComponentItem> mData = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_component_list);
        mRecyclerView = findViewById(R.id.rv_test);
        initData();
        initRecyclerView();
        setTitle("组件应用范例");
    }

    /**
     * 在这里添加要调试的组件数据
     */
    private void initData() {
        mData.add(new ComponentItem("调试弹窗", new DebugDemoManager()));
        mData.add(new ComponentItem("调节亮度测试", new BrightnessDemoManager()));
    }







    BaseQuickAdapter<ComponentItem, BaseViewHolder> adapter;

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter = new ComponentAdapter(R.layout.sample_list_item, mData));
    }


    private void start(Class clazz) {
        startActivity(new Intent(ComponentListActivity.this, clazz));
    }
}
