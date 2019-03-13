package com.example.administrator.demoall.fqladapter.test;

import android.content.Context;
import android.view.View;

import com.example.administrator.demoall.fqladapter.BaseViewHolder;
import com.example.administrator.demoall.myadapter.test.TestBean;

import java.util.List;

public class CouponHolder extends BaseViewHolder<TestBean> {


    public CouponHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setDataOnUI(final Context context, final TestBean item, final int position, List<TestBean> data) {


    }


}
