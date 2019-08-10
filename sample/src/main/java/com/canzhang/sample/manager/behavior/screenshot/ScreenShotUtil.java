package com.canzhang.sample.manager.behavior.screenshot;


import android.app.Application;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 注意此截屏功能需要 读文件权限 READ_EXTERNAL_STORAGE
 */
public class ScreenShotUtil {

    private static final String TAG = "ScreenShotListenManager";

    /**
     * 读取媒体数据库时需要读取的列,
     */
    private static final String[] MEDIA_PROJECTIONS = {
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.WIDTH,
            MediaStore.Images.ImageColumns.HEIGHT,
    };

    /**
     * 截屏依据中的路径判断关键字
     */
    private static final String[] KEYWORDS = {
            "screenshot", "screen_shot", "screen-shot", "screen shot",
            "screencapture", "screen_capture", "screen-capture", "screen capture",
            "screencap", "screen_cap", "screen-cap", "screen cap", "截屏", "screenshots"
    };

    private static Point sScreenRealSize;

    /**
     * 已回调过的路径
     */
    private final List<String> sHasCallbackPaths = new ArrayList<String>();

    private Context mContext;

    private OnScreenShotListener mListener;

    private long mStartListenTime;

    /**
     * 内部存储器内容观察者
     */
    private MediaContentObserver mInternalObserver;

    /**
     * 外部存储器内容观察者
     */
    private MediaContentObserver mExternalObserver;

    /**
     * 运行在 UI 线程的 Handler, 用于运行监听器回调
     */
    private final Handler mUiHandler = new Handler(Looper.getMainLooper());

    private ScreenShotUtil() {

    }


    private static class SignletonClassInstance {
        private static final ScreenShotUtil instance = new ScreenShotUtil();
    }


    public static ScreenShotUtil getInstance() {
        return SignletonClassInstance.instance;
    }

    /**
     * 启动监听
     */
    public void startListen(@NonNull Application application) {
        mContext = application.getApplicationContext();
        // 获取屏幕真实的分辨率
        if (sScreenRealSize == null) {
            sScreenRealSize = getRealScreenSize();
        }
        sHasCallbackPaths.clear();

        // 记录开始监听的时间戳
        mStartListenTime = System.currentTimeMillis();

        // 创建内容观察者
        mInternalObserver = new MediaContentObserver(MediaStore.Images.Media.INTERNAL_CONTENT_URI, ScreenShotThread.getInstance().getChildHandler());
        mExternalObserver = new MediaContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ScreenShotThread.getInstance().getChildHandler());

