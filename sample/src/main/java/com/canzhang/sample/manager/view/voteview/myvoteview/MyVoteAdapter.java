package com.canzhang.sample.manager.view.voteview.myvoteview;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.canzhang.sample.R;
import com.example.base.utils.ToastUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyVoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    //具体投票item
    public static final int VOTE_TYPE = 0;
    //更多item
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
                switch (bean.type){
                    case MyVoteAdapter.MORE_TYPE:
                        ToastUtil.toastShort("跳转详情页面");
                        break;
                    case MyVoteAdapter.VOTE_TYPE:
                        if(mVoteDataBiz!=null){//需要考虑不同的item类型
                            if(mVoteDataBiz.isHasVote()){//不能取消选择
                                ToastUtil.toastShort("跳转详情页面");
                                return;
                            }
                            mVoteDataBiz.onItemClick(bean);
                            notifyDataSetChanged();
                        }
                        break;
                    default:
                        break;

                }



            }
        });
        if (holder instanceof VoteHolder) {
            VoteHolder voteHolder = (VoteHolder) holder;
            ViewGroup.LayoutParams layoutParams = voteHolder.itemView.getLayoutParams();
            if(bean.isShow){
                voteHolder.itemView.setVisibility(View.VISIBLE);
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            }else{
                voteHolder.itemView.setVisibility(View.GONE);
                layoutParams.height=0;
                layoutParams.width=0;
            }
            voteHolder.itemView.setLayoutParams(layoutParams);
            voteHolder.itemView.setVisibility(bean.isShow?View.VISIBLE:View.GONE);
            //设置是否已经投票的状态
            voteHolder.voteItemView.setIsHasVote(mVoteDataBiz.isHasVote(),bean.percent,bean.isNeedAnim);
            bean.isNeedAnim = false;
            voteHolder.voteItemView.setContent(bean.title);
            voteHolder.voteItemView.setNumber(bean.currentItemVoteNum);
            voteHolder.voteItemView.setPercent(bean.percent);
            voteHolder.voteItemView.setVoteItemIsChecked(bean.isChecked);
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
