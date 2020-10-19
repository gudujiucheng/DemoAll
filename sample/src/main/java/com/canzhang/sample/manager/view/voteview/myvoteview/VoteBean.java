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
    boolean isChecked;
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

    public VoteBean setChecked(boolean checked) {
        isChecked = checked;
        return this;
    }

    public VoteBean setCurrentItemVoteNum(int currentItemVoteNum) {
        this.currentItemVoteNum = currentItemVoteNum;
        return this;
    }
}
