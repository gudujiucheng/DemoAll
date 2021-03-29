package com.canzhang.sample.manager;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.core.app.NotificationCompat;

import com.canzhang.sample.R;
import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.canzhang.sample.manager.antifraud.notification.NotificationTestActivity;
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
        super.getSampleItem(activity);
        List<ComponentItem> list = new ArrayList<>();
        list.add(testJiXing());
        list.add(testAppStatus());
        list.add(testGenerate());
        list.add(testLauncher());
        list.add(testTimeMillis());
        list.add(clearAppData(activity));
        list.add(methodTest());
        list.add(jsonTest());
        list.add(mainTest());
        return list;
    }

    private ComponentItem testJiXing() {
        return new ComponentItem("获取桌面应用包名", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mActivity==null){
                    return;
                }
                isSupport();


            }
        });
    }

    private boolean isSupport() {
        Intent launchIntent = mActivity.getPackageManager().getLaunchIntentForPackage(mActivity.getPackageName());
        if (launchIntent == null) {
            Log.e("CAN_TEST", "Unable to find launch intent for package " + mActivity.getPackageName());
            return false ;
        }
        ComponentName sComponentName = launchIntent.getComponent();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfos = mActivity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String currentHomePackage = resolveInfo.activityInfo.packageName;
            Log.d("CAN_TEST","currentHomePackage:"+currentHomePackage);
        }
        //Turns out framework does not guarantee to put DEFAULT Activity on top of the list.
        ResolveInfo resolveInfoDefault = mActivity.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        validateInfoList(resolveInfoDefault, resolveInfos);

        for (ResolveInfo resolveInfo : resolveInfos) {
            String currentHomePackage = resolveInfo.activityInfo.packageName;
            Log.e("CAN_TEST","currentHomePackage:"+currentHomePackage);
        }
        return true;
    }


    /**
     * Making sure the default Home activity is on top of the returned list
     *
     * @param defaultActivity default Home activity
     * @param resolveInfos    list of all Home activities in the system
     */
    private static void validateInfoList(ResolveInfo defaultActivity, List<ResolveInfo> resolveInfos) {
        int indexToSwapWith = 0;
        int resolveInfosSize = 0;
        if (resolveInfos != null) {
            resolveInfosSize = resolveInfos.size();
            for (int i = 0; i < resolveInfosSize; i++) {
                ResolveInfo resolveInfo = resolveInfos.get(i);
                String currentActivityName = resolveInfo.activityInfo.packageName;
                if (currentActivityName.equals(defaultActivity.activityInfo.packageName)) {
                    indexToSwapWith = i;
                }
            }
        }
        if (resolveInfosSize>1 && resolveInfosSize > indexToSwapWith) {
            Collections.swap(resolveInfos, 0, indexToSwapWith);
        }
    }

    private ComponentItem testAppStatus() {
        return new ComponentItem("测试app当前运行状态", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mActivity==null){
                    return;
                }
                Log.e("CAN_TEST", "isRunBackground:" + AppUtils.isRunBackground(mActivity) + " isRunForeground:" + AppUtils.isRunForeground(mActivity));
            }
        });
    }

    private ComponentItem testLauncher() {
        return new ComponentItem("启动别的activity 页面", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
//                intent.setData(Uri.parse("xgscheme://com.tencent.push/1001?param={\"action\":\"sysNotification\",\"param\":{\"id\":1604226916},\"gameId\":10011,\"notify\":1}"));//过期的消息
//                intent.setData(Uri.parse("xgscheme://com.tencent.push/1001?param={\"action\":\"sysNotification\",\"param\":{\"id\":1604914721},\"gameId\":20003,\"notify\":1}"));//该游戏id下没有角色
//                intent.setData(Uri.parse("xgscheme://com.tencent.push/1001?param={\"action\":\"sysNotification\",\"param\":{\"id\":1604916160},\"gameId\":10011,\"notify\":1}"));//
//                intent.setData(Uri.parse("xgscheme://com.tencent.push/1001?param={\"action\":\"sysNotification\",\"param\":{\"id\":1605151191},\"gameId\":10011,\"notify\":1}"));//
//                intent.setData(Uri.parse("xgscheme://com.tencent.push/1001?param={\"action\":\"sysNotification\",\"param\":{\"id\":1606110600},\"gameId\":1001,\"notify\":1}"));//
//                intent.setData(Uri.parse("xgscheme://com.tencent.push/1001?param={\"action\":\"sysNotification\",\"param\":{\"id\":1606893900},\"gameId\":10011,\"notify\":1}"));//
                intent.setData(Uri.parse("xgscheme://com.tencent.push/1001?param={\"action\":\"sysNotification\",\"param\":{\"id\":1612348200},\"gameId\":1001,\"notify\":1}"));//
                mActivity.startActivity(intent);
                String a = null;
                String b = "1"+a;
                Log.e("CAN_TEST","b:"+b);
            }
        });
    }

    private ComponentItem testGenerate() {
        return new ComponentItem("另外一种生成参数的方法", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//具体参见                https://developer.huawei.com/consumer/cn/doc/development/HMS-Guides/push-basic-capability#h2-1576658726104
                Intent intent = new Intent(Intent.ACTION_VIEW);
                // Scheme协议（pushscheme://com.huawei.codelabpush/deeplink?）需要开发者自定义
                intent.setData(Uri.parse("pushscheme://com.huawei.codelabpush/deeplink?"));
                // 往intent中添加参数，用户可以根据自己的需求进行添加参数
                intent.putExtra("name", "{\"action\":\"sysNotification\",\"param\":{\"id\":1603792829},\"gameId\":10011,\"notify\":1}");
                intent.putExtra("age", 180);
                // 必须带上该Flag
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                String intentUri = intent.toUri(Intent.URI_INTENT_SCHEME);
                // 打印出的intentUri值就是设置到推送消息中intent字段的值
                Log.d("TEST", "intentUri:" + intentUri);
            }
        });
    }


    private ComponentItem testTimeMillis() {
        /**
         * https://github.com/instacart/truetime-android/issues
         */
        return new ComponentItem("修改系统时间、测试时间戳", "时间戳会随着更改系统的时间而更改，如果需要精准，可获取服务器时间，或者使用第三方获取库", new View.OnClickListener() {
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
