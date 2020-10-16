package com.canzhang.sample.manager.view.voteview.myvoteview;

import android.util.Log;
import android.util.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 投片数据管理
 */
public class VoteDataBiz {
    //总投票数量
    public int mTotalVoteNum;
    //投票数据
    Pair<VoteListInfoBean, List<VoteBean>> mVoteListInfoBeanListPair;
    public List<VoteBean> mVoteData;
    private VoteListInfoBean mVoteListInfoBean;
    //最多选几项
    private int mMaxSelectNum = 1;
    //最大展示数量(多出的隐藏处理)
    public int mMaxShowNum;
    //最大选项
    public int maxSelectNum;


    public VoteDataBiz(Pair<VoteListInfoBean, List<VoteBean>> voteListInfoBeanListPair) {
        this.mVoteListInfoBeanListPair = voteListInfoBeanListPair;
        if (mVoteListInfoBeanListPair == null) {
            return;
        }
        mVoteListInfoBean = mVoteListInfoBeanListPair.first;
        mVoteData = mVoteListInfoBeanListPair.second;
        if (mVoteListInfoBean != null) {
            if (maxSelectNum > 0) {
                this.mMaxSelectNum = maxSelectNum;
            }

            this.mMaxShowNum = mVoteListInfoBean.mMaxShowNum;
        }


        mTotalVoteNum = getTotalVoteNum();


        formatData();
    }

    public boolean isHasVote() {
        //注意始终要引用同一个对象
        if (mVoteListInfoBean != null){
            return mVoteListInfoBean.mIsHasVote;
        }else{
            return false;
        }
    }

    public void setHasVote(boolean hasVote) {
        if (mVoteListInfoBean == null){
            return;
        }
        mVoteListInfoBean.mIsHasVote = hasVote;
    }

    private void formatData() {
        if (mVoteData == null || mVoteData.size() == 0) {
            return;
        }
        int num = 0;
        for (int i = 0; i < mVoteData.size(); i++) {
            VoteBean item = mVoteData.get(i);
            if (item.type == MyVoteAdapter.VOTE_TYPE) {
                num++;
                if (num > mMaxShowNum) {//控制展示隐藏
                    item.isShow = false;
                } else {
                    item.isShow = true;
                }
                item.percent = getPercent(item.currentItemVoteNum, mTotalVoteNum);
            }
        }

    }

    /**
     * 获取投票总数
     *
     * @return
     */
    private int getTotalVoteNum() {
        int totalVoteNum = 0;
        if (mVoteData == null || mVoteData.size() == 0) {
            return totalVoteNum;
        }
        for (VoteBean item : mVoteData) {
            totalVoteNum = totalVoteNum + item.currentItemVoteNum;
        }
        return totalVoteNum;
    }

    /**
     * 数据被点击
     *
     * @param bean
     */
    public void onItemClick(VoteBean bean) {
        if (bean == null) {
            return;
        }
        bean.isNeedAnim = true;
        if (mMaxSelectNum == 1) {//单选
            resetAndSelectCurrentItem(bean);
        } else {//FIXME 多选
            //多选应该是点击提交的时候 才更新数据，另外多选的ui样式也不同  这里暂时空值
        }
    }


    /**
     * （单选逻辑）重置已经选中的项目并选中当前项目
     *
     * @param bean
     */
    private void resetAndSelectCurrentItem(VoteBean bean) {
        if (bean == null) {
            return;
        }
        for (VoteBean item : mVoteData) {
            if (item == bean) {//当前选项
                //已选中则总票数-1，未选中则总票数+1
                if (item.isChecked) {
                    mTotalVoteNum--;
                    item.currentItemVoteNum--;
                    setHasVote(false);
                } else {
                    mTotalVoteNum++;
                    item.currentItemVoteNum++;
                    setHasVote(true);
                }
                item.isChecked = !item.isChecked;
            } else {//非当前选项
                if (item.isChecked) {
                    mTotalVoteNum--;
                    item.currentItemVoteNum--;
                }
                item.isChecked = false;
            }


        }
        //需要等循环完毕在更新百分比，防止总量错误
        for (VoteBean item : mVoteData) {
            //更新百分比
            item.percent = getPercent(item.currentItemVoteNum, mTotalVoteNum);
            Log.e("Test", "当前数量:" + item.currentItemVoteNum + " 总数量：" + mTotalVoteNum + " 百分比：" + item.percent);
        }

    }


    public static int getPercent(int a, int b) {
        if (b == 0) {
            return 0;
        }
        //四舍五入
        return Math.round((float) a * 100 / b);
    }
}
