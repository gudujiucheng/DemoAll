package com.canzhang.sample.manager.qrcode;

import android.graphics.Bitmap;
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
 * 参考：https://www.jianshu.com/p/a46b5aefa3ff
 */
public class YBNoWhiteQrCodeUtils {

    /**
     * @param expectWidth  期望的宽度
     * @param maxWidth     最大允许宽度
     * @param contents     生成条形码的内容
     * @param expectHeight 期望的高度
     * @return
     */
    public static Bitmap getBarCodeWithoutPadding(int expectWidth, int maxWidth, int expectHeight, String contents) {

        int realWidth = getBarCodeNoPaddingWidth(expectWidth, contents, maxWidth);
        return syncEncodeBarcode(contents, realWidth, expectHeight);
    }

    /**
     * 计算生成无白边的宽度
     *
     * @param expectWidth
     * @param contents
     * @param maxWidth
     * @return
     */
    private static int getBarCodeNoPaddingWidth(int expectWidth, String contents, int maxWidth) {
        boolean[] code = new Code128Writer().encode(contents);

        int inputWidth = code.length;
        Log.d("Test", "code:" + contents + " code.length:" + inputWidth + " expectWidth:" + expectWidth + " maxWidth:" + maxWidth);

        //code:210000000000000082 code.length:134 expectWidth:397 maxWidth:435
        // Add quiet zone on both sides.
        //int fullWidth = inputWidth + 0;

        double outputWidth = (double) Math.max(expectWidth, inputWidth);
        double multiple = outputWidth / inputWidth;
        Log.d("Test", "multiple:" + multiple);
        //multiple:2.962686567164179

        //优先取大的
        int returnVal = 0;
        int ceil = (int) Math.ceil(multiple);
        if (inputWidth * ceil <= maxWidth) {
            returnVal = inputWidth * ceil;
        } else {
            int floor = (int) Math.floor(multiple);
            returnVal = inputWidth * floor;
        }

        Log.d("Test", "returnVal:" + returnVal);
        return returnVal;
    }


    /**
     * 同步创建条形码图片
     *
     * @param content 要生成条形码包含的内容
     * @param width   条形码的宽度，单位px
     * @param height  条形码的高度，单位px
     * @return 返回生成条形的位图
     * <p>
     * 白边问题:
     * https://blog.csdn.net/sunshinwong/article/details/50156017
     * 已知高度,计算宽度:
     */
    private static Bitmap syncEncodeBarcode(String content, int width, int height) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 0);

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, width, height, hints);
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
