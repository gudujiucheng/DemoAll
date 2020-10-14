package com.canzhang.sample.manager.sockettest;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.example.simple_test_annotations.MarkManager;

import java.util.ArrayList;
import java.util.List;

@MarkManager(value = "socket test")
public class SocketTestManager extends BaseManager {


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        super.getSampleItem(activity);
        List<ComponentItem> list = new ArrayList<>();
        list.add(test());
        return list;
    }

    @Override
    public int getPriority() {
        return 6;
    }

    private ComponentItem test() {

        return new ComponentItem("开启测试页面", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity,SocketTestActivity.class));

            }
        });
    }


}
