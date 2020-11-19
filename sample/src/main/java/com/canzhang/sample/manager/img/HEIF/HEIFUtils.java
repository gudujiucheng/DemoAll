package com.canzhang.sample.manager.img.HEIF;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
//import androidx.heifwriter.HeifWriter;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

//import static androidx.heifwriter.HeifWriter.INPUT_MODE_BUFFER;
//import static androidx.heifwriter.HeifWriter.INPUT_MODE_BITMAP;

public class HEIFUtils {

    /**
     * bitmap转换heif，此外还支持byte[]转换 和一个surface转换
     * 测试 android x 封装的编解码器 进行heif转换
     * @param path
     * @param width
     * @param height
     * @param bitmap
     */
    public static void bitmapSwitchToHeif(String path,int width,int height,Bitmap bitmap) {
//        try {
//            HeifWriter heifWriter =	new HeifWriter.Builder(path,width,height,INPUT_MODE_BITMAP).build();
//            heifWriter.start();
//            heifWriter.addBitmap(bitmap);
//            heifWriter.stop(3000);
//            heifWriter.close();
//
//            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//            retriever.setDataSource(path);
//            String hasImage = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_IMAGE);
//            if (!"yes".equals(hasImage)) {
//                throw new Exception("No images found in file " + path);
//            }else{
//                Log.e("CAN_TEST","------------>>>>>>hasImage:"+hasImage);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("CAN_TEST","------------>>>>>>有异常:"+e.getMessage());
//        }
    }



    /**
     * 是否支持heif
     * @return
     */
    public static boolean isSupportHeif() {
        if("HUAWEI".equals(Build.MANUFACTURER) && Build.VERSION.SDK_INT>27) {
            return true;
        }
        return false;
    }

    /**
     * 读取 heif 文件
     * @param path
     * @return
     * @throws IOException
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public static Drawable getHeifImageFromSdcardUseImageDecoder(String path) throws IOException {
        File file = new File(path);
        ImageDecoder.Source source = ImageDecoder.createSource(file);
        return ImageDecoder.decodeDrawable(source);
    }


    /**
     * 读取 heif文件
     * @param path
     * @return
     */
    public static Bitmap getHeifImageFromSdcardUseBitmapFactory(String path) {
        return BitmapFactory.decodeFile(path);
    }


    /**
     * 图片扫描
     *
     * 实现本地图片的上传、分享或者是发送功能，需要扫描手机本地的图片。
     * 使用 ContentProvider 扫描手机中的图片。通过这种方法扫描，系统会将支持的所有解码格式的图片文件返回给应用，不需要应用自身再去做格式判断。
     * @param context
     */
    public static void getAllImagesLocal(final Context context) {
        AndPermission.with(context)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE,Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(permissions -> {
                    Log.e("CAN_TEST", "权限获取成功");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Cursor cursor = context.getContentResolver().query(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
                            while (cursor.moveToNext()) {
                                String path = cursor.getString(cursor
                                        .getColumnIndex(MediaStore.Images.Media.DATA));
                                if(path!=null&&(path.contains("jpg")||path.contains("png")||path.contains("jpeg"))){
                                    Log.d("CAN_TEST", "image path:" + path);
                                }else{
                                    Log.e("CAN_TEST", "image path:" + path);
                                }

                            }
                        }
                    }).start();


                })
                .onDenied(permissions -> {
                    Log.e("CAN_TEST", "权限获取被拒绝");
                })
                .start();

    }


    /**
     * 图片转换 示例
     * @param context
     */
    public static void switchJPEG(Context context,String resourceFilePath,String desFilePath){
        try {
            Bitmap bmp = getHeifImageFromSdcardUseBitmapFactory(resourceFilePath);
            File file = new File(desFilePath);
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
