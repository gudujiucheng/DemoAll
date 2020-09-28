package com.canzhang.sample.manager;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.canzhang.sample.utils.AppUtils;
import com.example.simple_test_annotations.MarkManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * 小测试
 */
@MarkManager(value = "其他测试")
public class OtherTestDemoManager extends BaseManager {

    private List<String> mList = new ArrayList<>();

    @Override
    public int getPriority() {
        return 1000;
    }

    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        List<ComponentItem> list = new ArrayList<>();
        list.add(testTimeMillis());
        list.add(clearAppData(activity));
        list.add(methodTest());
        list.add(jsonTest());
        list.add(mainTest());
        return list;
    }

    private ComponentItem testTimeMillis() {
        /**
         * https://github.com/instacart/truetime-android/issues
         */
        return new ComponentItem("修改系统时间、测试时间戳","时间戳会随着更改系统的时间而更改，如果需要精准，可获取服务器时间，或者使用第三方获取库", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
                showToast("当前时间:" + timeStr);
            }
        });
    }

    private ComponentItem clearAppData(Activity activity) {
        return new ComponentItem("清除 app数据，等价重装", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.clearAppUserData(activity);

            }
        });
    }


    private ComponentItem methodTest() {
        return new ComponentItem("入参修改测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentItem testItem = new ComponentItem("测试", (View.OnClickListener) null);
                print(testItem);
                Log.e("Test", "原来的值：" + testItem.name);
            }
        });
    }

    private void print(ComponentItem item) {
        Log.e("Test", "变化前：" + item.name);
        item = new ComponentItem("xxxxx", (View.OnClickListener) null);
        Log.e("Test", "变化后：" + item.name);


    }

    private ComponentItem jsonTest() {
        return new ComponentItem("jsonObject 并发修改异常", "1、不同线程操作jsonObject可能会出现并发修改异常（这里需要多开几个线程，更好复现，狂点击也行）" +
                "\n2、这是因为jsonObject本身是维护了一个LinkedHashMap，同时读写就可能发生并发修改异常", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final JSONObject jsonObject = new JSONObject();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        modifyJson(jsonObject);
                    }
                }).start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            jsonObject.put(Thread.currentThread().getName(), "线程2");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                log("当前json串：" + jsonObject.toString());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            jsonObject.put(Thread.currentThread().getName(), "线程3");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                log("当前json串2：" + jsonObject.toString());
            }
        });
    }

    private void modifyJson(JSONObject jsonObject) {

        try {
            jsonObject.put("1", "张灿");
            jsonObject.put("2", "zhangke");
            jsonObject.put("3", "zdk77777");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
