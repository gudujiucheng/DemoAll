package com.canzhang.sample.shot;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.util.Log;

import com.example.base.utils.PictureUtils;

import java.nio.ByteBuffer;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScreenshotUtil {

    public static final int REQUEST_MEDIA_PROJECTION = 1;
    private Context mContext;
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mMediaProjection;
    private ImageReader mImageReader;
    private ImageReader mLandScapeImageReader;
    private VirtualDisplay mVirtualDisplay;
    private VirtualDisplay mLandScapeVirtualDisplay;
    private int index;
    private static final int SCREEN_CAPTURE_RETRY_TIMES = 10;
    private static final int SCREEN_CAPTURE_RETRY_WAIT = 200;
    private static final int SAVE_PIC_QUALITY = 10;
    private boolean isRequestInit = false;
    private static final String TAG = "ScreenshotUtil";
    // 默认支持，车机不支持录屏
    private boolean isSupportMediaProjection = true;

    private ScreenshotUtil() {
    }

    private static class SingletonLoader {

        private static final ScreenshotUtil INSTANCE = new ScreenshotUtil();
    }

    public static ScreenshotUtil get() {
        return SingletonLoader.INSTANCE;
    }


    public void init(Activity mActivity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        this.mContext = mActivity.getApplication();
        if (mMediaProjection == null || mVirtualDisplay == null || mLandScapeVirtualDisplay == null) {
            Log.e(TAG, "mMediaProjection == null || mVirtualDisplay == null || mLandScapeVirtualDisplay == null");
            mImageReader = ImageReader
                    .newInstance(ScreenUtil.getScreenWidth(mActivity), ScreenUtil.getScreenHeight(mActivity),
                            PixelFormat.RGBA_8888, 1);
            mLandScapeImageReader = ImageReader
                    .newInstance(ScreenUtil.getScreenHeight(mActivity), ScreenUtil.getScreenWidth(mActivity),
                            PixelFormat.RGBA_8888, 1);
            mediaProjectionManager = (MediaProjectionManager) mActivity
                    .getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            try {
                mActivity.startActivityForResult(mediaProjectionManager
                        .createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
            } catch (ActivityNotFoundException e) {
                isSupportMediaProjection = false;
                Log.e(TAG, e.toString());
            }
        }else{
            mActivity.finish();
        }
        isRequestInit = true;
    }

    public void setData(Intent mResultData) {
        stop();
        mMediaProjection = mediaProjectionManager.getMediaProjection(Activity.RESULT_OK, mResultData);
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                ScreenUtil.getScreenWidth(mContext), ScreenUtil.getScreenHeight(mContext),
                ScreenUtil.getScreenDensityDpi(mContext), DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
        mLandScapeVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                ScreenUtil.getScreenHeight(mContext), ScreenUtil.getScreenWidth(mContext),
                ScreenUtil.getScreenDensityDpi(mContext), DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mLandScapeImageReader.getSurface(), null, null);
    }


    public boolean screenshot(ShotListener mShotListener, boolean landScape) {
        if (mMediaProjection == null || mVirtualDisplay == null || mLandScapeVirtualDisplay == null) {
            mShotListener.onError("未开启截屏功能");
            return false;
        }
        index = 0;
        startCapture(mShotListener, landScape);
        return true;
    }

    public boolean isInit() {
        return !isSupportMediaProjection || mMediaProjection != null;
    }

    private void startCapture(ShotListener mShotListener, boolean landScape) {
        Log.d("CAN_SCREEN_SHOT", "begin startCapture");
        index++;
        if (index > SCREEN_CAPTURE_RETRY_TIMES) {
            mShotListener.onError("截屏失败，重试次数超过了 -> " + mMediaProjection + " -> " + mVirtualDisplay);
            Log.e("CAN_SCREEN_SHOT", "截屏失败，重试次数超过了 -> " + mMediaProjection + " -> " + mVirtualDisplay);
            return;
        }
        Image image;
        if (landScape) {
            image = mLandScapeImageReader.acquireLatestImage();
        } else {
            image = mImageReader.acquireLatestImage();//FIXME can 这里获取不到图像
        }
        if (image == null) {
            Log.e("CAN_SCREEN_SHOT", "get image fail retry times:" + index);
            try {
                Thread.sleep(SCREEN_CAPTURE_RETRY_WAIT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startCapture(mShotListener, landScape);
        } else {

            Bitmap bitmap = covetBitmap(image);
            Log.d("CAN_SCREEN_SHOT", "get image success bitmap:" + bitmap);
            if (bitmap != null) {
                mShotListener.onSuccess(bitmap);
            }
        }
    }

    public void stop() {
        isRequestInit = false;
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
            mVirtualDisplay = null;
        }
        if (mLandScapeVirtualDisplay != null) {
            mLandScapeVirtualDisplay.release();
            mLandScapeVirtualDisplay = null;
        }
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }

    public boolean isRequestInit() {
        return isRequestInit;
    }


    public interface ShotListener {

        void onSuccess(Bitmap bitmap);

        void onError(String message);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Bitmap covetBitmap(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        //每个像素的间距
        int pixelStride = planes[0].getPixelStride();
        //总的间距
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        image.close();
        return bitmap;
    }

    /**
     * 保存 bitmap 到文件
     */
    public void saveBitmap2File(String path, Bitmap bitmap) {
        if (bitmap != null) {
            PictureUtils.saveBitmapToPicture(mContext, bitmap, path);
        }

//        if (bitmap != null) {
//            try {
//                // 创建文件夹
//                File file = new File(path);
//                File directory = new File(file.getParent());
//                if (!directory.exists()) {
//                    directory.mkdirs();
//                }
//                if (!file.exists()) {
//                    file.createNewFile();
//                }
//                FileOutputStream fos = new FileOutputStream(file);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, SAVE_PIC_QUALITY, fos);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    /**
     * 通过decorView屏幕截图
     *
     * @param activity 当前act
     * @param filepath 图片存储路径
     */
    public static void screenshotByDecorView(Activity activity, String filepath) {
        if (activity == null) {
            return;
        }
        //截图
        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
        Bitmap bitmap = activity.getWindow().getDecorView().getDrawingCache();
        ScreenshotUtil.get().saveBitmap2File(filepath, bitmap);
    }

}
