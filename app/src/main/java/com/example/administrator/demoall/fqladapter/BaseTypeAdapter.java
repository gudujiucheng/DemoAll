package com.example.administrator.demoall.fqladapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class BaseTypeAdapter<T extends ITypeBean, K extends BaseViewHolder<T>> extends RecyclerView.Adapter<K> {


    private List<T> mData;
    private Context mContext;
    private OnItemClickListener<T> mItemClickListener;

    public interface OnItemClickListener<T> {
        void onItemClick(View view, T item, int position);
    }

    @Override
    public int getItemViewType(int position) {
        if (mData != null && mData.size() > position) {
            return mData.get(position).getType();
        } else {
            return 0;
        }
    }

    public BaseTypeAdapter(Context context, List<T> data, OnItemClickListener<T> itemClickListener) {
        this.mData = data;
        this.mContext = context;
        this.mItemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public abstract K onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull final K holder, final int position) {
        final T itemBean = mData.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(holder.itemView, itemBean, position);
                }
            }
        });
        holder.setDataOnUI(mContext, itemBean, position, mData);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


}
