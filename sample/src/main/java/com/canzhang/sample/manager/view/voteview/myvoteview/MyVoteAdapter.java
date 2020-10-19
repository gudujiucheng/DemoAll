package com.canzhang.sample.manager.view.voteview.myvoteview;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.canzhang.sample.R;
import com.example.base.utils.ToastUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyVoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


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
        if (viewType == VoteBean.VOTE_TYPE) {
            return new VoteHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_vote_item, parent, false));
        } else if (viewType == VoteBean.MORE_TYPE) {
            return new MoreVoteHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_more_vote_item, parent, false));
        } else if (viewType == VoteBean.VOTE_BUTTON_TYPE) {
            return new VoteButtonHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_button_item, parent, false));
        }else{
            return  null;
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
                switch (bean.type) {
                    case VoteBean.VOTE_BUTTON_TYPE:
                        ToastUtil.toastShort("投票实现");//TODO
                        break;
                    case VoteBean.MORE_TYPE:
                        ToastUtil.toastShort("跳转详情页面");
                        break;
                    case VoteBean.VOTE_TYPE:
                        if (mVoteDataBiz != null) {//需要考虑不同的item类型
                            if (mVoteDataBiz.isHasVote()) {//不能取消选择
                                ToastUtil.toastShort("跳转详情页面");
                                return;
                            }
                            //未投票状态
                            mVoteDataBiz.onItemClickBeforeVote(bean);
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
            if (bean.isShow) {
                voteHolder.itemView.setVisibility(View.VISIBLE);
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                voteHolder.itemView.setVisibility(View.GONE);
                layoutParams.height = 0;
                layoutParams.width = 0;
            }
            voteHolder.itemView.setLayoutParams(layoutParams);
            voteHolder.itemView.setVisibility(bean.isShow ? View.VISIBLE : View.GONE);
            //设置是否已经投票的状态
            voteHolder.voteItemView.setIsHasVote(mVoteDataBiz.isHasVote(), bean.percent, bean.isNeedAnim);
            bean.isNeedAnim = false;
            voteHolder.voteItemView.setContent(bean.title);
            voteHolder.voteItemView.setNumber(bean.currentItemVoteNum);
            voteHolder.voteItemView.setPercent(bean.percent);
            voteHolder.voteItemView.setBeforeVoteItemIsChecked(bean.isCheckedOnBeforeVote);
            voteHolder.voteItemView.setAfterVoteItemIsChecked(bean.isCheckedOnAfterVote);
        } else if (holder instanceof MoreVoteHolder) {
            // can delete
        } else if (holder instanceof VoteButtonHolder) {
            // can delete
            ((VoteButtonHolder)holder).mBtVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mVoteDataBiz.onSubmitVote();
                    notifyDataSetChanged();
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    class VoteHolder extends RecyclerView.ViewHolder {

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
//            mTvDesc = (TextView) itemView.findViewById(R.id.bt_vote);
        }
    }


    class VoteButtonHolder extends RecyclerView.ViewHolder {

        Button mBtVote;

        public VoteButtonHolder(View itemView) {
            super(itemView);
            mBtVote = itemView.findViewById(R.id.bt_vote);
        }

    }


}
