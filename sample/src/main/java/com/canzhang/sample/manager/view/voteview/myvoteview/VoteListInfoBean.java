package com.canzhang.sample.manager.view.voteview.myvoteview;

public class VoteListInfoBean {
    //当前该组数据是否已经投票
    boolean mIsHasVote;
    //最多选几项
    private int mMaxSelectNum = 1;
    //最大展示数量(多出的隐藏处理)
    public int mMaxShowNum;

    public VoteListInfoBean(int maxSelectNum,boolean isHasVote,int maxShowNum) {
        this.mIsHasVote = isHasVote;
        this.mMaxSelectNum = maxSelectNum;
        this.mMaxShowNum = maxShowNum;
    }
}
