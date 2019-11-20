package com.canzhang.sample.manager;

import android.app.Activity;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.canzhang.sample.manager.jni.JNICmake;
import com.canzhang.sample.manager.jni.JNITest;
import com.canzhang.sample.manager.jni.JNICrash;

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
        list.add(jniTest02(activity));
        return list;
    }


    private ComponentItem jniTest(final Activity activity) {

        return new ComponentItem("传统方式调用jni方法","对应的库，没有提交，如果想正常运行，请先cd 进入jni文件夹，然后ndk-build生成so库之后，在运行", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             showToast(JNITest.get());
            }
        });
    }

    private ComponentItem jniTest02(final Activity activity) {

        return new ComponentItem("cmake方式调用jni方法",  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(JNICmake.stringFromJNI());
            }
        });
    }






}
