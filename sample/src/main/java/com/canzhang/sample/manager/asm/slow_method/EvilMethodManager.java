package com.canzhang.sample.manager.asm.slow_method;

import android.app.Activity;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.canzhang.sample.debug.DebugBaseApp;
import com.example.simple_test_annotations.MarkManager;

import java.util.ArrayList;
import java.util.List;

@MarkManager(value = "慢方法检测")
public class EvilMethodManager extends BaseManager {


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {


        List<ComponentItem> list = new ArrayList<>();
        list.add(test());
        list.add(testEvilMethod());
        return list;
    }

    @Override
    public int getPriority() {
        return 2;
    }

    private ComponentItem test() {

        return new ComponentItem("打印慢方法排名", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DebugBaseApp.sCustomBlockManager.dump();
            }
        });
    }

    private ComponentItem testEvilMethod() {

        return new ComponentItem("延迟方法", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
