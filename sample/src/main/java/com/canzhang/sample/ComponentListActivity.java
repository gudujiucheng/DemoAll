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
import com.canzhang.sample.manager.UseNonSdkApiDemoManager;
import com.canzhang.sample.manager.eventdispatch.EventDispatchFragment;
import com.canzhang.sample.manager.fragment_test.ContainerActivity;
import com.canzhang.sample.manager.lifetest.LifeTestFragment;
import com.canzhang.sample.manager.permission.PermissionFragment;
import com.canzhang.sample.manager.qrcode.QRCodeActivity;
import com.canzhang.sample.manager.view.editText.TestEditTextFragment;
import com.canzhang.sample.manager.view.recyclerView.RecyclerFragment;
import com.canzhang.sample.manager.view.recyclerView.RecyclerViewHeaderFooterFragment;
import com.canzhang.sample.manager.rxjava.RxJavaTestDemoManager;
import com.canzhang.sample.manager.view.shadow.ShadowFragment;
import com.canzhang.sample.manager.thread.ThreadTestManager;
import com.canzhang.sample.manager.view.viewpager.ViewPagerFragment;
import com.canzhang.sample.manager.view.viewpager.fql.FqlViewPagerFragment;
import com.canzhang.sample.manager.weex.WeexActivity;
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
        mData.add(new ComponentItem("EditText 相关测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new TestEditTextFragment());
            }
        }));
        mData.add(new ComponentItem("fragment相关测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(ContainerActivity.class);
            }
        }));
        mData.add(new ComponentItem("多线程相关测试", new ThreadTestManager()));
        mData.add(new ComponentItem("USE NON SDK API TEST", new UseNonSdkApiDemoManager()));
        mData.add(new ComponentItem("权限测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new PermissionFragment());
            }
        }));
        mData.add(new ComponentItem("未绑定生命周期测试（内存泄露）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new LifeTestFragment());
            }
        }));
        mData.add(new ComponentItem("阴影测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new ShadowFragment());
            }
        }));
        mData.add(new ComponentItem("线程测试", new ThreadTestManager()));
        mData.add(new ComponentItem("事件分发测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new EventDispatchFragment());
            }
        }));
        mData.add(new ComponentItem("平常测试demo", new OtherTestDemoManager()));
        mData.add(new ComponentItem("app前后台检测", new AppStatusManager()));
        mData.add(new ComponentItem("rxJava实际应用", new RxJavaTestDemoManager()));
        mData.add(new ComponentItem("jni", new JniDemoManager()));
        mData.add(new ComponentItem("调试弹窗", new DebugDemoManager()));
        mData.add(new ComponentItem("调节亮度测试", new BrightnessDemoManager()));
        mData.add(new ComponentItem("RecyclerView 多type类型(第三方框架)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new RecyclerFragment());
            }
        }));


        mData.add(new ComponentItem("RecyclerView fql 刷新头部", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new RecyclerViewHeaderFooterFragment());
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
        mData.add(new ComponentItem("weex测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(WeexActivity.class);
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
