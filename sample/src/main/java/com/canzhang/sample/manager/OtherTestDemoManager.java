package com.canzhang.sample.manager;

import android.app.Activity;
import android.os.Handler;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * 小测试
 */
public class OtherTestDemoManager extends BaseManager {

    private List<String> mList = new ArrayList<>();


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        List<ComponentItem> list = new ArrayList<>();
        list.add(mainTest());
        return list;
    }


    private ComponentItem mainTest() {
        return new ComponentItem("回调到主线程的测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rand = new Random();
                for (int i = 0; i < 100; i++) {
                    final int j = i;
                    int time = rand.nextInt(100) + 1;//1-100随机取值
                    log("随机time： " + time);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mList.add(j + "");
                        }
                    }, time);

                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Collections.sort(mList, new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                if (Integer.valueOf(o1) > Integer.valueOf(o2)) {
                                    return 1;
                                } else {
                                    return -1;
                                }

                            }
                        });
                        for (int i = 0; i < mList.size(); i++) {
                            log("当前值： " + mList.get(i) + " ");
                        }
                    }
                }, 1000);


            }
        });
    }


}
