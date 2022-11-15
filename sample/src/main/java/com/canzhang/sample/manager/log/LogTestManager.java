package com.canzhang.sample.manager.log;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.example.simple_test_annotations.MarkManager;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@MarkManager(value = "日志捕获测试")
public class LogTestManager extends BaseManager {


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        List<ComponentItem> list = new ArrayList<>();
        list.add(open(activity));
        list.add(close(activity));
        list.add(test(activity));


        return list;
    }

    /**
     * 获取屏幕亮度模式
     * 2. SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * 3. SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度
     */
    private ComponentItem open(final Activity activity) {

        return new ComponentItem("开启日志捕获", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogInfoManager.getInstance().start();

            }
        });
    }

    private ComponentItem close(final Activity activity) {

        return new ComponentItem("关闭日志捕获", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogInfoManager.getInstance().stop();
            }
        });
    }


    private ComponentItem test(final Activity activity) {

        return new ComponentItem("测试日志输出", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CAN_TEST","wwwwwww----哈哈哈哈哈哈哈哈哈");
                //这个捕获不到
                System.out.println("xxxxxxxxx-----哈哈哈哈");
            }
        });
    }

    @Override
    public int getPriority() {
        return 7;
    }
}
