package com.canzhang.sample.manager;

import android.app.Activity;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 非sdk开放api 测试
 *
 * 相关原理介绍：https://blog.csdn.net/woai110120130/article/details/83244075
 */
public class UseNonSdkApiDemoManager extends BaseManager {


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        List<ComponentItem> list = new ArrayList<>();
        list.add(getScreenMode(activity));
        list.add(addBrightness(activity));
        return list;
    }

    private ComponentItem getScreenMode(final Activity activity) {

        return new ComponentItem("调用深灰api", new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    private ComponentItem addBrightness(final Activity activity) {

        return new ComponentItem("调用黑名单api", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