        // 注册内容观察者
        mContext.getContentResolver().registerContentObserver(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                false,
                mInternalObserver
        );
        mContext.getContentResolver().registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                false,
                mExternalObserver
        );
    }

    /**
     * 停止监听
     */
    public void stopListen() {

        // 注销内容观察者
        if (mInternalObserver != null) {
            try {
                mContext.getContentResolver().unregisterContentObserver(mInternalObserver);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mInternalObserver = null;
        }
        if (mExternalObserver != null) {
            try {
                mContext.getContentResolver().unregisterContentObserver(mExternalObserver);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mExternalObserver = null;
        }

        // 清空数据
        mStartListenTime = 0;
        sHasCallbackPaths.clear();
    }

    /**
     * 处理媒体数据库的内容改变
     */
    private void handleMediaContentChange(Uri contentUri) {
        Log.d("SCREEN_SHOT", "handleMediaContentChange:" + contentUri);
        Cursor cursor = null;
        try {
            // 数据改变时查询数据库中最后加入的一条数据
            cursor = mContext.getContentResolver().query(
                    contentUri,
                    MEDIA_PROJECTIONS,
                    null,
                    null,
                    MediaStore.Images.ImageColumns.DATE_ADDED + " desc limit 1"
            );

            if (cursor == null) {
                Log.d("SCREEN_SHOT", "cursor is null");
                return;
            }
            if (!cursor.moveToFirst()) {
                Log.d("SCREEN_SHOT", "cursor not moveToFirst");
                return;
            }

            // 获取各列的索引
            int dataIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            int dateTakenIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN);

            //用来判断宽高的
            int widthIndex = -1;
            int heightIndex = -1;
            if (Build.VERSION.SDK_INT >= 16) {
                widthIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH);
                heightIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT);
            }

            // 获取行数据
            String data = cursor.getString(dataIndex);
            long dateTaken = cursor.getLong(dateTakenIndex);
            int width = 0;
            int height = 0;
            if (widthIndex >= 0 && heightIndex >= 0) {
                width = cursor.getInt(widthIndex);
                height = cursor.getInt(heightIndex);
            }

            // 处理获取到的第一行数据
            handleMediaRowData(data, dateTaken, width, height);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("SCREEN_SHOT", "handleMediaContentChange: 有异常" + e.getMessage());

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }


    /**
     * 处理获取到的一行数据
     */
    private void handleMediaRowData(final String data, long dateTaken, int width, int height) {
        Log.d("SCREEN_SHOT", "handleMediaRowData  data：" + data + " dateTaken:" + dateTaken + " width:" + width + " height:" + height);
        if (checkScreenShot(data, dateTaken, width, height)) {
            Log.d("SCREEN_SHOT", "go go go go");
            if (mListener != null && !checkCallback(data)) {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onShot(data);
                    }
                });
            }
        }
    }

    /**
     * 判断指定的数据行是否符合截屏条件
     */
    private boolean checkScreenShot(String data, long dateTaken, int width, int height) {
        /*
         * 判断依据一: 路径判断
         */
        if (TextUtils.isEmpty(data)) {
            Log.d("SCREEN_SHOT", "checkScreenShot data is null");
            return false;
        }
        if (Build.VERSION.SDK_INT >= 29) {
            File imgFile = new File(data);
            if (imgFile.isFile() && imgFile.exists()) {
                dateTaken = imgFile.lastModified();
                if (dateTaken < mStartListenTime) {
                    Log.d("SCREEN_SHOT", "time is not right");
                    return false;
                }
            } else {
                Log.d("SCREEN_SHOT", "file is not exists");
                return false;
            }
        } else {
            /*
             * 判断依据二: 时间判断
             */
            // 如果加入数据库的时间在开始监听之前
            if (dateTaken < mStartListenTime) {
                Log.d("SCREEN_SHOT", "time is not right");
                return false;
            }

            /*
             * 判断依据三: 尺寸判断
             */
            if (sScreenRealSize != null) {
                // 如果图片尺寸超出屏幕, 则认为当前没有截屏
                if (!((width <= sScreenRealSize.x && height <= sScreenRealSize.y) ||
                        (height <= sScreenRealSize.x && width <= sScreenRealSize.y))) {
                    Log.d("SCREEN_SHOT", "size is not right");

                    return false;
                }
            }
        }
        data = data.toLowerCase();
        // 判断图片路径是否含有指定的关键字之一, 如果有, 则认为当前截屏了
        for (String keyWork : KEYWORDS) {
            if (data.contains(keyWork)) {
                return true;
            }
        }
        Log.d("SCREEN_SHOT", "keyword is not right");
        return false;
    }

    /**
     * 判断是否已回调过, 某些手机ROM截屏一次会发出多次内容改变的通知; <br/>
     * 删除一个图片也会发通知, 同时防止删除图片时误将上一张符合截屏规则的图片当做是当前截屏.
     */
    private boolean checkCallback(String imagePath) {
        if (sHasCallbackPaths.contains(imagePath)) {
            Log.d("SCREEN_SHOT", "已经回调过了 ");
            return true;
        }
        // 大概缓存15~20条记录便可
        if (sHasCallbackPaths.size() >= 20) {
            for (int i = 0; i < 5; i++) {
                sHasCallbackPaths.remove(0);
            }
        }
        sHasCallbackPaths.add(imagePath);
        Log.d("SCREEN_SHOT", "搞定 ");
        return false;
    }

    /**
     * 获取屏幕分辨率
     */
    private Point getRealScreenSize() {
        Point screenSize = null;
        try {
            screenSize = new Point();
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display defaultDisplay = windowManager.getDefaultDisplay();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                defaultDisplay.getRealSize(screenSize);
            } else {
                try {
                    Method mGetRawW = Display.class.getMethod("getRawWidth");
                    Method mGetRawH = Display.class.getMethod("getRawHeight");
                    screenSize.set(
                            (Integer) mGetRawW.invoke(defaultDisplay),
                            (Integer) mGetRawH.invoke(defaultDisplay)
                    );
                } catch (Exception e) {
                    screenSize.set(defaultDisplay.getWidth(), defaultDisplay.getHeight());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenSize;
    }

    /**
     * 设置截屏监听器
     */
    public ScreenShotUtil setListener(OnScreenShotListener listener) {
        mListener = listener;
        return this;
    }

    public interface OnScreenShotListener {
        void onShot(String imagePath);
    }

    /**
     * 媒体内容观察者(观察媒体数据库的改变)
     */
    private class MediaContentObserver extends ContentObserver {

        private Uri mContentUri;

        public MediaContentObserver(Uri contentUri, Handler handler) {
            super(handler);
            Log.d("SCREEN_SHOT", "contentUri:" + contentUri);
            mContentUri = contentUri;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.d("SCREEN_SHOT", "selfChange:" + selfChange);
            handleMediaContentChange(mContentUri);
        }
    }

}