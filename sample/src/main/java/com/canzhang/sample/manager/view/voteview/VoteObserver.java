package com.canzhang.sample.manager.view.voteview;

import android.view.View;

public interface VoteObserver {
    void update(View view, boolean status);
    void setTotalNumber(int totalNumber);
}
