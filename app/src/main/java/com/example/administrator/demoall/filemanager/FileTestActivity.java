package com.example.administrator.demoall.filemanager;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.administrator.demoall.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_test);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String path = null, path2 = null;
                File file = getExternalCacheDir();//低版本19之下需要权限声明到清单文件，否则会获取不到路径
                if (file != null)
                    path = file.getAbsolutePath();
                File file02 = getCacheDir();//不需要任何权限（也不需要在清单文件做任何声明），任何版本都可以获取成功
                if (file02 != null)
                    path2 = file02.getAbsolutePath();

                Log.e("Test", "目录：\nfile:" + path + "\nfile2:" + path2);

//                try {
//                    DiskLruCacheHelper helper = new DiskLruCacheHelper(FileTestActivity.this);
//                    for (int i = 0; i < 10; i++) {
//                        helper.put("test" + i, "哈哈哈哈哈哈哈哈哈" + i);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


                if (checkWriteExternalStorage()) {//如果不在清单文件里面申请权限的话，在申请权限的时候会直接被拒绝，并无弹窗提示
                    // /storage/emulated/0/Android/data/com.example.administrator.demoall/cache
                    //超出包名以外就需要权限申请了，内部目录则不需要申请权限
                    //异常: /storage/emulated/0/Android/data/xxx.txt: open failed: EACCES (Permission denied)
                    writeFile(new File(file.getParentFile().getParentFile(), "xxx.txt"));
                } else {
                    Log.e("Test", "申请权限");
                    requestReadAndWriteStorage(FileTestActivity.this, 1001);
                }


            }
        });
    }


    // 写文件
    public static void writeFile(File file) {
        try {
            FileOutputStream fo = new FileOutputStream(file);
            fo.write("test".getBytes());
            fo.flush();
            fo.close();
            Log.e("Test 写入完毕", "ok");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("Test 异常", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Test 异常", e.getMessage());
        }
    }


    public boolean checkWriteExternalStorage() {

        return ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;

    }

    public static void requestReadAndWriteStorage(Activity mActivity, int requestCode) {
        ActivityCompat.requestPermissions(mActivity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Test", "申请权限结果：" + requestCode + " " + (grantResults[0] == PackageManager.PERMISSION_DENIED));
    }
}
