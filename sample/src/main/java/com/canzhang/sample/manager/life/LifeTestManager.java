package com.canzhang.sample.manager.life;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.example.simple_test_annotations.MarkManager;

import java.util.ArrayList;
import java.util.List;

@MarkManager(value = "生命周期test")
public class LifeTestManager extends BaseManager {


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        super.getSampleItem(activity);
        List<ComponentItem> list = new ArrayList<>();
        list.add(test());
        list.add(testGoogle());
        return list;
    }

    private ComponentItem testGoogle() {
        return new ComponentItem("启动activity(google LifeCycle)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, GoogleLifeCycleTestActivity.class));
            }
        });
    }

    @Override
    public int getPriority() {
        return 3;
    }

    private ComponentItem test() {

        return new ComponentItem("启动activity(RxLife)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, RXLifeTestActivity.class));
            }
        });
    }


}
