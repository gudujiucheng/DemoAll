package com.canzhang.sample.manager.view.voteview.myvoteview;

import android.util.Log;

public class VoteListInfoBean {
    //当前该组数据是否已经投票
    public boolean mIsHasVote;
    //最多选几项
    public int mMaxSelectNum = 1;
    //最大展示数量(多出的隐藏处理)
    public int mMaxShowNum;
    //当前已经选中的数量(用于记录多选场景下 当前已经选中的数量)
    public int mHasSelectedNumBeforeVote;

    public VoteListInfoBean(int maxSelectNum, boolean isHasVote, int maxShowNum) {
        this.mIsHasVote = isHasVote;
        this.mMaxSelectNum = maxSelectNum;
        this.mMaxShowNum = maxShowNum;
    }
}
