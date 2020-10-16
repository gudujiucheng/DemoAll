package com.canzhang.sample.manager.view.voteview.myvoteview;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.canzhang.sample.R;

import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class MyVoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    //投片
    public static final int VOTE_TYPE = 0;
    //更多
    public static final int MORE_TYPE = 1;

    private List<VoteBean> mData;
    private VoteDataBiz mVoteDataBiz;
    private OnItemClickListener mOnItemClickListener;



    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    @Override
    public int getItemViewType(int position) {
        return mData.get(position).type;
    }


    public MyVoteAdapter(VoteDataBiz voteDataBiz, OnItemClickListener itemClickListener) {
        this.mVoteDataBiz = voteDataBiz;
        this.mData = voteDataBiz.mVoteData;
        this.mOnItemClickListener = itemClickListener;
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == VOTE_TYPE) {
            return new VoteHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_vote_item, parent, false));
        } else if (viewType == MORE_TYPE) {
            return new MoreVoteHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_more_vote_item, parent, false));
        } else{
            return null;//TODO
        }


    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        VoteBean bean = mData.get(position);
        //所有item整体点击回调
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
                if(mVoteDataBiz!=null){
                    mVoteDataBiz.onItemClick(bean);
                }
                notifyDataSetChanged();
            }
        });
        if (holder instanceof VoteHolder) {
            //设置是否已经投票的状态
            ((VoteHolder) holder).voteItemView.setSelected(mVoteDataBiz.mIsHasVote);
            ((VoteHolder) holder).voteItemView.setContent(bean.title);
            ((VoteHolder) holder).voteItemView.setNumber(bean.currentItemVoteNum);
            ((VoteHolder) holder).voteItemView.changeChildrenViewStatus(bean.isChecked);
            ((VoteHolder) holder).voteItemView.setTotalNumber(mVoteDataBiz.mTotalVoteNum);
        } else if (holder instanceof MoreVoteHolder) {

        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    class VoteHolder extends RecyclerView.ViewHolder  {

         VoteItemView voteItemView;


        public VoteHolder(View itemView) {
            super(itemView);
            voteItemView = (VoteItemView) itemView.findViewById(R.id.vi_item);
        }

    }


    //类型1
    class MoreVoteHolder extends RecyclerView.ViewHolder {
        TextView mTvDesc;


        public MoreVoteHolder(View itemView) {
            super(itemView);
//            mTvDesc = (TextView) itemView.findViewById(R.id.tv_feature_house_desc);
        }
    }





}
