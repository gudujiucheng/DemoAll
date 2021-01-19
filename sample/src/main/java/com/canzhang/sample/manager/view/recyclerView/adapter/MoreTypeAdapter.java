package com.canzhang.sample.manager.view.recyclerView.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

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

        if (holder instanceof TwoColumnHolder) {
            ((TwoColumnHolder) holder).tvName.setText(bean.getText());
            /**
             * TODO 针对textview 获取宽度偶现异常，暂时未找到原因（需要重新梳理一遍绘制相关内容），获取宽度为o的时候，表现的没有父布局
             */
            ((TwoColumnHolder) holder).tvName.post(new Runnable() {
                @Override
                public void run() {
                    int width = ((TwoColumnHolder) holder).tvName.getWidth();
                    if(width==0){
                        Log.e("CAN_TEST","--------->>>>>width:"+ width +" text:"+bean.getText());
                    }else{
                        Log.d("CAN_TEST","--------->>>>>width:"+ width +" text:"+bean.getText());
                    }

                    ViewParent parent = holder.itemView.getParent();
                    int index= 1;
                    while (parent!=null){//从日志可以看出 被塞入了ListView的时候出的问题
                        Log.d("CAN_TEST","parent :"+index+" "+parent.getClass().getSimpleName());
                        parent = parent.getParent();
                        index++;
                    }
                    if(index==1){
                        Log.e("CAN_TEST","--------->>>>>异常 无父布局: text:"+bean.getText());
                    }

                }
            });
            ((TwoColumnHolder) holder).imageView.post(new Runnable() {
                @Override
                public void run() {
                    int width = ((TwoColumnHolder) holder).imageView.getWidth();
                    if(width==0){
                        Log.e("CAN_TEST","------xxxx--->>>>>width:"+ width +" text:"+bean.getText());
                    }else{
                        Log.d("CAN_TEST","-------xxxx-->>>>>width:"+ width +" text:"+bean.getText());
                    }
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
        TextView tvName;
        ImageView imageView;
        public TwoColumnHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            imageView = itemView.findViewById(R.id.iv_img);
        }
    }


}