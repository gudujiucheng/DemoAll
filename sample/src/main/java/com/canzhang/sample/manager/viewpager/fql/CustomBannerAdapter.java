package com.canzhang.sample.manager.viewpager.fql;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class CustomBannerAdapter<T> extends PagerAdapter {

    private List<T> mList;

    @Override
    public int getCount() {
        if (mList == null || mList.size() == 0) {
            return 0;
        }
        return CustomBannerVp.VIEWPAGER_TOTAL_NUMBER;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position = position % mList.size();
        T t = mList.get(position);
        View v = getViewGroupItemView(container, t, position);
        container.addView(v);
        return v;
    }

    public void setList(List<T> list) {
        mList = list;
        notifyDataSetChanged();
    }

    protected abstract View getViewGroupItemView(ViewGroup container, T t, int position);
}
