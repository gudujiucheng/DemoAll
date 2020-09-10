package com.canzhang.sample.manager.block;

import android.app.Activity;
import android.os.Debug;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.canzhang.sample.manager.block.fql_old.BlockManager;
import com.canzhang.sample.manager.block.githup_test_refactor.BlockCanary;

import java.util.ArrayList;
import java.util.List;


public class BlockTestManager extends BaseManager {


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {


        List<ComponentItem> list = new ArrayList<>();
        list.add(openNewBlockCheck());
        list.add(closeNewBlockCheck());
        list.add(openBlockCheck());
        list.add(closeBlockCheck());
        list.add(makeBlock());
        list.add(testEvilMethod());
        return list;
    }

    private ComponentItem makeBlock() {

        return new ComponentItem("制造测试ANR", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始埋点，“block_test”是最后生成的性能分析文件
                Debug.startMethodTracing("block_test");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //埋点结束，期间start 到 stop 之间的代码，就是你要测试的代码范围
                Debug.stopMethodTracing();
            }
        });

    }


    private ComponentItem openBlockCheck() {

        return new ComponentItem("开启fql老监控监测", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlockManager.getInstance().setBlockCheckerEnable();
            }
        });
    }


    private ComponentItem closeBlockCheck() {

        return new ComponentItem("关闭fql老监控监测", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlockManager.getInstance().disableBlockChecker();
            }
        });
    }


    private ComponentItem openNewBlockCheck() {

        return new ComponentItem("开启新卡顿监测(BlockCanary)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlockCanary.getInstance().start();
            }
        });
    }


    private ComponentItem closeNewBlockCheck() {

        return new ComponentItem("开启新卡顿监测(BlockCanary)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlockCanary.getInstance().stop();
            }
        });
    }


    private ComponentItem testEvilMethod() {

        return new ComponentItem("测试慢方法检测", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testJank();
            }
        });
    }


    //点击按钮触发 为放大耗时，循环执行200次
    public void testJank() {
        for (int i = 0; i < 200; i++) {
            wrapper();
        }
    }

    //包装方法用于测试调用深度
    void wrapper() {
        tryHeavyMethod();
    }

    //dump内存是耗时方法
    private void tryHeavyMethod() {
        Debug.getMemoryInfo(new Debug.MemoryInfo());
    }

}
