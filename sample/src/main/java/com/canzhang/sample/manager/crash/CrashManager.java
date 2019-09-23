package com.canzhang.sample.manager.crash;

import android.app.Activity;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.canzhang.sample.manager.crash.share.FileShareUtils;

import java.util.ArrayList;
import java.util.List;


public class CrashManager extends BaseManager {

    private Activity mActivity;

    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        mActivity = activity;
        List<ComponentItem> list = new ArrayList<>();
        list.add(init());
        list.add(makeException());
        list.add(shareExceptionLog());
        return list;
    }

    private ComponentItem init() {
        return new ComponentItem("初始化异常捕获组件", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrashHandler.getInstance().init(mActivity);
            }
        });
    }

    private ComponentItem shareExceptionLog() {
        return new ComponentItem("分享异常日志文件", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileShareUtils.shareCrashFile(mActivity);
            }
        });
    }


    private ComponentItem makeException() {
        return new ComponentItem("构建一个异常", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 10 / 0;
            }
        });
    }


}
