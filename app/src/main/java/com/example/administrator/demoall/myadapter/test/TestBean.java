package com.example.administrator.demoall.myadapter.test;

import com.example.administrator.demoall.fqladapter.ITypeBean;
import com.example.administrator.demoall.myadapter.TypeEntity;

public class TestBean implements TypeEntity , ITypeBean {

    private int type;

    public TestBean setType(int type) {
        this.type = type;
        return this;
    }


    @Override
    public int getItemType() {
        return type;
    }

    @Override
    public int getType() {
        return type;
    }
}
