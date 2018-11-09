package com.example.administrator.demoall.storage;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.administrator.demoall.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class StorageActivity extends AppCompatActivity {
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        findViewById(R.id.tv_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        createFile("哈哈哈哈哈哈",i++ +".txt");
                        Log.e("Test","开始创建"+i);
                    }
                }).start();
            }
        });
    }




    public  void createFile(String data, String key) {
        try {
            File file = getCacheFile(key);
            // 创建缓存数据到磁盘，就是创建文件
            synchronized (getCacheFileLock(file)) {
                writeTextFile(file, data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  File getCacheFile(String key) {
        File file = new File(getDiskCacheDir(this,"net"), key);
        if (!file.getParentFile().exists()) {
           boolean isSuccess =  file.getParentFile().mkdirs();
           if(isSuccess)
            Log.e("Test","创建文件夹成功");
        }
        return file;
    }





    private static String getCacheFileLock(File file) {
        return file.getName().intern();
    }

    public  void writeTextFile(File file, String data) throws IOException {
        try {
            FileOutputStream out = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(out);
            bos.write(data.getBytes());
            bos.flush();
            bos.close();
            out.close();
            Log.e("Test","生成文件成功");

            File fileParent =getDiskCacheDir(this,"net");;
            String[] filelist = fileParent.list();
            for (int j = 0; j <filelist.length ; j++) {
                Log.e("Test","存在的文件："+filelist[j]);
            }
        } catch (FileNotFoundException e) {
           e.printStackTrace();
        }
    }



    private File getDiskCacheDir(Context context, String uniqueName)
    {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable())
        {
            cachePath = context.getExternalCacheDir().getPath();//TODO  对比存储目录的不同
        } else
        {
            cachePath = context.getCacheDir().getPath();//TODO  对比存储目录的不同点（这个文件夹默认是隐藏的）
        }
        return new File(cachePath + File.separator + uniqueName);
//        前者获取到的就是 /sdcard/Android/data/<application package>/cache 这个路径，
//        而后者获取到的是 /data/data/<application package>/cache 这个路径。
    }
}
