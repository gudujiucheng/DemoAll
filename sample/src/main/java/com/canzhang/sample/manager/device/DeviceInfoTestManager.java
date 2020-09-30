package com.canzhang.sample.manager.device;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.example.simple_test_annotations.MarkManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@MarkManager(value = "设备信息获取相关")
public class DeviceInfoTestManager extends BaseManager {

    private Activity mActivity;
    private static String mAbi;


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {

        mActivity = activity;
        List<ComponentItem> list = new ArrayList<>();
        //https://blog.csdn.net/liuwan1992/article/details/83625520
        //
        list.add(new ComponentItem("使用时长", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUseInfo();
            }
        }));
        list.add(new ComponentItem("查看cpu支持的ABI架构", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("CPU_TYPE:" + _cpuType());
            }
        }));
        list.add(test());
        return list;
    }


    private ComponentItem test() {

        return new ComponentItem("查看cpu支持的ABI架构2", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isARMv7Compatible();
            }
        });
    }


    /**
     * weex用法
     *
     * @return
     */
    private String _cpuType() {
        if (TextUtils.isEmpty(mAbi)) {
            try {
                mAbi = Build.CPU_ABI;
            } catch (Throwable var1) {
                var1.printStackTrace();
                mAbi = "armeabi";
                log("异常：" + var1.getMessage());
            }

            if (TextUtils.isEmpty(mAbi)) {
                mAbi = "armeabi";
                log("异常：没有获取到");
            }

            mAbi = mAbi.toLowerCase(Locale.ROOT);
        }

        return mAbi;
    }


    //jianshu.com/p/d65885e1664c
    public void isARMv7Compatible() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                for (String abi : Build.SUPPORTED_32_BIT_ABIS) {//此设备支持对的32位ABI列表，第一个是最优先的
                    log("cpu-1-支持的序列:" + abi);
                    if (abi.equals("armeabi-v7a")) {
                        log("cpu-1-支持 armeabi-v7a");
                    }
                }
                for (String abi : Build.SUPPORTED_64_BIT_ABIS) {//此设备支持对的64位ABI列表，第一个是最优先的
                    log("cpu-2-支持的序列:" + abi);
                }
                for (String abi : Build.SUPPORTED_ABIS) {//此设备支持的ABI列表，第一个是最优先的
                    log("cpu-3-支持的序列:" + abi);
                }
            } else {
                log("cpu-4-支持的序列:" + Build.CPU_ABI);//The name of the instruction set (CPU type + ABI convention) of native code.
                log("cpu-5-支持的序列:" + Build.CPU_ABI2);//The name of the second instruction set (CPU type + ABI convention) of native code
                if (Build.CPU_ABI.equals("armeabi-v7a") || Build.CPU_ABI.equals("areabi-v8a")) {
                    log("cpu-6-支持 armeabi-v7a");
                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
            log("有异常：" + e.getMessage());
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getUseInfo() {
        UsageStatsManager usm = (UsageStatsManager) mActivity.getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_WEEK, -2);//
        long startTime = calendar.getTimeInMillis();
/**
 * 最近两周启动过所用app的List
 * queryUsageStats第一个参数是根据后面的参数获取合适数据的来源，有按天，按星期，按月，按年等。
 *  UsageStatsManager.INTERVAL_BEST
 *   UsageStatsManager.INTERVAL_DAILY 按天
 *   UsageStatsManager.INTERVAL_WEEKLY 按星期
 *   UsageStatsManager.INTERVAL_MONTHLY 按月
 *   UsageStatsManager.INTERVAL_YEARLY 按年
 */

        List<UsageStats> list = usm.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY, startTime, endTime);
//需要注意的是5.1以上，如果不打开此设置，queryUsageStats获取到的是size为0的list；
        if (list.size() == 0) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                try {
                    mActivity.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                } catch (Exception e) {
                    Toast.makeText(mActivity, "无法开启允许查看使用情况的应用界面", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        } else {
            for (UsageStats usageStats : list) {
                String packageName = usageStats.getPackageName();//获取包名
                long firstTimeStamp = usageStats.getFirstTimeStamp();//获取第一次运行的时间
                long lastTimeStamp = usageStats.getLastTimeStamp();//获取最后一次运行的时间
                long totalTimeInForeground = usageStats.getTotalTimeInForeground();//获取总共运行的时间

                log("packageName:" + packageName);
                log("firstTimeStamp:" + firstTimeStamp);
                log("lastTimeStamp:" + lastTimeStamp);
                log("totalTimeInForeground:" + totalTimeInForeground/1000f/60f/60f+"h");

                try {
                    Field field = usageStats.getClass().getDeclaredField("mLaunchCount");//获取应用启动次数，UsageStats未提供方法来获取，只能通过反射来拿到
                    log("field:" + field.get(usageStats));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
