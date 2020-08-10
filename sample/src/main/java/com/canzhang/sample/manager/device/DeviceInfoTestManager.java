package com.canzhang.sample.manager.device;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DeviceInfoTestManager extends BaseManager {

    private Activity mActivity;
    private static String mAbi;


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {

        mActivity = activity;
        List<ComponentItem> list = new ArrayList<>();
        list.add(test());
        return list;
    }


    private ComponentItem test() {

        return new ComponentItem("查看cpu支持的ABI架构", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("CPU_TYPE:"+_cpuType());
            }
        });
    }


    /**
     * weex用法
     * @return
     */
    private  String _cpuType() {
        if (TextUtils.isEmpty(mAbi)) {
            try {
                mAbi = Build.CPU_ABI;
            } catch (Throwable var1) {
                var1.printStackTrace();
                mAbi = "armeabi";
                log("异常："+var1.getMessage());
            }

            if (TextUtils.isEmpty(mAbi)) {
                mAbi = "armeabi";
                log("异常：没有获取到");
            }

            mAbi = mAbi.toLowerCase(Locale.ROOT);
        }

        return mAbi;
    }

}
