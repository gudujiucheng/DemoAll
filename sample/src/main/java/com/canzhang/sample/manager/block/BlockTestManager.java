package com.canzhang.sample.manager.block;

import android.app.Activity;
import android.view.View;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.canzhang.sample.manager.block.block.BlockManager;

import java.util.ArrayList;
import java.util.List;


public class BlockTestManager extends BaseManager {


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {


        List<ComponentItem> list = new ArrayList<>();
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

        return new ComponentItem("开启监测", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlockManager.getInstance().setBlockCheckerEnable();
            }
        });
    }



    private ComponentItem closeBlockCheck() {

        return new ComponentItem("关闭监测", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlockManager.getInstance().disableBlockChecker();
            }
        });
    }


}
