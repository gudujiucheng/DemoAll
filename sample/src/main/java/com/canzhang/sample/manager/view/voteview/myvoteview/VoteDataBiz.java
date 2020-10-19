package com.canzhang.sample.manager.view.voteview.myvoteview;

import android.util.Log;
import android.util.Pair;

import com.example.base.utils.ToastUtil;

import java.util.ArrayList;
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



    public VoteDataBiz(Pair<VoteListInfoBean, List<VoteBean>> voteListInfoBeanListPair) {
        this.mVoteListInfoBeanListPair = voteListInfoBeanListPair;
        if (mVoteListInfoBeanListPair == null) {
            return;
        }
        mVoteListInfoBean = mVoteListInfoBeanListPair.first;
        mVoteData = mVoteListInfoBeanListPair.second;
        if (mVoteListInfoBean != null) {
            if (mVoteListInfoBean.mMaxSelectNum > 0) {
                this.mMaxSelectNum = mVoteListInfoBean.mMaxSelectNum;
            }

            this.mMaxShowNum = mVoteListInfoBean.mMaxShowNum;
        }


        mTotalVoteNum = getTotalVoteNum();


        formatData();
    }

    public boolean isHasVote() {
        //注意始终要引用同一个对象
        if (mVoteListInfoBean != null) {
            return mVoteListInfoBean.mIsHasVote;
        } else {
            return false;
        }
    }

    public void setHasVote(boolean hasVote) {
        if (mVoteListInfoBean == null) {
            return;
        }
        mVoteListInfoBean.mIsHasVote = hasVote;
    }

    /**
     * 是否需要执行item动画
     * @return
     */
    public boolean isNeedAnim() {
        //注意始终要引用同一个对象
        if (mVoteListInfoBean != null) {
            return mVoteListInfoBean.mIsNeedAnim;
        } else {
            return false;
        }
    }

    /**
     * 是否需要执行item动画
     * @return
     */
    public void setIsNeedAnim(boolean isNeedAnim) {
        //注意始终要引用同一个对象
        if (mVoteListInfoBean == null) {
            return;
        }
        mVoteListInfoBean.mIsNeedAnim = isNeedAnim;
    }





    /**
     * 多选的项目数量调整
     *
     * @param isAdd 是：增加  否：减去
     */
    public void changeMultipleHasVoteNum(boolean isAdd) {
        if (mVoteListInfoBean == null) {
            return;
        }
        if (isAdd) {
            mVoteListInfoBean.mHasSelectedNumBeforeVote++;
        } else {
            if (mVoteListInfoBean.mHasSelectedNumBeforeVote <= 0) {
                mVoteListInfoBean.mHasSelectedNumBeforeVote = 0;
                return;
            }
            mVoteListInfoBean.mHasSelectedNumBeforeVote--;
        }

    }


    /**
     * 获取多选场景下 当前已经选中的数量
     *
     * @return
     */
    public int getMultipleHasVoteNum() {
        if (mVoteListInfoBean == null) {
            return 0;
        }
        return mVoteListInfoBean.mHasSelectedNumBeforeVote;

    }

    private void formatData() {
        if (mVoteData == null || mVoteData.size() == 0) {
            return;
        }
        int num = 0;

        for (int i = 0; i < mVoteData.size(); i++) {
            VoteBean item = mVoteData.get(i);
            if (item.type == VoteBean.VOTE_TYPE) {
                num++;
                if (num > mMaxShowNum) {//控制展示隐藏
                    item.isShow = false;
                } else {
                    item.isShow = true;
                }
                item.percent = getPercent(item.currentItemVoteNum, mTotalVoteNum);
            }
        }

        //FIXME 最好在外层控制  避免每次滑动创建大量对象
        if(mVoteData.size()>mMaxShowNum){////如果数量超出最大可展示数量则展示
            mVoteData.add(new VoteBean(VoteBean.MORE_TYPE));
        }
        if(!isHasVote()){//TODO 这里还需要根据 是否超出最大可展示数量 展示不同样式
            mVoteData.add(new VoteBean(VoteBean.VOTE_BUTTON_TYPE));
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
     * 数据被点击(投票前)
     *
     * @param bean
     */
    public void onItemClickBeforeVote(VoteBean bean) {
        if (bean == null) {
            return;
        }
        if (mMaxSelectNum <= 1) {//单选
            if (mMaxSelectNum < 0) {//容错
                mMaxSelectNum = 1;
            }
            resetAndSelectCurrentItemWithSingle(bean);
        } else {//FIXME 多选
            //多选应该是点击提交的时候 才更新数据，另外多选的ui样式也不同  这里暂时空值
            resetAndSelectCurrentItemWithMultiple(bean);
        }

    }

    /**
     * 多选逻辑
     *
     * @param bean
     */
    private void resetAndSelectCurrentItemWithMultiple(VoteBean bean) {
        if (bean == null) {
            return;
        }
        for (VoteBean item : mVoteData) {//TODO 这个逻辑可以优化 每次搞个指针指向最后一个选中的对象
            if (item == bean) {//当前选项
                //已选中则总票数-1，未选中则总票数+1
                if (item.isCheckedOnBeforeVote) {
                    mTotalVoteNum--;
                    item.currentItemVoteNum--;
                    item.isCheckedOnBeforeVote = false;
                    changeMultipleHasVoteNum(false);
//                    setHasVote(false);
                } else {
                    if (getMultipleHasVoteNum() >= mMaxSelectNum) {
                        ToastUtil.toastShort("已经达到最大选项");
                        continue;
                    }
                    mTotalVoteNum++;
                    item.currentItemVoteNum++;
                    item.isCheckedOnBeforeVote = true;
//                    setHasVote(true);
                    changeMultipleHasVoteNum(true);
                }

            }


        }
        //需要等循环完毕在更新百分比，防止总量错误
        for (VoteBean item : mVoteData) {
            //更新百分比
            item.percent = getPercent(item.currentItemVoteNum, mTotalVoteNum);
            Log.e("Test", "当前数量:" + item.currentItemVoteNum + " 总数量：" + mTotalVoteNum + " 百分比：" + item.percent);
        }
    }


    /**
     * （单选逻辑）重置已经选中的项目并选中当前项目
     *
     * @param bean
     */
    private void resetAndSelectCurrentItemWithSingle(VoteBean bean) {
        if (bean == null) {
            return;
        }
        for (VoteBean item : mVoteData) {//TODO 这个逻辑可以优化 每次搞个指针指向最后一个选中的对象
            if (item == bean) {//当前选项
                //已选中则总票数-1，未选中则总票数+1
                if (item.isCheckedOnBeforeVote) {
                    mTotalVoteNum--;
                    item.currentItemVoteNum--;
//                    setHasVote(false);
                } else {
                    mTotalVoteNum++;
                    item.currentItemVoteNum++;
//                    setHasVote(true);
                }
                item.isCheckedOnBeforeVote = !item.isCheckedOnBeforeVote;
            } else {//非当前选项
                if (item.isCheckedOnBeforeVote) {
                    mTotalVoteNum--;
                    item.currentItemVoteNum--;
                }
                item.isCheckedOnBeforeVote = false;
            }


        }
        //需要等循环完毕在更新百分比，防止总量错误
        for (VoteBean item : mVoteData) {
            //更新百分比
            item.percent = getPercent(item.currentItemVoteNum, mTotalVoteNum);
            Log.e("Test", "当前数量:" + item.currentItemVoteNum + " 总数量：" + mTotalVoteNum + " 百分比：" + item.percent);
        }

    }


    /**
     * 提交选票
     */
    public void onSubmitVote() {
        if (isHasVote()) {//容错
            ToastUtil.toastShort("已经投票过了 应当隐藏此选项");
            return;
        }
        ArrayList<String> hasVoteList = new ArrayList<>();
        for (VoteBean item : mVoteData) {
            if (item.isCheckedOnBeforeVote) {
                hasVoteList.add(item.title);
            }
            item.isCheckedOnAfterVote = item.isCheckedOnBeforeVote;
        }
        setHasVote(hasVoteList.size() > 0);
    }


    public static int getPercent(int a, int b) {
        if (b == 0) {
            return 0;
        }
        //四舍五入
        return Math.round((float) a * 100 / b);
    }
}
