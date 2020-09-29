package com.canzhang.sample.copyfast;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.canzhang.sample.base.BaseManager;
import com.canzhang.sample.base.bean.ComponentItem;
import com.example.base.base.AppProxy;
import com.example.simple_test_annotations.MarkManager;

import java.util.ArrayList;
import java.util.List;

@MarkManager(value = "用于快速copy")
public class CopyManager extends BaseManager {


    @Override
    public List<ComponentItem> getSampleItem(Activity activity) {


        List<ComponentItem> list = new ArrayList<>();
        list.add(test());
        return list;
    }


    private ComponentItem test() {

        return new ComponentItem("用于快速copy", new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }


}
