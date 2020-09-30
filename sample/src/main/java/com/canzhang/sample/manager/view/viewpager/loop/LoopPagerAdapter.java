package com.canzhang.sample.manager.view.viewpager.loop;

import androidx.viewpager.widget.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class LoopPagerAdapter extends PagerAdapter {

    private List<View> mList;
    private View mView;

    public LoopPagerAdapter() {
        mList = new ArrayList<>();
    }

    /**
     * 返回Integer的最大值
     */
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d("I am postion cx cx xcx", String.valueOf(position));
        if (getSize() != 0) {
            if (mList.size() <= (position % getSize())) {
                for (int i = mList.size();i <= position % getSize();++i) {
                    mList.add(getView(container,i));
                }
            }
            mView = mList.get(position % getSize());
            if (mView.getParent() != null) {
                container.removeView(mView);
            }
            container.addView(mView);
        }

        return mView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 滑动下一张图时当前的图
        if (getSize() != 0 && position != 0) {
            container.removeView(mList.get(position % getSize()));
        }
    }

    /**
     *  获取要显示的View
     * @param container
     * @param position
     * @return
     */
    public abstract View getView(ViewGroup container,int position);

    /**
     *  获取实际ItemView的数量
     * @return
     */
    public abstract int getSize();


}
