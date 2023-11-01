package com.canzhang.sample.manager.android_11;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.example.simple_test_annotations.MarkManager;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.ArrayList;
import java.util.List;

@MarkManager(value = "android11适配测试")
public class Android11TestManager extends BaseManager {
    private Activity mActivity;

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {

        mActivity = activity;
        List<ComponentItem> list = new ArrayList<>();
        list.add(test());
        list.add(storageTest());
        list.add(readTest());
        return list;
    }

    private ComponentItem storageTest() {
        return new ComponentItem("android 存储文件到外部目录测试", v -> {

            AndPermission.with(mActivity)
                    .runtime()
                    .permission(Permission.READ_EXTERNAL_STORAGE)
                    .onGranted(permissions -> {

                        //虽然可以正常写入，但是实际测试别的app 读取不到

                        FileUtil.writeFile(Environment.getExternalStorageDirectory() + "/Download/actionhelp.json", "哈哈哈哈哈哈");
                        showToast("权限获取成功");
                    })
                    .onDenied(permissions -> {
                        showToast("权限获取被拒绝");
                    })
                    .start();
        });
    }

    private ComponentItem readTest() {
        return new ComponentItem("android 读取外部目录测试", v -> {

            AndPermission.with(mActivity)
                    .runtime()
                    .permission(Permission.READ_EXTERNAL_STORAGE)
                    .onGranted(permissions -> {
                        //这个Download 似乎比较特殊，在android 13上 ，读写都能成功（不同app之间也可以）
                        String s = FileUtil.readFile(Environment.getExternalStorageDirectory() + "/Download/actionhelp.json");
                        showToast("结果："+s);
                    })
                    .onDenied(permissions -> {
                        showToast("权限获取被拒绝");
                    })
                    .start();
        });
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
