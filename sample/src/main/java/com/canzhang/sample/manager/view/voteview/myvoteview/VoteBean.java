package com.canzhang.sample.manager.view.voteview.myvoteview;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class VoteBean {

    //具体投票item
    public static final int VOTE_TYPE = 0;
    //更多item
    public static final int MORE_TYPE = 1;
    //投票按钮类型(点击投票)
    public static final int VOTE_BUTTON_TYPE = 2;

    @IntDef({VOTE_TYPE, MORE_TYPE,VOTE_BUTTON_TYPE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface VoteItemType {

    }
    @VoteItemType int type;
    String title;
    //投票前当前选项是否被选中
    boolean isCheckedOnBeforeVote;
    //投票后当前选项是否被选中
    boolean isCheckedOnAfterVote;
    //是否展示当前item
    boolean isShow;
    //是否需要动画
    public boolean isNeedAnim;
    //当前item投票总量
    int currentItemVoteNum;
    //百分比*100
    int percent;

    public VoteBean(@VoteItemType int type) {
        this.type = type;
    }

    public VoteBean setTitle(String title) {
        this.title = title;
        return this;
    }

    public VoteBean setCheckedOnAfterVote(boolean checkedOnAfterVote) {
        isCheckedOnAfterVote = checkedOnAfterVote;
        return this;
    }

    public VoteBean setCurrentItemVoteNum(int currentItemVoteNum) {
        this.currentItemVoteNum = currentItemVoteNum;
        return this;
    }
}
