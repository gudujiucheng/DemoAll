package com.canzhang.sample.manager.view.voteview.myvoteview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.canzhang.sample.R;


/**
 * 投票的子 view
 */
public class VoteItemView extends LinearLayout {

    private ProgressBar progressBar;
    private TextView tvVoteName;
    private TextView mTvNumber;
    private TextView mTvPercent;


    private AnimatorSet mAnimatorSet;

    private int mAnimationRate = 600;


    public VoteItemView(Context context) {
        this(context, null);
    }

    public VoteItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoteItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.vote_item_view, this);
        initView();
        initAnimation();
    }

    private void initView() {
        progressBar = findViewById(R.id.progress_view);
        tvVoteName = findViewById(R.id.tv_name);
        mTvNumber = findViewById(R.id.tv_number);
        mTvPercent = findViewById(R.id.tv_percent);
        mTvNumber.setAlpha(0.0f);
    }

    public void setContent(String content) {
        tvVoteName.setText(content);
    }

    public void setNumber(int number) {
        mTvNumber.setText(number + "票");
    }

    public void setPercent(float percent) {
        mTvPercent.setText(percent + "%");
    }


    private void initAnimation() {
        mAnimatorSet = new AnimatorSet();
        Animator[] arrayAnimator = new Animator[2];
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(tvVoteName, "x", 30);
        arrayAnimator[0] = objectAnimator;
        objectAnimator = ObjectAnimator.ofFloat(mTvNumber, "alpha", 1.0f);
        arrayAnimator[1] = objectAnimator;
        mAnimatorSet.playTogether(arrayAnimator);
        mAnimatorSet.setDuration(mAnimationRate);
    }

    public void setIsHasVote(boolean selected, float percent) {//这里是设置是否已经投片的状态
        setSelected(selected);
        setChildViewStatus(selected, percent);
        if (selected) {
            start();
        } else {
            cancel();
        }
    }


    public void start() {
        post(new Runnable() {
            @Override
            public void run() {
                mAnimatorSet.start();
            }
        });
    }

    public void cancel() {
        post(new Runnable() {
            @Override
            public void run() {
                mAnimatorSet.cancel();
            }
        });
    }


    private void setChildViewStatus(boolean isSelected, float percent) {
        if (isSelected) {
            progressBar.post(new Runnable() {
                @Override
                public void run() {
                    progressBarAnimation(progressBar, percent);
                }
            });
            mTvNumber.setVisibility(VISIBLE);
            mTvNumber.setAlpha(0.0f);
        } else {
            progressBar.setProgress(0);
            mTvNumber.setVisibility(GONE);
            tvVoteName.setTextColor(Color.parseColor("#8D9799"));
            tvVoteName.setCompoundDrawables(null, null, null, null);
            tvVoteName.animate().translationX(0).setDuration(mAnimationRate).start();

        }
    }


    private void progressBarAnimation(final ProgressBar progressBar, float percent) {
        float result = percent * 100;
        ValueAnimator animator = ValueAnimator.ofInt(0, (int) Math.ceil(result)).setDuration(mAnimationRate);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                progressBar.setProgress((int) valueAnimator.getAnimatedValue());
            }
        });
        animator.start();
    }


    /**
     * 设置view选中状态
     *
     * @param status
     */
    public void setVoteItemIsChecked(boolean status) {
        //选中文字颜色
        tvVoteName.setTextColor(Color.parseColor(status ? "#00C0EB" : "#8D9799"));
        //数字颜色
        mTvNumber.setTextColor(Color.parseColor(status ? "#00C0EB" : "#8D9799"));
        //带勾选框
        Drawable right = getResources().getDrawable(status ? R.mipmap.vote_selected : R.mipmap.vote_empty);
        right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
        tvVoteName.setCompoundDrawables(null, null, right, null);
        //进度条颜色设置
        progressBar.setProgressDrawable(getResources().getDrawable(status ? R.drawable.select_progress_view_bg : R.drawable.unselect_progress_view_bg));
        setBackgroundResource(status ? R.drawable.select_bg : R.drawable.unselect_bg);
    }

}
