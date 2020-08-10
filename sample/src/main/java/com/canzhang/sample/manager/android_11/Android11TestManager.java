package com.canzhang.sample.manager.android_11;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.ArrayList;
import java.util.List;


public class Android11TestManager extends BaseManager {

    private Activity mActivity;


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {

        mActivity = activity;
        List<ComponentItem> list = new ArrayList<>();
        list.add(test());
        return list;
    }


    private ComponentItem test() {

        return new ComponentItem("getExternalStorageDirectory", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.with(mActivity)
                        .runtime()
                        .permission(Permission.READ_EXTERNAL_STORAGE)
                        .onGranted(permissions -> {
//                            showToast("权限获取成功");
                            String firstSDPath = getFirstSDPath(mActivity);
                            //Environment.getExternalStorageDirectory() 在 API Level 29 开始已被弃用，开发者应迁移至 Context#getExternalFilesDir(String), MediaStore, 或Intent#ACTION_OPEN_DOCUMENT。
                            Log.e("Amdroid11TestManager", "firstSDPath:" + firstSDPath);
                            showToast(firstSDPath);

                        })
                        .onDenied(permissions -> {
                            showToast("权限获取被拒绝");
                        })
                        .start();

            }
        });
    }


    public static String getFirstSDPath(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return "";
        }
    }
}
