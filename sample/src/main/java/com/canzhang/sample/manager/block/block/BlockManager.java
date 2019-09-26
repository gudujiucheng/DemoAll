package com.canzhang.sample.manager.block.block;

import android.os.Handler;
import android.os.Looper;

import com.canzhang.sample.manager.appstatus.AppStatus;
import com.canzhang.sample.manager.appstatus.AppStatusChangeListener;


/**
 * Created by owenli on 2018/11/16.
 * 卡顿anr管理
 */
public class BlockManager {

    private static volatile BlockManager mInstance;
    private Handler sHandler = new Handler(Looper.getMainLooper());

    private BlockManager() {
        AppStatus.getInstance().addChangeListener(new AppStatusChangeListener() {
            @Override
            public void onAppToBackground() {
                //后台的时候就关闭检测
                disableBlockChecker();
            }

            @Override
            public void onAppToFront() {
                //前台的时候在开启检测
                setBlockCheckerEnable();
            }

            @Override
            public void onAppFirstCreate() {

            }
        });
    }

    public static BlockManager getInstance() {
        if (mInstance == null) {
            synchronized (BlockManager.class) {
                if (mInstance == null) {
                    mInstance = new BlockManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置卡顿监控是否打开
     */
    public void setBlockCheckerEnable() {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                BlockChecker.getInstance().start();
            }
        });
    }

    public void disableBlockChecker() {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                BlockChecker.getInstance().stop();
            }
        });
    }
}
