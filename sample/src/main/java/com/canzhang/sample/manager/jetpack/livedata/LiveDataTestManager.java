package com.canzhang.sample.manager.jetpack.livedata;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.example.simple_test_annotations.MarkManager;

import java.util.ArrayList;
import java.util.List;

@MarkManager(value = "liveData test")
public class LiveDataTestManager extends BaseManager {


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        super.getSampleItem(activity);
        List<ComponentItem> list = new ArrayList<>();
        list.add(testGoogle());
        return list;
    }

    private ComponentItem testGoogle() {
        return new ComponentItem("启动activity(测试 liveData)","ViewModel 类旨在以注重生命周期的方式存储和管理界面相关的数据。ViewModel 类让数据可在发生屏幕旋转等配置更改后继续留存。\n" +
                "ViewModel 的数据是在onDestory清理的（这里会有判断，如果是配置信息销毁重建则不会清空）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, LiveDataTestActivity.class));
            }
        });
    }

    @Override
    public int getPriority() {
        return 4;
    }




}
