package com.canzhang.sample.manager.block;

import android.app.Activity;
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
        return list;
    }

    private ComponentItem makeBlock() {
        return new ComponentItem("制造测试ANR", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

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

        return new ComponentItem("开启新卡顿监测", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlockCanary.getInstance().start();
            }
        });
    }


    private ComponentItem closeNewBlockCheck() {

        return new ComponentItem("关闭新卡顿监测", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlockCanary.getInstance().stop();
            }
        });
    }


}
