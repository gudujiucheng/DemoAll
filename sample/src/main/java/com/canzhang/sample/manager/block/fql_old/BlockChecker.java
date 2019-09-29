package com.canzhang.sample.manager.block.fql_old;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Printer;

import com.example.base.utils.ToastUtil;


/**
 * Created by owenli on 2018/11/8.
 *
 * 更为详细的代码部分参考：http://blog.zhaiyifan.cn/2016/01/16/BlockCanaryTransparentPerformanceMonitor/
 */
public class BlockChecker {

    private static volatile BlockChecker mInstance;
    //这是一个拥有自己looper的子线程，可以用这个looper关联一个handler和其他线程交互信息
    private HandlerThread mBlockThread = new HandlerThread("blockThread");
    private Handler mHandler;
    private boolean enable = false;

    public static BlockChecker getInstance() {
        if (mInstance == null) {
            synchronized (BlockChecker.class) {
                if (mInstance == null) {
                    mInstance = new BlockChecker();
                }
            }
        }
        return mInstance;
    }

    private BlockChecker() {
    }

    private Runnable mBlockRunnable = new Runnable() {
        @Override
        public void run() {
            ToastUtil.toastLong("BlockChecker 检测到卡顿");
        }
    };

    public void start() {
        //防止多次调用
        enable = true;
        if (!mBlockThread.isAlive()) {
            mBlockThread.start();
            mHandler = new Handler(mBlockThread.getLooper());
            Looper.getMainLooper().setMessageLogging(new Printer() {

                private static final String START = ">>>>> Dispatching";
                private static final String END = "<<<<< Finished";

                @Override
                public void println(String x) {
                    //监测打印两次日志的时间间隔，如果超出了预定阈值300毫秒 我们就认为发生了卡顿
                    //打印日志的具体代码位置在Looper 中的 dispatchMessage 方法中
                    // 参考：https://blog.csdn.net/qq_39037047/article/details/79676217
                    if (x.startsWith(START)) {
                        startMonitor();
                    }
                    if (x.startsWith(END)) {
                        removeMonitor();//没达到延迟就移除了，就说明没有卡顿，反之则有卡顿现象
                    }
                }
            });
        }
    }

    public void stop() {
        enable = false;
    }

    public void startMonitor() {
        if (!enable) {
            return;
        }
        mHandler.postDelayed(mBlockRunnable, 300);//设置多长时间发送一次
    }

    public void removeMonitor() {
        mHandler.removeCallbacks(mBlockRunnable);
    }

}
