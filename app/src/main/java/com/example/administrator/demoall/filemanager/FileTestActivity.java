package com.example.administrator.demoall.filemanager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.administrator.demoall.BaseApp;
import com.example.administrator.demoall.R;
import com.example.administrator.demoall.filemanager.disklrucachehelper.DiskLruCacheHelper;

import java.io.File;
import java.io.IOException;

public class FileTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_test);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(checkWriteExternalStorage()){
                String path = null, path2 = null;
                File file = getExternalCacheDir();//低版本19之下需要权限声明到清单文件，否则会获取不到路径
                if (file != null)
                    path = file.getAbsolutePath();
                File file02 = getCacheDir();//不需要任何权限（也不需要在清单文件做任何声明），任何版本都可以获取成功
                if (file02 != null)
                    path2 = file02.getAbsolutePath();

                Log.e("Test", "目录：\nfile:" + path + "\nfile2:" + path2);

                try {
                    DiskLruCacheHelper helper = new DiskLruCacheHelper(FileTestActivity.this);
                    for (int i = 0; i < 10; i++) {
                        helper.put("test" + i, "哈哈哈哈哈哈哈哈哈" + i);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                }else {
//                    Log.e("Test", "申请权限" );
//                    requestReadAndWriteStorage(FileTestActivity.this,1001);
//                }

            }
        });
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
