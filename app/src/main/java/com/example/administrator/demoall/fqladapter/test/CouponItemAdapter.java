package com.example.administrator.demoall.fqladapter.test;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.example.administrator.demoall.R;
import com.example.administrator.demoall.fqladapter.BaseTypeFooterAdapter;
import com.example.administrator.demoall.fqladapter.BaseViewHolder;
import com.example.administrator.demoall.myadapter.test.TestBean;

import java.util.List;

public class CouponItemAdapter extends BaseTypeFooterAdapter<TestBean, BaseViewHolder<TestBean>> {


    public CouponItemAdapter(Context context, List<TestBean> data, OnItemClickListener<TestBean> itemClickListener) {
        super(context, data, itemClickListener);
    }

    @Override
    public BaseViewHolder<TestBean> onCreateVH(@NonNull ViewGroup parent, int viewType) {
        return new CouponHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_other, parent, false));
    }


}

