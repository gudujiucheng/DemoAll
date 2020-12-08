package com.canzhang.sample.manager.view.recyclerView.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.canzhang.sample.R;
import com.canzhang.sample.manager.view.recyclerView.bean.AppItemBean;
import com.canzhang.sample.manager.view.recyclerView.bean.PageItem;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.base.view.viewpager.CommonViewPager;
import com.example.base.view.viewpager.ViewPagerHolderCreator;

import java.util.List;

public class MoreTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    //数据
    private List<AppItemBean> datas;
    //回调
    private OnItemClickListener itemClickListener;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getItemType();
    }


    public MoreTypeAdapter(List<AppItemBean> datas, OnItemClickListener itemClickListener) {
        this.datas = datas;
        this.itemClickListener = itemClickListener;
    }


    private View.OnClickListener clickListener;




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == AppItemBean.APP_ITEM_01) {
            return new ViewPageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_app_item_01, parent, false));
        } else if (viewType == AppItemBean.APP_ITEM_02) {
            return new TwoColumnHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_app_item_02, parent, false));
        } else {
            return null;
        }


    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        AppItemBean bean = datas.get(position);
        //所有item整体点击回调
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(holder.itemView, position);
                }


            }
        });

        if (holder instanceof ViewPageHolder) {
            ((ViewPageHolder) holder).viewPager.setPages(bean.getPageItems(), new ViewPagerHolderCreator<AppAdapter.ViewImageHolder>() {
                @Override
                public AppAdapter.ViewImageHolder createViewHolder() {
                    // 返回ViewPagerHolder
                    return new AppAdapter.ViewImageHolder();
                }
            });
        }


    }


    @Override
    public int getItemCount() {
        return datas.size();
    }


    class ViewPageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CommonViewPager<PageItem> viewPager;


        public ViewPageHolder(View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.view_pager);
        }


        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onClick(v);
            }
        }
    }


    //类型1
    class TwoColumnHolder extends RecyclerView.ViewHolder {
        public TwoColumnHolder(View itemView) {
            super(itemView);
        }
    }


}