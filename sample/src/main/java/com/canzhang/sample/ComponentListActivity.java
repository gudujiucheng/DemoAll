package com.canzhang.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.canzhang.sample.base.adapter.ComponentAdapter;
import com.canzhang.sample.base.bean.ComponentItem;
import com.canzhang.sample.manager.AppStatusManager;
import com.canzhang.sample.manager.BrightnessDemoManager;
import com.canzhang.sample.manager.DebugDemoManager;
import com.canzhang.sample.manager.JniDemoManager;
import com.canzhang.sample.manager.OtherTestDemoManager;
import com.canzhang.sample.manager.qrcode.QRCodeActivity;
import com.canzhang.sample.manager.recyclerView.RecyclerViewActivity;
import com.canzhang.sample.manager.rxjava.RxJavaTestDemoManager;
import com.canzhang.sample.manager.viewpager.ViewPagerFragment;
import com.canzhang.sample.manager.viewpager.fql.FqlViewPagerFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.base.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 各组件应用范例
 */

//@Route(path = "/sample/sampleList")
public class ComponentListActivity extends BaseActivity {
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
        mData.add(new ComponentItem("平常测试demo", new OtherTestDemoManager()));
        mData.add(new ComponentItem("app前后台检测", new AppStatusManager()));
        mData.add(new ComponentItem("rxJava实际应用", new RxJavaTestDemoManager()));
        mData.add(new ComponentItem("jni", new JniDemoManager()));
        mData.add(new ComponentItem("调试弹窗", new DebugDemoManager()));
        mData.add(new ComponentItem("调节亮度测试", new BrightnessDemoManager()));
        mData.add(new ComponentItem("调节亮度测试", new BrightnessDemoManager()));
        mData.add(new ComponentItem("多type类型(非原生)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(RecyclerViewActivity.class);
            }
        }));

        mData.add(new ComponentItem("viewPager", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new ViewPagerFragment());
            }
        }));

        mData.add(new ComponentItem("fql viewPager", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new FqlViewPagerFragment());
            }
        }));
        mData.add(new ComponentItem("二维码生成测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(QRCodeActivity.class);
            }
        }));
    }


    BaseQuickAdapter<ComponentItem, BaseViewHolder> adapter;

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter = new ComponentAdapter(R.layout.sample_list_item, mData));
    }


    private void start(Class clazz) {
        startActivity(new Intent(ComponentListActivity.this, clazz));
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
