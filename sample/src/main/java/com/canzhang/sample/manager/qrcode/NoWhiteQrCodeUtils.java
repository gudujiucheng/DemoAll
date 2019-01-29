package com.canzhang.sample.manager.qrcode;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * 无白边工具类
 * 参考：https://www.jianshu.com/p/a46b5aefa3ff
 */
public class NoWhiteQrCodeUtils {


    /**
     * @param expectWidth  期望的宽度
     * @param contents     生成条形码的内容
     * @param expectHeight
     * @return
     */
    public static int scale = 1;
    public static Bitmap getBarCodeWithoutPadding(int expectWidth, int expectHeight, String contents) {
        //为了避免去掉白边后图片变小问题，进行先放大，在进行缩放，保证图片清晰度
        //TODO 放大生成的bitmap宽度
        int scaleWidth = expectWidth * scale;
        int realWidth = getBarCodeNoPaddingWidth(scaleWidth, contents);
        return syncEncodeBarcode(contents, expectWidth, realWidth, expectHeight);
    }

    /**
     * 同步创建条形码图片
     *
     * @param content       要生成条形码包含的内容
     * @param realCodeWidth 条形码的宽度，单位px
     * @param expectHeight  条形码的高度，单位px
     * @return 返回生成条形的位图
     * <p>
     * 白边问题:
     * https://blog.csdn.net/sunshinwong/article/details/50156017
     * 已知高度,计算宽度:
     */
    private static Bitmap syncEncodeBarcode(String content, int expectWidth, int realCodeWidth, int expectHeight) {
        Log.e("Test", "expectWidth:" + expectWidth + " expectHeight:" + expectHeight + " realCodeWidth:" + realCodeWidth);
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 0);

        //TODO 放大生成的bitmap高度
        int scaleHeight = expectHeight * scale;


        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, realCodeWidth, scaleHeight, hints);
            int[] pixels = new int[realCodeWidth * scaleHeight];
            for (int y = 0; y < scaleHeight; y++) {
                for (int x = 0; x < realCodeWidth; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * realCodeWidth + x] = 0xff000000;
                    } else {
                        pixels[y * realCodeWidth + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(realCodeWidth, scaleHeight, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, realCodeWidth, 0, 0, realCodeWidth, scaleHeight);

            Log.e("Test", "裁剪之前:" +"height"+ bitmap.getHeight() + " width:" + bitmap.getWidth());
            //因为放大了二倍，所以宽度已经发生了变化，为了保证等比缩放，不变模糊，要重新按照期望的比例计算实际高度
            //这里应当根据当前宽度和想要的宽度的比例，获取所想要的高度，进行裁剪。
            if (realCodeWidth != 0) {
                float realCodeHeight = realCodeWidth * expectHeight / (float) expectWidth;
                Log.e("Test", "计算结果：realCodeHeight 应当是:" + realCodeHeight + " realCodeWidth:" + realCodeWidth);
                if (realCodeHeight < scaleHeight) {//要小于bitmap的height 才能剪裁
                    Log.e("Test", "开始剪裁---------->>>>>");
                    Bitmap tempBitmap = Bitmap.createBitmap(bitmap, 0, 0, realCodeWidth, (int) realCodeHeight);
                    if (tempBitmap != null) {
                        Log.e("Test", "裁剪后---------->>>>>"+"height:"+tempBitmap.getHeight()+" width:"+tempBitmap.getWidth());
//                        bitmap = tempBitmap;
                    }
                }

            }
            return bitmap;
//            return  scaleBitmap(bitmap,(float)expectWidth/realCodeWidth);
//            return zoomImage(bitmap, expectWidth, expectHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    private static int getBarCodeNoPaddingWidth(int expectWidth, String contents) {
        boolean[] code = new Code128Writer().encode(contents);
        int inputWidth = code.length;

        double multiple = expectWidth / inputWidth;
        //优先取大的
        int returnVal = 0;
        int ceil = (int) Math.ceil(multiple);
        if (inputWidth * ceil <= expectWidth) {
            returnVal = inputWidth * ceil;
        } else {
            int floor = (int) Math.floor(multiple);
            returnVal = inputWidth * floor;
        }
        return returnVal;
    }

    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param ratio  比例
     * @return 新的bitmap
     */
    public static Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }


    public static Bitmap zoomImage(Bitmap inputBitmap, double newWidth,
                                    double newHeight) {
        if (inputBitmap == null) {
            return null;
        }
        // 获取这个图片的宽和高
        float width = inputBitmap.getWidth();
        float height = inputBitmap.getHeight();// 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

//        Log.e("Test","scaleWidth:"+scaleWidth+" scaleHeight:"+scaleHeight);
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(inputBitmap, 0, 0, (int) width, (int) height, matrix, true);
    }







}
