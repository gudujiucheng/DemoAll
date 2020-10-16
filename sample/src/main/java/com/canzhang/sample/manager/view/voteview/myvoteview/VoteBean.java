package com.canzhang.sample.manager.view.voteview.myvoteview;

public class VoteBean {
    int type;
    String title;
    boolean isChecked;
    boolean isShow;
    //当前item投票总量
    int currentItemVoteNum;
    //百分比*100
    int percent;

    public VoteBean(int type) {
        this.type = type;
    }

    public VoteBean setTitle(String title) {
        this.title = title;
        return this;
    }

    public VoteBean setChecked(boolean checked) {
        isChecked = checked;
        return this;
    }

    public VoteBean setCurrentItemVoteNum(int currentItemVoteNum) {
        this.currentItemVoteNum = currentItemVoteNum;
        return this;
    }
}
