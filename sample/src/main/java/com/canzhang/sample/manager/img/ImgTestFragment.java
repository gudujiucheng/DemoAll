package com.canzhang.sample.manager.img;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.thread.demo.fqlreport.LogUtils;
import com.example.base.base.BaseFragment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Handler;


public class ImgTestFragment extends BaseFragment {


    public static Fragment newInstance() {
        return new ImgTestFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sample_fragment_img_test, container, false);
        initView(view);
        return view;
    }


    private void initView(View view) {
        ImageView iv = view.findViewById(R.id.iv);

        view.findViewById(R.id.bt_get_img_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputStream inputStream = getAssetsInputStream(mContext, "test_img.png");
//                iv.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                copyFileToCacheDir(mContext, inputStream);
            }
        });

    }


    /**
     * 根据路径获取Bitmap图片
     *
     * @param context
     * @param path
     * @return
     */
    public InputStream getAssetsInputStream(Context context, String path) {
        AssetManager am = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = am.open(path);
        } catch (IOException e) {
            e.printStackTrace();
            log(e.getMessage());
        }
        log("get getAssetsInputStream");
        return inputStream;
    }


    public void copyFileToCacheDir(Context context, InputStream inputStream) {

        log("copyFileToCacheDir");
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }

        String filePath = cacheDir.getAbsolutePath() + File.separator + "canzhang" +
                File.separator + "photo" + File.separator;
        String fileName = filePath + System.currentTimeMillis() + ".jpg";
        File tempFile = new File(fileName);
        if (!tempFile.getParentFile().exists()) {
            File parentFile = new File(filePath);
            parentFile.mkdirs();
        }
        try {
            //封装数据源
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            //封装目的地
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));

            byte[] bys = new byte[1024];
            int len = 0;
            while ((len = bis.read(bys)) != -1) {
                bos.write(bys, 0, len);
                bos.flush();
            }
            bos.close();
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
            log(e.getMessage());
        }

        /**
         * 测试读取修改图片信息
         */
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", "canzhang");//随便塞入没用
        hashMap.put(ExifInterface.TAG_IMAGE_WIDTH, "777");//某些字段没用
        hashMap.put(ExifInterface.TAG_MODEL, "nukia");//可以
        hashMap.put(ExifInterface.TAG_MAKE, "canzhang");//可以
        ImageUtils.setExif(tempFile.getAbsolutePath(), hashMap);
        ImageUtils.getExif(tempFile.getAbsolutePath());


    }
}
