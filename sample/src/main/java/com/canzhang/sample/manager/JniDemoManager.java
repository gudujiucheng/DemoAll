package com.canzhang.sample.manager;

import android.app.Activity;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.canzhang.sample.manager.jni.JNITest;
import com.component.debugdialog.DebugDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * jni 用法展示
 *
 */
public class JniDemoManager extends BaseManager {


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        List<ComponentItem> list = new ArrayList<>();
        list.add(jniTest(activity));
        return list;
    }


    private ComponentItem jniTest(final Activity activity) {

        return new ComponentItem("调用jni方法", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             showToast(JNITest.get());
            }
        });
    }






}
