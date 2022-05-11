package com.canzhang.sample.manager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.canzhang.sample.manager.statusbar.FullscreenActivity;
import com.canzhang.sample.utils.AppUtils;
import com.example.simple_test_annotations.MarkManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
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
        list.add(testStatusBar());
        list.add(testAddUrlParams());
        list.add(testScheme());
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

    int index = 0;

    private ComponentItem testStatusBar() {
        return new ComponentItem("状态栏测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, FullscreenActivity.class));
            }
        });
    }

    private ComponentItem testAddUrlParams() {
        return new ComponentItem("测试uri拼接参数", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "";
                switch (index % 3) {
                    case 0:
                        url = "https://www.baidu.com/s?";//无参数
                        break;
                    case 1:
                        url = "https://www.baidu.com/s?wd=haha";//有参数
                        break;
                    case 2:
                        url = "https://www.baidu.com/s?key=haha";//有同名参数 (测试结果：不会去重)
                        break;
                }
                Uri.Builder builder = Uri.parse(url).buildUpon();
                builder.appendQueryParameter("key", "{\"result\":0,\"returnCode\":0,\"returnMsg\":\"\"}");
                index++;
                Log.e("CAN_TEST", builder.toString());
            }
        });
    }

    private ComponentItem testScheme() {
        return new ComponentItem("通过scheme打开页面", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivity == null) {
                    return;
                }
//                http://tapd.woa.com/gamehelper_client_doc/markdown_wikis/show/#1220383062001980329

                //cf 打开url 的方法   其中要求 三个参数都必须有值
//                String url = "cfpage://webopenapi?action=20002&gameId=1&url=https://bb.img.qq.com/clsq/protocol/cf/protocol.html";
//                String url = "cfpage://webopenapi?action=20002&gameId=10011&url=https://cf.qq.com/act/5326/a20210323ld/index.html";
//                String url = "cfpage://webopenapi/startApp";//可以打开 但是应用在后台的时候 似乎唤起不到前台
//                String url = "cfpage://webopenapi";//可以
//                String url = "cfpage://";//不可以

//                ConfigManager.getInstance().putStringConfig(GlobalData.ArgumentsKey.KEY_START_APP_FROM_BROWSWER_CONFIG, uri.toString());

                JSONObject button = new JSONObject();
                try {
                    button.put("type", 10005);
//                    button.put("belongGameId", 10011); //可以不要了

//                    button.put("uri","com.tencent.gamehelper.ui.moment.TopicMomentActivity");
//                    button.put("uri","momentTopic?id=252&name=#DNF十周年#");
//                    JSONObject param =  new JSONObject();
//                    param.put("id",482);
//                    param.put("name","#Qqqqqqq#");
//                    button.put("param",param);


//                    button.put("uri","moment_detail?feedId=35262&type=0");


//                    button.put("uri", "submitMoment");
//                    JSONObject param = new JSONObject();
//                    param.put("topic", "#如何看待免费皮肤枪#");
//                    param.put("topicGameId", 10038);
////                    param.put("fromPage", "topic");
//                    button.put("param", param);


//                    button.put("uri","homepage?userId=497257227");


//                    JSONObject param = new JSONObject();
////                    param.put("userId", 497257227);
//                    param.put("TO_USER_ID", 497257227);
//                    button.put("param", param);



//                    button.put("uri","submit_work?submit_work_type=1");
                    button.put("uri","submitMoment");


                    Log.e("TEST", button.toString());
                    String url = "cfpage://webopenapi?action=20003&button=" + URLEncoder.encode(button.toString(), "utf-8");
//                    String url = "flutter_host?route=" + Uri.encode("weapon_wiki_list?gameId=10011&name=武器百科");
                    Log.e("TEST", url);


//                    url = "cfpage://webopenapi?action=20003&button=%7b%22type%22%3a10032%2c%22belongGameId%22%3a10011%2c%22uri%22%3a%22https%253A%252F%252Ftest.mwegame.qq.com%252Fact%252Fcfm%252Fa20191210egg%252Findex.html%22%7d";
                    Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    mActivity.startActivity(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private ComponentItem testJiXing() {
        return new ComponentItem("获取桌面应用包名", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivity == null) {
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
            return false;
        }
        ComponentName sComponentName = launchIntent.getComponent();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfos = mActivity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String currentHomePackage = resolveInfo.activityInfo.packageName;
            Log.d("CAN_TEST", "currentHomePackage:" + currentHomePackage);
        }
        //Turns out framework does not guarantee to put DEFAULT Activity on top of the list.
        ResolveInfo resolveInfoDefault = mActivity.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        validateInfoList(resolveInfoDefault, resolveInfos);

        for (ResolveInfo resolveInfo : resolveInfos) {
            String currentHomePackage = resolveInfo.activityInfo.packageName;
            Log.e("CAN_TEST", "currentHomePackage:" + currentHomePackage);
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
        if (resolveInfosSize > 1 && resolveInfosSize > indexToSwapWith) {
            Collections.swap(resolveInfos, 0, indexToSwapWith);
        }
    }

    private ComponentItem testAppStatus() {
        return new ComponentItem("测试app当前运行状态", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivity == null) {
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
                String b = "1" + a;
                Log.e("CAN_TEST", "b:" + b);
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
