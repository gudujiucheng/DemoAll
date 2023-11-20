package com.canzhang.sample.shot;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;

import com.example.base.utils.PictureUtils;


public class MessageService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        //仅用于方便测试的
        if (intent != null && "sample_screenshot".equals(intent.getAction())) {
            ScreenshotUtil.get().screenshot(new ScreenshotUtil.ShotListener() {
                @Override
                public void onSuccess(Bitmap bitmap) {
                    Log.d("CAN_SCREEN_SHOT","bitmap："+bitmap);
                    PictureUtils.saveBitmapToPicture(getApplication(), bitmap, "can_app/testX.png");
                }

                @Override
                public void onError(String message) {
                    Log.e("CAN_SCREEN_SHOT","截图失败："+message);
                }
            }, false);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
