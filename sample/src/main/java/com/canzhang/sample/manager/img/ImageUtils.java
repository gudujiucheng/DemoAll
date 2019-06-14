package com.canzhang.sample.manager.img;

import android.media.ExifInterface;
import android.text.TextUtils;

import com.canzhang.sample.manager.thread.demo.fqlreport.LogUtils;

import java.lang.reflect.Field;
import java.util.HashMap;

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
            LogUtils.log("ExifInterface:"+exifInfo.toString());
            return exifInfo;
        } catch (Throwable throwable) {
            LogUtils.log("ExifInterface:"+throwable.getMessage());
        }
        return null;
    }

}
