package com.canzhang.sample.manager.img;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Base64;

import com.canzhang.sample.manager.thread.demo.fqlreport.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: canzhang
 * @CreateDate: 2019/6/12 20:35
 */
public class ImageUtils {


    /**
     * fql 获取图片的信息
     *
     * @param filePath
     * @return
     */
    public static HashMap<String, String> getExif(String filePath) {
        if (filePath == null) {
            return null;
        }
        try {
            HashMap<String, String> exifInfo = new HashMap<>();
            ExifInterface exifInterface = new ExifInterface(filePath);
            Class<ExifInterface> cls = ExifInterface.class;
            Field[] fields = cls.getFields();
            for (int i = 0; i < fields.length; i++) {
                String fieldName = fields[i].getName();
                if (!TextUtils.isEmpty(fieldName) && fieldName.startsWith("TAG")) {
                    String fieldValue = fields[i].get(cls).toString();
                    String attribute = exifInterface.getAttribute(fieldValue);
                    if (attribute != null) {
                        exifInfo.put(fieldValue, attribute);
                    }
                }
            }
            LogUtils.log("图片信息:" + exifInfo.toString());
            return exifInfo;
        } catch (Throwable throwable) {
            LogUtils.log("图片信息:" + throwable.getMessage());
        }
        return null;
    }


    public static void setExif(String filePath, HashMap<String, String> exif) {
        if (filePath == null || exif == null || exif.size() <= 0) {
            return;
        }
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            for (Map.Entry<String, String> entry : exif.entrySet()) {
                exifInterface.setAttribute(entry.getKey(), entry.getValue());
            }
            exifInterface.saveAttributes();
            LogUtils.log("setExif");
        } catch (Throwable throwable) {
            LogUtils.log("setExif:" + throwable.getMessage());
        }
    }


    public static void printFileSize(File file) {
        LogUtils.log("printFileSize:" + file.length() / 1024f / 1024f + "M");
    }

    public static void printFileSize02(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            LogUtils.log("printFileSize:" + fis.available() / 1024f / 1024f + "M ");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * base64转为bitmap
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


}
