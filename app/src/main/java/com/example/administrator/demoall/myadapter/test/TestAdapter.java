package com.example.administrator.demoall.myadapter.test;

import com.example.administrator.demoall.R;
import com.example.administrator.demoall.myadapter.BaseTypeAdapter;
import com.example.administrator.demoall.myadapter.BaseViewHolder;

import java.util.List;

public class TestAdapter extends BaseTypeAdapter<TestBean,BaseViewHolder> {
    public TestAdapter(List<TestBean> data) {
        super(data);
        addItemType(0, R.layout.item_other);
        addItemType(1, R.layout.item_other_02);
    }

    @Override
    protected void convert(BaseViewHolder helper, TestBean item) {
        //这里没有解决一个问题，单独设置holder


    }
}
