package com.canzhang.sample.base.adapter;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.canzhang.sample.CommonComponentSampleActivity;
import com.canzhang.sample.R;
import com.canzhang.sample.base.bean.ComponentItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class ComponentAdapter extends BaseQuickAdapter<ComponentItem, BaseViewHolder> {
    public ComponentAdapter(int layoutResId, @Nullable List<ComponentItem> data) {
        super(layoutResId, data);
        setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ComponentItem item = (ComponentItem) adapter.getItem(position);
                if (item != null) {
                    if (item.manager != null) {//优先使用通用模板
                        CommonComponentSampleActivity.setManager(item.manager);
                        mContext.startActivity(new Intent(mContext, CommonComponentSampleActivity.class));
                    } else if (item.listener != null) {//自定义实现
                        item.listener.onClick(view);
                    }
                }

            }
        });
    }

    @Override
    protected void convert(BaseViewHolder helper, ComponentItem item) {
        helper.setText(R.id.tv_item, item.name);
        helper.setText(R.id.tv_desc,item.desc);
        helper.setGone(R.id.ll_desc,!TextUtils.isEmpty(item.desc));
    }


}
