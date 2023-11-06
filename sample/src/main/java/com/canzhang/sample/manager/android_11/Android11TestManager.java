package com.canzhang.sample.manager.android_11;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.example.base.utils.FileUtil;
import com.example.simple_test_annotations.MarkManager;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.ArrayList;
import java.util.List;

@MarkManager(value = "android11适配测试")
public class Android11TestManager extends BaseManager {
    private Activity mActivity;
    private String AUTHORITY = "com.tencent.replayHelper.provider";
    private String ACTION_HELP_JSON = "ACTION_HELP_JSON";

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {

        mActivity = activity;
        List<ComponentItem> list = new ArrayList<>();
        list.add(test());
        list.add(newWrite());
        list.add(newRead());
        list.add(storageTest());
        list.add(readTest());
        //无需权限，但是需要  <queries>  声明
        list.add(testProviderWrite());
        list.add(testProviderRead());
        list.add(testProviderDelete());

        return list;
    }

    /**
     * 在高版本上面，如果想读一个app的数据，还需要通过 <queries> 标签添加要读app的白名单，这样才能读到，比如我们要读A app的，就得声明A app的包名
     *
     * @return
     */
    private ComponentItem testProviderWrite() {
        return new ComponentItem("外部存储不行了，使用provider 写", v -> {
            // 在A应用中，创建一个ContentValues对象，将字符串作为键值对添加到其中
            ContentValues contentValues = new ContentValues();
            contentValues.put(ACTION_HELP_JSON, "Hello, world! by can 222222");

            // 使用ContentResolver将数据插入到ContentProvider中
            mActivity.getContentResolver().insert(getProviderUri(), contentValues);
        });
    }

    private Uri getProviderUri() {
        return Uri.parse("content://" + AUTHORITY + "/" + ACTION_HELP_JSON);
    }

    private ComponentItem testProviderRead() {
        return new ComponentItem("外部存储不行了，使用provider 读", v -> {
            Cursor cursor = mActivity.getContentResolver().query(getProviderUri(), null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(ACTION_HELP_JSON);
                String myString = cursor.getString(columnIndex);
                showToast("获取的值：" + myString);
                cursor.close();
            } else {
                showToast("没有读到值");
            }
        });
    }


    private ComponentItem testProviderDelete() {
        return new ComponentItem("外部存储不行了，使用provider 删", v -> {
            int deletedRows = mActivity.getContentResolver().delete(getProviderUri(), null, null);
            if (deletedRows > 0) {
                showToast("删除成功");
            } else {
                showToast("删除失败");
            }
        });
    }

    private ComponentItem newWrite() {
        return new ComponentItem("android 存储外部目录文件测试(兼容高版本api，官方写法)", v -> {

            AndPermission.with(mActivity)
                    .runtime()
                    .permission(Permission.READ_EXTERNAL_STORAGE)
                    .onGranted(permissions -> {
                        FileUtil.saveStringToPublicDownLoadPath(mActivity, "cannnzhang", "我是新的api 哈哈哈哈wwwwww333333333333");
                    })
                    .onDenied(permissions -> {
                        showToast("权限获取被拒绝");
                    })
                    .start();
        });
    }

    private ComponentItem newRead() {
        return new ComponentItem("android 读取外部目录测试(兼容高版本api，官方写法)", v -> {

            AndPermission.with(mActivity)
                    .runtime()
                    .permission(Permission.READ_EXTERNAL_STORAGE)
                    .onGranted(permissions -> {
                        String s = FileUtil.readStringFromPublicDownLoadPath(mActivity, "cannnzhang");
                        showToast("结果：" + s);
                    })
                    .onDenied(permissions -> {
                        showToast("权限获取被拒绝");
                    })
                    .start();
        });
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
                        showToast("结果：" + s);
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
