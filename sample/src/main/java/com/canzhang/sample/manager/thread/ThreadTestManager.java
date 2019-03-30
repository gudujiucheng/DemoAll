package com.canzhang.sample.manager.thread;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.example.base.base.AppProxy;

import java.util.ArrayList;
import java.util.List;

/**
 * 线程操作相关（并发等场景测试）
 */
public class ThreadTestManager extends BaseManager {


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {

        List<ComponentItem> list = new ArrayList<>();
        list.add(createThread());
        return list;
    }


    private ComponentItem createThread() {

        return new ComponentItem("创建线程", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread01 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //会有个默认的名字
                        System.out.print("线程名字："+Thread.currentThread().getName());
                    }
                });


                Thread thread02 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.print("\n线程名字："+Thread.currentThread().getName());
                    }
                }, "66666");


                thread01.start();
                thread02.start();

            }
        });
    }


}
