package com.canzhang.sample.manager.activity_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * 小测试
 */
public class ActivityTestDemoManager extends BaseManager {

    private Activity mActivity;


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        mActivity = activity;
        List<ComponentItem> list = new ArrayList<>();
        list.add(jsonTest());
        return list;
    }

    private ComponentItem jsonTest() {
        return new ComponentItem("启动一个透明activity（生命周期）","透明主题的activity，前一个activity只会走onPause，不会走onStop" +
                "\n2、关掉透明activity，前一个activity也只会走onResume,而不是onRestart-onStart-onResume", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity,TranslateActivity.class));
            }
        });
    }







}
