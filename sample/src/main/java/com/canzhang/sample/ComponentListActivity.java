package com.canzhang.sample;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.canzhang.sample.base.IManager;
import com.canzhang.sample.base.adapter.ComponentAdapter;
import com.canzhang.sample.base.bean.ComponentItem;
import com.canzhang.sample.manager.OtherTestDemoManager;
import com.canzhang.sample.manager.aidl.AidlClientFragment;
import com.canzhang.sample.manager.antifraud.AntiFraudManager;
import com.canzhang.sample.manager.behavior.BehaviorTestActivity;
import com.canzhang.sample.manager.eventdispatch.EventDispatchFragment;
import com.canzhang.sample.manager.flutter_test.FlutterTestActivity;
import com.canzhang.sample.manager.flutter_test.FlutterTestFragment;
import com.canzhang.sample.manager.fragment_test.ContainerActivity;
import com.canzhang.sample.manager.gif.GifFragment;
import com.canzhang.sample.manager.img.ImgTestFragment;
import com.canzhang.sample.manager.lifetest.LifeTestFragment;
import com.canzhang.sample.manager.messenger.MessengerClientFragment;
import com.canzhang.sample.manager.permission.PermissionFragment;
import com.canzhang.sample.manager.qrcode.QRCodeActivity;
import com.canzhang.sample.manager.view.CommonViewShowFragment;
import com.canzhang.sample.manager.view.editText.TestEditTextFragment;
import com.canzhang.sample.manager.view.font.FontTestFragment;
import com.canzhang.sample.manager.view.recyclerView.RecyclerFragment;
import com.canzhang.sample.manager.view.recyclerView.RecyclerNativeTestFragment;
import com.canzhang.sample.manager.view.recyclerView.RecyclerViewHeaderFooterFragment;
import com.canzhang.sample.manager.view.shadow.ShadowFragment;
import com.canzhang.sample.manager.view.viewpager.ViewPagerFragment;
import com.canzhang.sample.manager.view.viewpager.fql.FqlViewPagerFragment;
import com.canzhang.sample.manager.view.webview.WebViewFragment;
import com.canzhang.sample.manager.weex.WeexActivity;
import com.canzhang.sample.manager.zhujie.ZhuJieManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.base.base.BaseActivity;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * 各组件应用范例
 */

//@Route(path = "/sample/sampleList")
public class ComponentListActivity extends BaseActivity implements INotifyListener {
    private RecyclerView mRecyclerView;
    private List<ComponentItem> mData = new ArrayList<>();

    private int  mPriority = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getApplication().setTheme(R.style.Sample_AppThemeSplash);
//        setTheme(R.style.Sample_AppThemeSplash);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_component_list);
        mRecyclerView = findViewById(R.id.rv_test);
        preLoad();
        initData();
        initRecyclerView();
        setTitle("组件应用范例");
        log("onCreate");
    }

    /**
     * 需要提前做的一些事情
     */
    private void preLoad() {
//        GifUtils.preLoadGif(this, GifFragment.gifUrl);
    }

    /**
     * 在这里添加要调试的组件数据(经常用的或者最新调整的，向前提)
     */
    private void initData() {
        //
        Map<String, Object> allManagerMap = ZhuJieManager.getAllManager();
        if (allManagerMap == null || allManagerMap.size() == 0) {
            showToast("注解获取的数据异常");
            mData.add(new ComponentItem("其他测试", new OtherTestDemoManager()));
        } else {
            for (String key : allManagerMap.keySet()) {
                Object manager = allManagerMap.get(key);
                if (manager instanceof IManager) {
                    IManager currentManger = (IManager) manager;
                    ComponentItem componentItem = new ComponentItem(key, currentManger);
                    if (currentManger.getPriority() != 0) {//manager 优先级高于 componentItem 优先级配置
                        componentItem.setPriority(currentManger.getPriority());
                    }
                    mData.add(componentItem);
                } else {
                    showToast("注解获取的数据异常=====>>>>" + key);
                }
            }
        }


        mData.add(new ComponentItem("图片信息、压缩、旋转、颜色修改等测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(ImgTestFragment.newInstance());
            }
        }).setPriority(10));
        mData.add(new ComponentItem("flutter 测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(FlutterTestFragment.newInstance());
            }
        }));

        mData.add(new ComponentItem("flutter 测试2", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(FlutterTestActivity.class);
            }
        }));


        mData.add(new ComponentItem("gif 测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(GifFragment.newInstance());
            }
        }));
        mData.add(new ComponentItem("用户行为监听", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start(BehaviorTestActivity.class);
            }
        }));
        mData.add(new ComponentItem("Messenger 跨进程应用", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(MessengerClientFragment.newInstance());
            }
        }));
        mData.add(new ComponentItem("ipc之aidl", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(AidlClientFragment.newInstance());
            }
        }));

        mData.add(new ComponentItem("字体测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(FontTestFragment.newInstance());
            }
        }));

        mData.add(new ComponentItem("虚线", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(CommonViewShowFragment.newInstance(CommonViewShowFragment.DASH_LINE));
            }
        }));

        mData.add(new ComponentItem("投票", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(CommonViewShowFragment.newInstance(CommonViewShowFragment.VOTE_VIEW));
            }
        }).setPriority(7));
        mData.add(new ComponentItem("WebView 相关测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new WebViewFragment());
            }
        }));
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

        mData.add(new ComponentItem("事件分发测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new EventDispatchFragment());
            }
        }));
        mData.add(new ComponentItem("RecyclerView 多type类型(第三方框架)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new RecyclerFragment());
            }
        }));
        mData.add(new ComponentItem("RecyclerView 多type类型(非框架)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new RecyclerNativeTestFragment());
            }
        }).setPriority(20));

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
        Collections.sort(mData, new Comparator<ComponentItem>() {
            @Override
            public int compare(ComponentItem o1, ComponentItem o2) {
                if (o1.priority != 0 || o2.priority != 0) {
                    return o1.priority < o2.priority ? 1 : -1;
                }
                return Collator.getInstance(Locale.CHINA).compare(o1.name, o2.name);
            }
        });
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        log("onNewIntent");
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
    public void onNotify() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
