package com.canzhang.sample.manager.crash;

import android.app.Activity;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.canzhang.sample.manager.crash.share.FileShareUtils;
import com.example.simple_test_annotations.MarkManager;

import java.util.ArrayList;
import java.util.List;

/**
 * FIXME 怎么防止异常堆栈信息过长
 */
@MarkManager(value = "捕获异常测试")
public class CrashManager extends BaseManager {

    private Activity mActivity;

    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {
        mActivity = activity;
        List<ComponentItem> list = new ArrayList<>();
        list.add(init());
        list.add(makeException());
        list.add(makeCatchException());
        list.add(threadException());
        list.add(shareExceptionLog());
        return list;
    }

    private ComponentItem init() {
        return new ComponentItem("初始化异常捕获组件", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrashHandler.getInstance().init(mActivity);
            }
        });
    }

    private ComponentItem shareExceptionLog() {
        return new ComponentItem("分享异常日志文件", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileShareUtils.shareCrashFile(mActivity);
            }
        });
    }


    private ComponentItem makeException() {
        return new ComponentItem("构建一个未捕获异常", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 10 / 0;
            }
        });
    }

    private ComponentItem makeCatchException() {
        return new ComponentItem("构建一个已经捕获异常","看看已经捕获的还会不会走CrashHandler（答案：不会）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int a = 10 / 0;
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }


    private ComponentItem threadException() {
        return new ComponentItem("构建一个线程嵌套线程异常","观测堆栈会不会过长（反而变得更短了...）", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                       test();
                                    }
                                },"Thread 3").start();
                            }
                        },"Thread 2").start();
                    }
                },"Thread 1").start();


            }
        });
    }

    private void test(){
        int a = 10 / 0;
    }


}
