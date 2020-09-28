package com.canzhang.sample.manager.ram;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.canzhang.sample.R;
import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.example.base.utils.ToastUtil;
import com.example.simple_test_annotations.MarkManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 内存相关测试(未得出明确结论)
 */
@MarkManager(value = "回收测试")
public class RamManager extends BaseManager {

    Activity activity;
    GcBeanTest c;
    WeakReference weakReference;

    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {

        this.activity = activity;
        List<ComponentItem> list = new ArrayList<>();
        list.add(create());
        list.add(gc());
        list.add(useRam());
        list.add(test());
        return list;
    }

    private ComponentItem create() {

        return new ComponentItem("创建一个对象", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GcBeanTest("我是对象1");
                GcBeanTest a = new GcBeanTest("我是对象2");
                a = null;
                c = new GcBeanTest("------------------------->我是被外部持有引用的对象3");
                weakReference = new WeakReference<GcBeanTest>(c);
            }
        });
    }

    private ComponentItem gc() {

        return new ComponentItem("触发gc", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.gc();
            }
        });
    }

    private List<Drawable> list = new ArrayList<android.graphics.drawable.Drawable>();

    private ComponentItem useRam() {

        return new ComponentItem("触发耗内存操作", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 10000; i++) {
                    list.add(activity.getResources().getDrawable(R.mipmap.ic_launcher));
                }
            }
        });
    }

    private ComponentItem test() {

        return new ComponentItem("测试对象是否为空", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toastShort(weakReference==null||weakReference.get() == null ? "回收了" : "没有回收");//有这种强引用 很难触发回收操作
            }
        });
    }


}
